package com.yida.spider4j.crawler.utils.collection;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.primitives.Ints;

/**
 * Basic implementation of {@code Multiset<E>} backed by an instance of
 * {@code Map<E, AtomicInteger>}.
 * 
 * <p>
 * For serialization to work, the subclass must specify explicit
 * {@code readObject} and {@code writeObject} methods.
 * 
 * @author Kevin Bourrillion
 */
@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E>
		implements Serializable {

	// TODO(kevinb): test whether a custom IntegerHolder would be faster
	private transient Map<E, AtomicInteger> backingMap;

	/*
	 * Cache the size for efficiency. Using a long lets us avoid the need for
	 * overflow checking and ensures that size() will function correctly even if
	 * the multiset had once been larger than Integer.MAX_VALUE.
	 */
	private transient long size;

	/** Standard constructor. */
	protected AbstractMapBasedMultiset(Map<E, AtomicInteger> backingMap) {
		this.backingMap = Preconditions.checkNotNull(backingMap);
		this.size = super.size();
	}

	Map<E, AtomicInteger> backingMap() {
		return backingMap;
	}

	/** Used during deserialization only. The backing map must be empty. */
	void setBackingMap(Map<E, AtomicInteger> backingMap) {
		this.backingMap = backingMap;
	}

	// Required Implementations

	private transient EntrySet entrySet;

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Invoking {@link Multiset.Entry#getCount} on an entry in the returned set
	 * always returns the current count of that element in the multiset, as
	 * opposed to the count at the time the entry was retrieved.
	 */
	@Override
	public Set<Multiset.Entry<E>> entrySet() {
		EntrySet result = entrySet;
		if (result == null) {
			entrySet = result = new EntrySet();
		}
		return result;
	}

	private class EntrySet extends AbstractSet<Multiset.Entry<E>> {
		@Override
		public Iterator<Multiset.Entry<E>> iterator() {
			final Iterator<Map.Entry<E, AtomicInteger>> backingEntries = backingMap
					.entrySet().iterator();
			return new Iterator<Multiset.Entry<E>>() {
				Map.Entry<E, AtomicInteger> toRemove;

				public boolean hasNext() {
					return backingEntries.hasNext();
				}

				public Multiset.Entry<E> next() {
					final Map.Entry<E, AtomicInteger> mapEntry = backingEntries
							.next();
					toRemove = mapEntry;
					return new Multisets.AbstractEntry<E>() {
						public E getElement() {
							return mapEntry.getKey();
						}

						public int getCount() {
							int count = mapEntry.getValue().get();
							if (count == 0) {
								AtomicInteger frequency = backingMap
										.get(getElement());
								if (frequency != null) {
									count = frequency.get();
								}
							}
							return count;
						}
					};
				}

				public void remove() {
					Preconditions
							.checkState(toRemove != null,
									"no calls to next() since the last call to remove()");
					size -= toRemove.getValue().getAndSet(0);
					backingEntries.remove();
					toRemove = null;
				}
			};
		}

		@Override
		public int size() {
			return backingMap.size();
		}

		// The following overrides are for better performance.

		@Override
		public void clear() {
			for (AtomicInteger frequency : backingMap.values()) {
				frequency.set(0);
			}
			backingMap.clear();
			size = 0L;
		}

		@Override
		public boolean contains(Object o) {
			if (o instanceof Entry) {
				Entry<?> entry = (Entry<?>) o;
				int count = count(entry.getElement());
				return (count == entry.getCount()) && (count > 0);
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (contains(o)) {
				Entry<?> entry = (Entry<?>) o;
				AtomicInteger frequency = backingMap.remove(entry.getElement());
				int numberRemoved = frequency.getAndSet(0);
				size -= numberRemoved;
				return true;
			}
			return false;
		}
	}

	// Optimizations - Query Operations

	@Override
	public int size() {
		return Ints.saturatedCast(size);
	}

	@Override
	public Iterator<E> iterator() {
		return new MapBasedMultisetIterator();
	}

	/*
	 * Not subclassing AbstractMultiset$MultisetIterator because next() needs to
	 * retrieve the Map.Entry<E, AtomicInteger> entry, which can then be used
	 * for a more efficient remove() call.
	 */
	private class MapBasedMultisetIterator implements Iterator<E> {
		final Iterator<Map.Entry<E, AtomicInteger>> entryIterator;
		Map.Entry<E, AtomicInteger> currentEntry;
		int occurrencesLeft;
		boolean canRemove;

		MapBasedMultisetIterator() {
			this.entryIterator = backingMap.entrySet().iterator();
		}

		public boolean hasNext() {
			return occurrencesLeft > 0 || entryIterator.hasNext();
		}

		public E next() {
			if (occurrencesLeft == 0) {
				currentEntry = entryIterator.next();
				occurrencesLeft = currentEntry.getValue().get();
			}
			occurrencesLeft--;
			canRemove = true;
			return currentEntry.getKey();
		}

		public void remove() {
			Preconditions.checkState(canRemove,
					"no calls to next() since the last call to remove()");
			int frequency = currentEntry.getValue().get();
			if (frequency <= 0) {
				throw new ConcurrentModificationException();
			}
			if (currentEntry.getValue().addAndGet(-1) == 0) {
				entryIterator.remove();
			}
			size--;
			canRemove = false;
		}
	}

	@Override
	public boolean contains(Object element) {
		AtomicInteger frequency = backingMap.get(element);
		return frequency != null && frequency.get() > 0;
	}

	@Override
	public int count(Object element) {
		AtomicInteger frequency = backingMap.get(element);
		return (frequency == null) ? 0 : frequency.get();
	}

	// Optional Operations - Modification Operations

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException
	 *             if the call would result in more than
	 *             {@link Integer#MAX_VALUE} occurrences of {@code element} in
	 *             this multiset.
	 */
	@Override
	public int add(E element, int occurrences) {
		if (occurrences == 0) {
			return count(element);
		}
		Preconditions.checkArgument(occurrences > 0,
				"occurrences cannot be negative: %s", occurrences);
		AtomicInteger frequency = backingMap.get(element);
		int oldCount;
		if (frequency == null) {
			oldCount = 0;
			backingMap.put(element, new AtomicInteger(occurrences));
		} else {
			oldCount = frequency.get();
			long newCount = (long) oldCount + (long) occurrences;
			Preconditions.checkArgument(newCount <= Integer.MAX_VALUE,
					"too many occurrences: %s", newCount);
			frequency.getAndAdd(occurrences);
		}
		size += occurrences;
		return oldCount;
	}

	@Override
	public int remove(Object element, int occurrences) {
		if (occurrences == 0) {
			return count(element);
		}
		Preconditions.checkArgument(occurrences > 0,
				"occurrences cannot be negative: %s", occurrences);
		AtomicInteger frequency = backingMap.get(element);
		if (frequency == null) {
			return 0;
		}

		int oldCount = frequency.get();

		int numberRemoved;
		if (oldCount > occurrences) {
			numberRemoved = occurrences;
		} else {
			numberRemoved = oldCount;
			backingMap.remove(element);
		}

		frequency.addAndGet(-numberRemoved);
		size -= numberRemoved;
		return oldCount;
	}

	// Roughly a 33% performance improvement over AbstractMultiset.setCount().
	@Override
	public int setCount(E element, int count) {
		Multisets.checkNonnegative(count, "count");

		AtomicInteger existingCounter;
		int oldCount;
		if (count == 0) {
			existingCounter = backingMap.remove(element);
			oldCount = getAndSet(existingCounter, count);
		} else {
			existingCounter = backingMap.get(element);
			oldCount = getAndSet(existingCounter, count);

			if (existingCounter == null) {
				backingMap.put(element, new AtomicInteger(count));
			}
		}

		size += (count - oldCount);
		return oldCount;
	}

	private static int getAndSet(AtomicInteger i, int count) {
		if (i == null) {
			return 0;
		}

		return i.getAndSet(count);
	}

	private int removeAllOccurrences(Object element, Map<E, AtomicInteger> map) {
		AtomicInteger frequency = map.remove(element);
		if (frequency == null) {
			return 0;
		}
		int numberRemoved = frequency.getAndSet(0);
		size -= numberRemoved;
		return numberRemoved;
	}

	// Views

	@Override
	Set<E> createElementSet() {
		return new MapBasedElementSet(backingMap);
	}

	class MapBasedElementSet extends ForwardingSet<E> {

		// This mapping is the usually the same as {@code backingMap}, but can
		// be a
		// submap in some implementations.
		private final Map<E, AtomicInteger> map;
		private final Set<E> delegate;

		MapBasedElementSet(Map<E, AtomicInteger> map) {
			this.map = map;
			delegate = map.keySet();
		}

		@Override
		protected Set<E> delegate() {
			return delegate;
		}

		@Override
		public Iterator<E> iterator() {
			final Iterator<Map.Entry<E, AtomicInteger>> entries = map
					.entrySet().iterator();
			return new Iterator<E>() {
				Map.Entry<E, AtomicInteger> toRemove;

				public boolean hasNext() {
					return entries.hasNext();
				}

				public E next() {
					toRemove = entries.next();
					return toRemove.getKey();
				}

				public void remove() {
					Preconditions
							.checkState(toRemove != null,
									"no calls to next() since the last call to remove()");
					size -= toRemove.getValue().getAndSet(0);
					entries.remove();
					toRemove = null;
				}
			};
		}

		@Override
		public boolean remove(Object element) {
			return removeAllOccurrences(element, map) != 0;
		}

		@Override
		public boolean removeAll(Collection<?> elementsToRemove) {
			return Iterators.removeAll(iterator(), elementsToRemove);
		}

		@Override
		public boolean retainAll(Collection<?> elementsToRetain) {
			return Iterators.retainAll(iterator(), elementsToRetain);
		}

		@Override
		public void clear() {
			if (map == backingMap) {
				AbstractMapBasedMultiset.this.clear();
			} else {
				Iterator<E> i = iterator();
				while (i.hasNext()) {
					i.next();
					i.remove();
				}
			}
		}

		public Map<E, AtomicInteger> getMap() {
			return map;
		}
	}

	// Don't allow default serialization.
	@GwtIncompatible("java.io.ObjectStreamException")
	// actually used during deserialization
	private void readObjectNoData() throws ObjectStreamException {
		throw new InvalidObjectException("Stream data required");
	}

	@GwtIncompatible("not needed in emulated source.")
	private static final long serialVersionUID = -2250766705698539974L;
}
