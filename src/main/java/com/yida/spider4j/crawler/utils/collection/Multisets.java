package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.yida.spider4j.crawler.utils.collection.Multiset.Entry;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Objects;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.primitives.Ints;

/**
 * Provides static utility methods for creating and working with {@link
 * Multiset} instances.
 *
 * @author Kevin Bourrillion
 * @author Mike Bostock
 * @author Louis Wasserman
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
@SuppressWarnings({"unchecked","rawtypes"})
public final class Multisets {
  private Multisets() {}

  /**
   * Returns an unmodifiable view of the specified multiset. Query operations on
   * the returned multiset "read through" to the specified multiset, and
   * attempts to modify the returned multiset result in an
   * {@link UnsupportedOperationException}.
   *
   * <p>The returned multiset will be serializable if the specified multiset is
   * serializable.
   *
   * @param multiset the multiset for which an unmodifiable view is to be
   *     generated
   * @return an unmodifiable view of the multiset
   */
  public static <E> Multiset<E> unmodifiableMultiset(
      Multiset<? extends E> multiset) {
    return new UnmodifiableMultiset<E>(Preconditions.checkNotNull(multiset));
  }

  private static class UnmodifiableMultiset<E>
      extends ForwardingMultiset<E> implements Serializable {
    final Multiset<? extends E> delegate;

    UnmodifiableMultiset(Multiset<? extends E> delegate) {
      this.delegate = delegate;
    }

    @Override protected Multiset<E> delegate() {
      // This is safe because all non-covariant methods are overriden
      return (Multiset) delegate;
    }

    transient Set<E> elementSet;

    @Override public Set<E> elementSet() {
      Set<E> es = elementSet;
      return (es == null)
          ? elementSet = Collections.<E>unmodifiableSet(delegate.elementSet())
          : es;
    }

    transient Set<Multiset.Entry<E>> entrySet;

	@Override public Set<Multiset.Entry<E>> entrySet() {
      Set<Multiset.Entry<E>> es = entrySet;
      return (es == null)
          // Safe because the returned set is made unmodifiable and Entry
          // itself is readonly
          ? entrySet = (Set) Collections.unmodifiableSet(delegate.entrySet())
          : es;
    }

    @Override public Iterator<E> iterator() {
      // Safe because the returned Iterator is made unmodifiable
      return (Iterator) Iterators.unmodifiableIterator(delegate.iterator());
    }

    @Override public boolean add(E element) {
      throw new UnsupportedOperationException();
    }

    @Override public int add(E element, int occurences) {
      throw new UnsupportedOperationException();
    }

    @Override public boolean addAll(Collection<? extends E> elementsToAdd) {
      throw new UnsupportedOperationException();
    }

    @Override public boolean remove(Object element) {
      throw new UnsupportedOperationException();
    }

    @Override public int remove(Object element, int occurrences) {
      throw new UnsupportedOperationException();
    }

    @Override public boolean removeAll(Collection<?> elementsToRemove) {
      throw new UnsupportedOperationException();
    }

    @Override public boolean retainAll(Collection<?> elementsToRetain) {
      throw new UnsupportedOperationException();
    }

    @Override public void clear() {
      throw new UnsupportedOperationException();
    }

    @Override public int setCount(E element, int count) {
      throw new UnsupportedOperationException();
    }

    @Override public boolean setCount(E element, int oldCount, int newCount) {
      throw new UnsupportedOperationException();
    }

    private static final long serialVersionUID = 0;
  }

  /**
   * Returns an immutable multiset entry with the specified element and count.
   *
   * @param e the element to be associated with the returned entry
   * @param n the count to be associated with the returned entry
   * @throws IllegalArgumentException if {@code n} is negative
   */
  public static <E> Multiset.Entry<E> immutableEntry(
      final E e, final int n) {
    Preconditions.checkArgument(n >= 0);
    return new AbstractEntry<E>() {
      public E getElement() {
        return e;
      }
      public int getCount() {
        return n;
      }
    };
  }

  /**
   * Returns a multiset view of the specified set. The multiset is backed by the
   * set, so changes to the set are reflected in the multiset, and vice versa.
   * If the set is modified while an iteration over the multiset is in progress
   * (except through the iterator's own {@code remove} operation) the results of
   * the iteration are undefined.
   *
   * <p>The multiset supports element removal, which removes the corresponding
   * element from the set. It does not support the {@code add} or {@code addAll}
   * operations, nor does it support the use of {@code setCount} to add
   * elements.
   *
   * <p>The returned multiset will be serializable if the specified set is
   * serializable. The multiset is threadsafe if the set is threadsafe.
   *
   * @param set the backing set for the returned multiset view
   */
  static <E> Multiset<E> forSet(Set<E> set) {
    return new SetMultiset<E>(set);
  }

  /** @see Multisets#forSet */
  private static class SetMultiset<E> extends ForwardingCollection<E>
      implements Multiset<E>, Serializable {
    final Set<E> delegate;

    SetMultiset(Set<E> set) {
      delegate = Preconditions.checkNotNull(set);
    }

    @Override protected Set<E> delegate() {
      return delegate;
    }

    public int count(Object element) {
      return delegate.contains(element) ? 1 : 0;
    }

    public int add(E element, int occurrences) {
      throw new UnsupportedOperationException();
    }

    public int remove(Object element, int occurrences) {
      if (occurrences == 0) {
        return count(element);
      }
      Preconditions.checkArgument(occurrences > 0);
      return delegate.remove(element) ? 1 : 0;
    }

    transient Set<E> elementSet;

    public Set<E> elementSet() {
      Set<E> es = elementSet;
      return (es == null) ? elementSet = new ElementSet() : es;
    }

    transient Set<Entry<E>> entrySet;

    public Set<Entry<E>> entrySet() {
      Set<Entry<E>> es = entrySet;
      return (es == null) ? entrySet = new EntrySet() : es;
    }

    @Override public boolean add(E o) {
      throw new UnsupportedOperationException();
    }

    @Override public boolean addAll(Collection<? extends E> c) {
      throw new UnsupportedOperationException();
    }

    public int setCount(E element, int count) {
      checkNonnegative(count, "count");

      if (count == count(element)) {
        return count;
      } else if (count == 0) {
        remove(element);
        return 1;
      } else {
        throw new UnsupportedOperationException();
      }
    }

    public boolean setCount(E element, int oldCount, int newCount) {
      return setCountImpl(this, element, oldCount, newCount);
    }

    @Override public boolean equals(Object object) {
      if (object instanceof Multiset) {
        Multiset<?> that = (Multiset<?>) object;
        return this.size() == that.size() && delegate.equals(that.elementSet());
      }
      return false;
    }

    @Override public int hashCode() {
      int sum = 0;
      for (E e : this) {
        sum += ((e == null) ? 0 : e.hashCode()) ^ 1;
      }
      return sum;
    }

    /** @see SetMultiset#elementSet */
    class ElementSet extends ForwardingSet<E> {
      @Override protected Set<E> delegate() {
        return delegate;
      }

      @Override public boolean add(E o) {
        throw new UnsupportedOperationException();
      }

      @Override public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
      }
    }

    /** @see SetMultiset#entrySet */
    class EntrySet extends AbstractSet<Entry<E>> {
      @Override public int size() {
        return delegate.size();
      }
      @Override public Iterator<Entry<E>> iterator() {
        return new Iterator<Entry<E>>() {
          final Iterator<E> elements = delegate.iterator();

          public boolean hasNext() {
            return elements.hasNext();
          }
          public Entry<E> next() {
            return immutableEntry(elements.next(), 1);
          }
          public void remove() {
            elements.remove();
          }
        };
      }
      // TODO(kevinb): faster contains, remove
    }

    private static final long serialVersionUID = 0;
  }

  /**
   * Returns the expected number of distinct elements given the specified
   * elements. The number of distinct elements is only computed if {@code
   * elements} is an instance of {@code Multiset}; otherwise the default value
   * of 11 is returned.
   */
  static int inferDistinctElements(Iterable<?> elements) {
    if (elements instanceof Multiset) {
      return ((Multiset<?>) elements).elementSet().size();
    }
    return 11; // initial capacity will be rounded up to 16
  }

  /**
   * Returns an unmodifiable <b>view</b> of the intersection of two multisets.
   * An element's count in the multiset is the smaller of its counts in the two
   * backing multisets. The iteration order of the returned multiset matches the
   * element set of {@code multiset1}, with repeated occurrences of the same
   * element appearing consecutively.
   *
   * <p>Results are undefined if {@code multiset1} and {@code multiset2} are
   * based on different equivalence relations (as {@code HashMultiset} and
   * {@code TreeMultiset} are).
   *
   * @since 2
   */
  public static <E> Multiset<E> intersection(
      final Multiset<E> multiset1, final Multiset<?> multiset2) {
    Preconditions.checkNotNull(multiset1);
    Preconditions.checkNotNull(multiset2);

    return new AbstractMultiset<E>() {
      @Override public int count(Object element) {
        int count1 = multiset1.count(element);
        return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
      }

      @Override Set<E> createElementSet() {
        return Sets.intersection(
            multiset1.elementSet(), multiset2.elementSet());
      }

      @Override public Set<Entry<E>> entrySet() {
        return entrySet;
      }

      final Set<Entry<E>> entrySet = new AbstractSet<Entry<E>>() {
        @Override public Iterator<Entry<E>> iterator() {
          final Iterator<Entry<E>> iterator1 = multiset1.entrySet().iterator();
          return new AbstractIterator<Entry<E>>() {
            @Override protected Entry<E> computeNext() {
              while (iterator1.hasNext()) {
                Entry<E> entry1 = iterator1.next();
                E element = entry1.getElement();
                int count
                    = Math.min(entry1.getCount(), multiset2.count(element));
                if (count > 0) {
                  return Multisets.immutableEntry(element, count);
                }
              }
              return endOfData();
            }
          };
        }

        @Override public int size() {
          return elementSet().size();
        }

        @Override public boolean contains(Object o) {
          if (o instanceof Entry) {
            Entry<?> entry = (Entry<?>) o;
            int entryCount = entry.getCount();
            return (entryCount > 0)
                && (count(entry.getElement()) == entryCount);
          }
          return false;
        }

        @Override public boolean isEmpty() {
          return elementSet().isEmpty();
        }
      };
    };
  }

  /**
   * Implementation of the {@code equals}, {@code hashCode}, and
   * {@code toString} methods of {@link Multiset.Entry}.
   */
  abstract static class AbstractEntry<E> implements Multiset.Entry<E> {
    /**
     * Indicates whether an object equals this entry, following the behavior
     * specified in {@link Multiset.Entry#equals}.
     */
    @Override public boolean equals(Object object) {
      if (object instanceof Multiset.Entry) {
        Multiset.Entry<?> that = (Multiset.Entry<?>) object;
        return this.getCount() == that.getCount()
            && Objects.equal(this.getElement(), that.getElement());
      }
      return false;
    }

    /**
     * Return this entry's hash code, following the behavior specified in
     * {@link Multiset.Entry#hashCode}.
     */
    @Override public int hashCode() {
      E e = getElement();
      return ((e == null) ? 0 : e.hashCode()) ^ getCount();
    }

    /**
     * Returns a string representation of this multiset entry. The string
     * representation consists of the associated element if the associated count
     * is one, and otherwise the associated element followed by the characters
     * " x " (space, x and space) followed by the count. Elements and counts are
     * converted to strings as by {@code String.valueOf}.
     */
    @Override public String toString() {
      String text = String.valueOf(getElement());
      int n = getCount();
      return (n == 1) ? text : (text + " x " + n);
    }
  } 

  /**
   * An implementation of {@link Multiset#equals}.
   */
  static boolean equalsImpl(Multiset<?> multiset, Object object) {
    if (object == multiset) {
      return true;
    }
    if (object instanceof Multiset) {
      Multiset<?> that = (Multiset<?>) object;
      /*
       * We can't simply check whether the entry sets are equal, since that
       * approach fails when a TreeMultiset has a comparator that returns 0
       * when passed unequal elements.
       */

      if (multiset.size() != that.size()
          || multiset.entrySet().size() != that.entrySet().size()) {
        return false;
      }
      for (Entry<?> entry : that.entrySet()) {
        if (multiset.count(entry.getElement()) != entry.getCount()) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * An implementation of {@link Multiset#addAll}.
   */
  static <E> boolean addAllImpl(
      Multiset<E> self, Collection<? extends E> elements) {
    if (elements.isEmpty()) {
      return false;
    }
    if (elements instanceof Multiset) {
      Multiset<? extends E> that = cast(elements);
      for (Entry<? extends E> entry : that.entrySet()) {
        self.add(entry.getElement(), entry.getCount());
      }
    } else {
      Iterators.addAll(self, elements.iterator());
    }
    return true;
  }

  /**
   * An implementation of {@link Multiset#removeAll}.
   */
  static boolean removeAllImpl(
      Multiset<?> self, Collection<?> elementsToRemove) {
    Collection<?> collection = (elementsToRemove instanceof Multiset)
        ? ((Multiset<?>) elementsToRemove).elementSet() : elementsToRemove;

    return self.elementSet().removeAll(collection);
  }

  /**
   * An implementation of {@link Multiset#retainAll}.
   */
  static boolean retainAllImpl(
      Multiset<?> self, Collection<?> elementsToRetain) {
    Collection<?> collection = (elementsToRetain instanceof Multiset)
        ? ((Multiset<?>) elementsToRetain).elementSet() : elementsToRetain;

    return self.elementSet().retainAll(collection);
  }

  /**
   * An implementation of {@link Multiset#setCount(Object, int)}.
   */
  static <E> int setCountImpl(Multiset<E> self, E element, int count) {
    checkNonnegative(count, "count");

    int oldCount = self.count(element);

    int delta = count - oldCount;
    if (delta > 0) {
      self.add(element, delta);
    } else if (delta < 0) {
      self.remove(element, -delta);
    }

    return oldCount;
  }

  /**
   * An implementation of {@link Multiset#setCount(Object, int, int)}.
   */
  static <E> boolean setCountImpl(
      Multiset<E> self, E element, int oldCount, int newCount) {
    checkNonnegative(oldCount, "oldCount");
    checkNonnegative(newCount, "newCount");

    if (self.count(element) == oldCount) {
      self.setCount(element, newCount);
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * An implementation of {@link Multiset#elementSet}.
   */
  static <E> Set<E> elementSetImpl(Multiset<E> self){
    return new ElementSetImpl<E>(self);
  }
  
  private static final class ElementSetImpl<E> extends AbstractSet<E>
      implements Serializable {
    private final Multiset<E> multiset;
    
    ElementSetImpl(Multiset<E> multiset) {
      this.multiset = multiset;
    }

    @Override public boolean add(E e) {
      throw new UnsupportedOperationException();
    }

    @Override public boolean addAll(Collection<? extends E> c) {
      throw new UnsupportedOperationException();
    }

    @Override public void clear() {
      multiset.clear();
    }

    @Override public boolean contains(Object o) {
      return multiset.contains(o);
    }

    @Override public boolean containsAll(Collection<?> c) {
      return multiset.containsAll(c);
    }

    @Override public boolean isEmpty() {
      return multiset.isEmpty();
    }

    @Override public Iterator<E> iterator() {
      final Iterator<Entry<E>> entryIterator = multiset.entrySet().iterator();
      return new Iterator<E>() {

        @Override public boolean hasNext() {
          return entryIterator.hasNext();
        }

        @Override public E next() {
          return entryIterator.next().getElement();
        }

        @Override public void remove() {
          entryIterator.remove();
        }
      };
    }

    @Override public boolean remove(Object o) {
      int count = multiset.count(o);
      if (count > 0) {
        multiset.remove(o, count);
        return true;
      }
      return false;
    }

    @Override public int size() {
      return multiset.entrySet().size();
    }

    private static final long serialVersionUID = 0;
  }
  
  /**
   * An implementation of {@link Multiset#iterator}.
   */
  static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
    return new MultisetIteratorImpl<E>(
        multiset, multiset.entrySet().iterator());
  }

  static final class MultisetIteratorImpl<E> implements Iterator<E> {
    private final Multiset<E> multiset;
    private final Iterator<Entry<E>> entryIterator;
    private Entry<E> currentEntry;
    /** Count of subsequent elements equal to current element */
    private int laterCount;
    /** Count of all elements equal to current element */
    private int totalCount;
    private boolean canRemove;

    MultisetIteratorImpl(
        Multiset<E> multiset, Iterator<Entry<E>> entryIterator) {
      this.multiset = multiset;
      this.entryIterator = entryIterator;
    }

    public boolean hasNext() {
      return laterCount > 0 || entryIterator.hasNext();
    }

    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (laterCount == 0) {
        currentEntry = entryIterator.next();
        totalCount = laterCount = currentEntry.getCount();
      }
      laterCount--;
      canRemove = true;
      return currentEntry.getElement();
    }

    public void remove() {
      Preconditions.checkState(
          canRemove, "no calls to next() since the last call to remove()");
      if (totalCount == 1) {
        entryIterator.remove();
      } else {
        multiset.remove(currentEntry.getElement());
      }
      totalCount--;
      canRemove = false;
    }
  }
  
  /**
   * An implementation of {@link Multiset#size}.
   */
  static int sizeImpl(Multiset<?> multiset) {
    long size = 0;
    for (Entry<?> entry : multiset.entrySet()) {
      size += entry.getCount();
    }
    return Ints.saturatedCast(size);
  }

  static void checkNonnegative(int count, String name) {
    Preconditions.checkArgument(count >= 0, "%s cannot be negative: %s", name, count);
  }

  /**
   * Used to avoid http://bugs.sun.com/view_bug.do?bug_id=6558557
   */
  static <T> Multiset<T> cast(Iterable<T> iterable) {
    return (Multiset<T>) iterable;
  }
}
