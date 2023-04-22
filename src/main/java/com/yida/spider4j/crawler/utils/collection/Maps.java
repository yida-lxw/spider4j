package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

import com.yida.spider4j.crawler.utils.collection.MapDifference.ValueDifference;
import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;
import com.yida.spider4j.crawler.utils.collection.base.Function;
import com.yida.spider4j.crawler.utils.collection.base.Joiner.MapJoiner;
import com.yida.spider4j.crawler.utils.collection.base.Objects;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.base.Predicates;
import com.yida.spider4j.crawler.utils.collection.base.Supplier;
import com.yida.spider4j.crawler.utils.collection.primitives.Ints;

/**
 * Static utility methods pertaining to {@link Map} instances. Also see this
 * class's counterparts {@link Lists} and {@link Sets}.
 * 
 * @author Kevin Bourrillion
 * @author Mike Bostock
 * @author Isaac Shum
 * @author Louis Wasserman
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible(emulated = true)
@SuppressWarnings("rawtypes")
public final class Maps {
	private Maps() {
	}

	/**
	 * Creates a <i>mutable</i>, empty {@code HashMap} instance.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, use {@link ImmutableMap#of()}
	 * instead.
	 * 
	 * <p>
	 * <b>Note:</b> if {@code K} is an {@code enum} type, use
	 * {@link #newEnumMap} instead.
	 * 
	 * @return a new, empty {@code HashMap}
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * Creates a {@code HashMap} instance with enough capacity to hold the
	 * specified number of elements without rehashing.
	 * 
	 * @param expectedSize
	 *            the expected size
	 * @return a new, empty {@code HashMap} with enough capacity to hold
	 *         {@code expectedSize} elements without rehashing
	 * @throws IllegalArgumentException
	 *             if {@code expectedSize} is negative
	 */
	public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(
			int expectedSize) {
		/*
		 * The HashMap is constructed with an initialCapacity that's greater
		 * than expectedSize. The larger value is necessary because HashMap
		 * resizes its internal array if the map size exceeds loadFactor *
		 * initialCapacity.
		 */
		return new HashMap<K, V>(capacity(expectedSize));
	}

	/**
	 * Returns an appropriate value for the "capacity" (in reality, "minimum
	 * table size") parameter of a {@link HashMap} constructor, such that the
	 * resulting table will be between 25% and 50% full when it contains
	 * {@code expectedSize} entries, unless {@code expectedSize} is greater than
	 * {@link Integer#MAX_VALUE} / 2.
	 * 
	 * @throws IllegalArgumentException
	 *             if {@code expectedSize} is negative
	 */
	static int capacity(int expectedSize) {
		Preconditions.checkArgument(expectedSize >= 0);
		// Avoid the int overflow if expectedSize > (Integer.MAX_VALUE / 2)
		return Ints.saturatedCast(Math.max(expectedSize * 2L, 16L));
	}

	/**
	 * Creates a <i>mutable</i> {@code HashMap} instance with the same mappings
	 * as the specified map.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, use
	 * {@link ImmutableMap#copyOf(Map)} instead.
	 * 
	 * <p>
	 * <b>Note:</b> if {@code K} is an {@link Enum} type, use
	 * {@link #newEnumMap} instead.
	 * 
	 * @param map
	 *            the mappings to be placed in the new map
	 * @return a new {@code HashMap} initialized with the mappings from
	 *         {@code map}
	 */
	public static <K, V> HashMap<K, V> newHashMap(
			Map<? extends K, ? extends V> map) {
		return new HashMap<K, V>(map);
	}

	/**
	 * Creates a <i>mutable</i>, empty, insertion-ordered {@code LinkedHashMap}
	 * instance.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, use {@link ImmutableMap#of()}
	 * instead.
	 * 
	 * @return a new, empty {@code LinkedHashMap}
	 */
	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}

	/**
	 * Creates a <i>mutable</i>, insertion-ordered {@code LinkedHashMap}
	 * instance with the same mappings as the specified map.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, use
	 * {@link ImmutableMap#copyOf(Map)} instead.
	 * 
	 * @param map
	 *            the mappings to be placed in the new map
	 * @return a new, {@code LinkedHashMap} initialized with the mappings from
	 *         {@code map}
	 */
	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
			Map<? extends K, ? extends V> map) {
		return new LinkedHashMap<K, V>(map);
	}

	/**
	 * Returns a general-purpose instance of {@code ConcurrentMap}, which
	 * supports all optional operations of the ConcurrentMap interface. It does
	 * not permit null keys or values. It is serializable.
	 * 
	 * <p>
	 * This is currently accomplished by calling {@link MapMaker#makeMap()}.
	 * 
	 * <p>
	 * It is preferable to use {@code MapMaker} directly (rather than through
	 * this method), as it presents numerous useful configuration options, such
	 * as the concurrency level, load factor, key/value reference types, and
	 * value computation.
	 * 
	 * @return a new, empty {@code ConcurrentMap}
	 * @since 3
	 */
	public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
		return new MapMaker().<K, V> makeMap();
	}

	/**
	 * Creates a <i>mutable</i>, empty {@code TreeMap} instance using the
	 * natural ordering of its elements.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, use
	 * {@link ImmutableSortedMap#of()} instead.
	 * 
	 * @return a new, empty {@code TreeMap}
	 */
	// eclipse doesn't like the raw Comparable

	public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
		return new TreeMap<K, V>();
	}

	/**
	 * Creates a <i>mutable</i> {@code TreeMap} instance with the same mappings
	 * as the specified map and using the same ordering as the specified map.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, use
	 * {@link ImmutableSortedMap#copyOfSorted(SortedMap)} instead.
	 * 
	 * @param map
	 *            the sorted map whose mappings are to be placed in the new map
	 *            and whose comparator is to be used to sort the new map
	 * @return a new {@code TreeMap} initialized with the mappings from
	 *         {@code map} and using the comparator of {@code map}
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
		return new TreeMap<K, V>(map);
	}

	/**
	 * Creates a <i>mutable</i>, empty {@code TreeMap} instance using the given
	 * comparator.
	 * 
	 * <p>
	 * <b>Note:</b> if mutability is not required, use
	 * {@code ImmutableSortedMap.orderedBy(comparator).build()} instead.
	 * 
	 * @param comparator
	 *            the comparator to sort the keys with
	 * @return a new, empty {@code TreeMap}
	 */
	public static <C, K extends C, V> TreeMap<K, V> newTreeMap(
			Comparator<C> comparator) {
		// Ideally, the extra type parameter "C" shouldn't be necessary. It is a
		// work-around of a compiler type inference quirk that prevents the
		// following code from being compiled:
		// Comparator<Class<?>> comparator = null;
		// Map<Class<? extends Throwable>, String> map = newTreeMap(comparator);
		return new TreeMap<K, V>(comparator);
	}

	/**
	 * Creates an {@code EnumMap} instance.
	 * 
	 * @param type
	 *            the key type for this map
	 * @return a new, empty {@code EnumMap}
	 */
	public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
		return new EnumMap<K, V>(Preconditions.checkNotNull(type));
	}

	/**
	 * Creates an {@code EnumMap} with the same mappings as the specified map.
	 * 
	 * @param map
	 *            the map from which to initialize this {@code EnumMap}
	 * @return a new {@code EnumMap} initialized with the mappings from
	 *         {@code map}
	 * @throws IllegalArgumentException
	 *             if {@code m} is not an {@code EnumMap} instance and contains
	 *             no mappings
	 */
	public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(
			Map<K, ? extends V> map) {
		return new EnumMap<K, V>(map);
	}

	/**
	 * Creates an {@code IdentityHashMap} instance.
	 * 
	 * @return a new, empty {@code IdentityHashMap}
	 */
	public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
		return new IdentityHashMap<K, V>();
	}

	/**
	 * Returns a synchronized (thread-safe) bimap backed by the specified bimap.
	 * In order to guarantee serial access, it is critical that <b>all</b>
	 * access to the backing bimap is accomplished through the returned bimap.
	 * 
	 * <p>
	 * It is imperative that the user manually synchronize on the returned map
	 * when accessing any of its collection views:
	 * 
	 * <pre>
	 * {@code
	 * 
	 *   BiMap<Long, String> map = Maps.synchronizedBiMap(
	 *       HashBiMap.<Long, String>create());
	 *   ...
	 *   Set<Long> set = map.keySet();  // Needn't be in synchronized block
	 *   ...
	 *   synchronized (map) {  // Synchronizing on map, not set!
	 *     Iterator<Long> it = set.iterator(); // Must be in synchronized block
	 *     while (it.hasNext()) {
	 *       foo(it.next());
	 *     }
	 *   }}
	 * </pre>
	 * 
	 * Failure to follow this advice may result in non-deterministic behavior.
	 * 
	 * <p>
	 * The returned bimap will be serializable if the specified bimap is
	 * serializable.
	 * 
	 * @param bimap
	 *            the bimap to be wrapped in a synchronized view
	 * @return a sychronized view of the specified bimap
	 */
	public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
		return Synchronized.biMap(bimap, null);
	}

	/**
	 * Computes the difference between two maps. This difference is an immutable
	 * snapshot of the state of the maps at the time this method is called. It
	 * will never change, even if the maps change at a later time.
	 * 
	 * <p>
	 * Since this method uses {@code HashMap} instances internally, the keys of
	 * the supplied maps must be well-behaved with respect to
	 * {@link Object#equals} and {@link Object#hashCode}.
	 * 
	 * <p>
	 * <b>Note:</b>If you only need to know whether two maps have the same
	 * mappings, call {@code left.equals(right)} instead of this method.
	 * 
	 * @param left
	 *            the map to treat as the "left" map for purposes of comparison
	 * @param right
	 *            the map to treat as the "right" map for purposes of comparison
	 * @return the difference between the two maps
	 */
	public static <K, V> MapDifference<K, V> difference(
			Map<? extends K, ? extends V> left,
			Map<? extends K, ? extends V> right) {
		Map<K, V> onlyOnLeft = newHashMap();
		Map<K, V> onlyOnRight = new HashMap<K, V>(right); // will whittle it
															// down
		Map<K, V> onBoth = newHashMap();
		Map<K, MapDifference.ValueDifference<V>> differences = newHashMap();
		boolean eq = true;

		for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
			K leftKey = entry.getKey();
			V leftValue = entry.getValue();
			if (right.containsKey(leftKey)) {
				V rightValue = onlyOnRight.remove(leftKey);
				if (Objects.equal(leftValue, rightValue)) {
					onBoth.put(leftKey, leftValue);
				} else {
					eq = false;
					differences.put(leftKey, new ValueDifferenceImpl<V>(
							leftValue, rightValue));
				}
			} else {
				eq = false;
				onlyOnLeft.put(leftKey, leftValue);
			}
		}

		boolean areEqual = eq && onlyOnRight.isEmpty();
		return mapDifference(areEqual, onlyOnLeft, onlyOnRight, onBoth,
				differences);
	}

	private static <K, V> MapDifference<K, V> mapDifference(boolean areEqual,
			Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth,
			Map<K, ValueDifference<V>> differences) {
		return new MapDifferenceImpl<K, V>(areEqual,
				Collections.unmodifiableMap(onlyOnLeft),
				Collections.unmodifiableMap(onlyOnRight),
				Collections.unmodifiableMap(onBoth),
				Collections.unmodifiableMap(differences));
	}

	static class MapDifferenceImpl<K, V> implements MapDifference<K, V> {
		final boolean areEqual;
		final Map<K, V> onlyOnLeft;
		final Map<K, V> onlyOnRight;
		final Map<K, V> onBoth;
		final Map<K, ValueDifference<V>> differences;

		MapDifferenceImpl(boolean areEqual, Map<K, V> onlyOnLeft,
				Map<K, V> onlyOnRight, Map<K, V> onBoth,
				Map<K, ValueDifference<V>> differences) {
			this.areEqual = areEqual;
			this.onlyOnLeft = onlyOnLeft;
			this.onlyOnRight = onlyOnRight;
			this.onBoth = onBoth;
			this.differences = differences;
		}

		public boolean areEqual() {
			return areEqual;
		}

		public Map<K, V> entriesOnlyOnLeft() {
			return onlyOnLeft;
		}

		public Map<K, V> entriesOnlyOnRight() {
			return onlyOnRight;
		}

		public Map<K, V> entriesInCommon() {
			return onBoth;
		}

		public Map<K, ValueDifference<V>> entriesDiffering() {
			return differences;
		}

		@Override
		public boolean equals(Object object) {
			if (object == this) {
				return true;
			}
			if (object instanceof MapDifference) {
				MapDifference<?, ?> other = (MapDifference<?, ?>) object;
				return entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft())
						&& entriesOnlyOnRight().equals(
								other.entriesOnlyOnRight())
						&& entriesInCommon().equals(other.entriesInCommon())
						&& entriesDiffering().equals(other.entriesDiffering());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(entriesOnlyOnLeft(), entriesOnlyOnRight(),
					entriesInCommon(), entriesDiffering());
		}

		@Override
		public String toString() {
			if (areEqual) {
				return "equal";
			}

			StringBuilder result = new StringBuilder("not equal");
			if (!onlyOnLeft.isEmpty()) {
				result.append(": only on left=").append(onlyOnLeft);
			}
			if (!onlyOnRight.isEmpty()) {
				result.append(": only on right=").append(onlyOnRight);
			}
			if (!differences.isEmpty()) {
				result.append(": value differences=").append(differences);
			}
			return result.toString();
		}
	}

	static class ValueDifferenceImpl<V> implements
			MapDifference.ValueDifference<V> {
		private final V left;
		private final V right;

		ValueDifferenceImpl(V left, V right) {
			this.left = left;
			this.right = right;
		}

		public V leftValue() {
			return left;
		}

		public V rightValue() {
			return right;
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof MapDifference.ValueDifference<?>) {
				MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference<?>) object;
				return Objects.equal(this.left, that.leftValue())
						&& Objects.equal(this.right, that.rightValue());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(left, right);
		}

		@Override
		public String toString() {
			return "(" + left + ", " + right + ")";
		}
	}

	/**
	 * Returns an immutable map for which the {@link Map#values} are the given
	 * elements in the given order, and each key is the product of invoking a
	 * supplied function on its corresponding value.
	 * 
	 * @param values
	 *            the values to use when constructing the {@code Map}
	 * @param keyFunction
	 *            the function used to produce the key for each value
	 * @return a map mapping the result of evaluating the function
	 *         {@code keyFunction} on each value in the input collection to that
	 *         value
	 * @throws IllegalArgumentException
	 *             if {@code keyFunction} produces the same key for more than
	 *             one value in the input collection
	 * @throws NullPointerException
	 *             if any elements of {@code values} is null, or if
	 *             {@code keyFunction} produces {@code null} for any value
	 */
	public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values,
			Function<? super V, K> keyFunction) {
		Preconditions.checkNotNull(keyFunction);
		ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
		for (V value : values) {
			builder.put(keyFunction.apply(value), value);
		}
		return builder.build();
	}

	/**
	 * Creates an {@code ImmutableMap<String, String>} from a {@code Properties}
	 * instance. Properties normally derive from {@code Map<Object, Object>},
	 * but they typically contain strings, which is awkward. This method lets
	 * you get a plain-old-{@code Map} out of a {@code Properties}.
	 * 
	 * @param properties
	 *            a {@code Properties} object to be converted
	 * @return an immutable map containing all the entries in {@code properties}
	 * @throws ClassCastException
	 *             if any key in {@code Properties} is not a {@code String}
	 * @throws NullPointerException
	 *             if any key or value in {@code Properties} is null
	 */
	@GwtIncompatible("java.util.Properties")
	public static ImmutableMap<String, String> fromProperties(
			Properties properties) {
		ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

		for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			builder.put(key, properties.getProperty(key));
		}

		return builder.build();
	}

	/**
	 * Returns an immutable map entry with the specified key and value. The
	 * {@link Entry#setValue} operation throws an
	 * {@link UnsupportedOperationException}.
	 * 
	 * <p>
	 * The returned entry is serializable.
	 * 
	 * @param key
	 *            the key to be associated with the returned entry
	 * @param value
	 *            the value to be associated with the returned entry
	 */
	@GwtCompatible(serializable = true)
	public static <K, V> Entry<K, V> immutableEntry(K key, V value) {
		return new ImmutableEntry<K, V>(key, value);
	}

	/**
	 * Returns an unmodifiable view of the specified set of entries. The
	 * {@link Entry#setValue} operation throws an
	 * {@link UnsupportedOperationException}, as do any operations that would
	 * modify the returned set.
	 * 
	 * @param entrySet
	 *            the entries for which to return an unmodifiable view
	 * @return an unmodifiable view of the entries
	 */
	static <K, V> Set<Entry<K, V>> unmodifiableEntrySet(
			Set<Entry<K, V>> entrySet) {
		return new UnmodifiableEntrySet<K, V>(
				Collections.unmodifiableSet(entrySet));
	}

	/**
	 * Returns an unmodifiable view of the specified map entry. The
	 * {@link Entry#setValue} operation throws an
	 * {@link UnsupportedOperationException}. This also has the side-effect of
	 * redefining {@code equals} to comply with the Entry contract, to avoid a
	 * possible nefarious implementation of equals.
	 * 
	 * @param entry
	 *            the entry for which to return an unmodifiable view
	 * @return an unmodifiable view of the entry
	 */
	static <K, V> Entry<K, V> unmodifiableEntry(final Entry<K, V> entry) {
		Preconditions.checkNotNull(entry);
		return new AbstractMapEntry<K, V>() {
			@Override
			public K getKey() {
				return entry.getKey();
			}

			@Override
			public V getValue() {
				return entry.getValue();
			}
		};
	}

	/** @see Multimaps#unmodifiableEntries */
	static class UnmodifiableEntries<K, V> extends
			ForwardingCollection<Entry<K, V>> {
		private final Collection<Entry<K, V>> entries;

		UnmodifiableEntries(Collection<Entry<K, V>> entries) {
			this.entries = entries;
		}

		@Override
		protected Collection<Entry<K, V>> delegate() {
			return entries;
		}

		@Override
		public Iterator<Entry<K, V>> iterator() {
			final Iterator<Entry<K, V>> delegate = super.iterator();
			return new ForwardingIterator<Entry<K, V>>() {
				@Override
				public Entry<K, V> next() {
					return unmodifiableEntry(super.next());
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

				@Override
				protected Iterator<Entry<K, V>> delegate() {
					return delegate;
				}
			};
		}

		// See java.util.Collections.UnmodifiableEntrySet for details on
		// attacks.

		@Override
		public boolean add(Entry<K, V> element) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(Collection<? extends Entry<K, V>> collection) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(Object object) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(Collection<?> collection) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(Collection<?> collection) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object[] toArray() {
			return standardToArray();
		}

		@Override
		public <T> T[] toArray(T[] array) {
			return standardToArray(array);
		}
	}

	/** @see Maps#unmodifiableEntrySet(Set) */
	static class UnmodifiableEntrySet<K, V> extends UnmodifiableEntries<K, V>
			implements Set<Entry<K, V>> {
		UnmodifiableEntrySet(Set<Entry<K, V>> entries) {
			super(entries);
		}

		// See java.util.Collections.UnmodifiableEntrySet for details on
		// attacks.

		@Override
		public boolean equals(Object object) {
			return Sets.equalsImpl(this, object);
		}

		@Override
		public int hashCode() {
			return Sets.hashCodeImpl(this);
		}
	}

	/**
	 * Returns an unmodifiable view of the specified bimap. This method allows
	 * modules to provide users with "read-only" access to internal bimaps.
	 * Query operations on the returned bimap "read through" to the specified
	 * bimap, and attemps to modify the returned map, whether direct or via its
	 * collection views, result in an {@code UnsupportedOperationException}.
	 * 
	 * <p>
	 * The returned bimap will be serializable if the specified bimap is
	 * serializable.
	 * 
	 * @param bimap
	 *            the bimap for which an unmodifiable view is to be returned
	 * @return an unmodifiable view of the specified bimap
	 */
	public static <K, V> BiMap<K, V> unmodifiableBiMap(
			BiMap<? extends K, ? extends V> bimap) {
		return new UnmodifiableBiMap<K, V>(bimap, null);
	}

	/** @see Maps#unmodifiableBiMap(BiMap) */
	private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V>
			implements BiMap<K, V>, Serializable {
		final Map<K, V> unmodifiableMap;
		final BiMap<? extends K, ? extends V> delegate;
		transient BiMap<V, K> inverse;
		transient Set<V> values;

		UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate,
				BiMap<V, K> inverse) {
			unmodifiableMap = Collections.unmodifiableMap(delegate);
			this.delegate = delegate;
			this.inverse = inverse;
		}

		@Override
		protected Map<K, V> delegate() {
			return unmodifiableMap;
		}

		public V forcePut(K key, V value) {
			throw new UnsupportedOperationException();
		}

		public BiMap<V, K> inverse() {
			BiMap<V, K> result = inverse;
			return (result == null) ? inverse = new UnmodifiableBiMap<V, K>(
					delegate.inverse(), this) : result;
		}

		@Override
		public Set<V> values() {
			Set<V> result = values;
			return (result == null) ? values = Collections
					.unmodifiableSet(delegate.values()) : result;
		}

		private static final long serialVersionUID = 0;
	}

	/**
	 * Returns a view of a map where each value is transformed by a function.
	 * All other properties of the map, such as iteration order, are left
	 * intact. For example, the code:
	 * 
	 * <pre>
	 * {
	 * 	&#064;code
	 * 	Map&lt;String, Integer&gt; map = ImmutableMap.of(&quot;a&quot;, 4, &quot;b&quot;, 9);
	 * 	Function&lt;Integer, Double&gt; sqrt = new Function&lt;Integer, Double&gt;() {
	 * 		public Double apply(Integer in) {
	 * 			return Math.sqrt((int) in);
	 * 		}
	 * 	};
	 * 	Map&lt;String, Double&gt; transformed = Maps.transformValues(map, sqrt);
	 * 	System.out.println(transformed);
	 * }
	 * </pre>
	 * 
	 * ... prints {@code a=2.0, b=3.0}}.
	 * 
	 * <p>
	 * Changes in the underlying map are reflected in this view. Conversely,
	 * this view supports removal operations, and these are reflected in the
	 * underlying map.
	 * 
	 * <p>
	 * It's acceptable for the underlying map to contain null keys, and even
	 * null values provided that the function is capable of accepting null
	 * input. The transformed map might contain null values, if the function
	 * sometimes gives a null result.
	 * 
	 * <p>
	 * The returned map is not thread-safe or serializable, even if the
	 * underlying map is.
	 * 
	 * <p>
	 * The function is applied lazily, invoked when needed. This is necessary
	 * for the returned map to be a view, but it means that the function will be
	 * applied many times for bulk operations like {@link Map#containsValue} and
	 * {@code Map.toString()}. For this to perform well, {@code function} should
	 * be fast. To avoid lazy evaluation when the returned map doesn't need to
	 * be a view, copy the returned map into a new map of your choosing.
	 */
	public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap,
			final Function<? super V1, V2> function) {
		Preconditions.checkNotNull(function);
		EntryTransformer<K, V1, V2> transformer = new EntryTransformer<K, V1, V2>() {
			public V2 transformEntry(K key, V1 value) {
				return function.apply(value);
			}
		};
		return transformEntries(fromMap, transformer);
	}

	/**
	 * Returns a view of a map whose values are derived from the original map's
	 * entries. In contrast to {@link #transformValues}, this method's
	 * entry-transformation logic may depend on the key as well as the value.
	 * 
	 * <p>
	 * All other properties of the transformed map, such as iteration order, are
	 * left intact. For example, the code:
	 * 
	 * <pre>
	 * {
	 * 	&#064;code
	 * 	Map&lt;String, Boolean&gt; options = ImmutableMap.of(&quot;verbose&quot;, true, &quot;sort&quot;,
	 * 			false);
	 * 	EntryTransformer&lt;String, Boolean, String&gt; flagPrefixer = new EntryTransformer&lt;String, Boolean, String&gt;() {
	 * 		public String transformEntry(String key, Boolean value) {
	 * 			return value ? key : &quot;no&quot; + key;
	 * 		}
	 * 	};
	 * 	Map&lt;String, String&gt; transformed = Maps.transformEntries(options,
	 * 			flagPrefixer);
	 * 	System.out.println(transformed);
	 * }
	 * </pre>
	 * 
	 * ... prints {@code verbose=verbose, sort=nosort}}.
	 * 
	 * <p>
	 * Changes in the underlying map are reflected in this view. Conversely,
	 * this view supports removal operations, and these are reflected in the
	 * underlying map.
	 * 
	 * <p>
	 * It's acceptable for the underlying map to contain null keys and null
	 * values provided that the transformer is capable of accepting null inputs.
	 * The transformed map might contain null values if the transformer
	 * sometimes gives a null result.
	 * 
	 * <p>
	 * The returned map is not thread-safe or serializable, even if the
	 * underlying map is.
	 * 
	 * <p>
	 * The transformer is applied lazily, invoked when needed. This is necessary
	 * for the returned map to be a view, but it means that the transformer will
	 * be applied many times for bulk operations like {@link Map#containsValue}
	 * and {@link Object#toString}. For this to perform well,
	 * {@code transformer} should be fast. To avoid lazy evaluation when the
	 * returned map doesn't need to be a view, copy the returned map into a new
	 * map of your choosing.
	 * 
	 * <p>
	 * <b>Warning:</b> This method assumes that for any instance {@code k} of
	 * {@code EntryTransformer} key type {@code K}, {@code k.equals(k2)} implies
	 * that {@code k2} is also of type {@code K}. Using an
	 * {@code EntryTransformer} key type for which this may not hold, such as
	 * {@code ArrayList}, may risk a {@code ClassCastException} when calling
	 * methods on the transformed map.
	 * 
	 * @since 7
	 */
	@Beta
	public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap,
			EntryTransformer<? super K, ? super V1, V2> transformer) {
		return new TransformedEntriesMap<K, V1, V2>(fromMap, transformer);
	}

	/**
	 * A transformation of the value of a key-value pair, using both key and
	 * value as inputs. To apply the transformation to a map, use
	 * {@link Maps#transformEntries(Map, EntryTransformer)}.
	 * 
	 * @param <K>
	 *            the key type of the input and output entries
	 * @param <V1>
	 *            the value type of the input entry
	 * @param <V2>
	 *            the value type of the output entry
	 * @since 7
	 */
	@Beta
	public interface EntryTransformer<K, V1, V2> {
		/**
		 * Determines an output value based on a key-value pair. This method is
		 * <i>generally expected</i>, but not absolutely required, to have the
		 * following properties:
		 * 
		 * <ul>
		 * <li>Its execution does not cause any observable side effects.
		 * <li>The computation is <i>consistent with equals</i>; that is,
		 * {@link Objects#equal Objects.equal}{@code (k1, k2) &&}
		 * {@link Objects#equal}{@code (v1, v2)} implies that
		 * {@code Objects.equal(transformer.transform(k1, v1),
		 * transformer.transform(k2, v2))}.
		 * </ul>
		 * 
		 * @throws NullPointerException
		 *             if the key or value is null and this transformer does not
		 *             accept null arguments
		 */
		V2 transformEntry(K key, V1 value);
	}

	static class TransformedEntriesMap<K, V1, V2> extends AbstractMap<K, V2> {
		final Map<K, V1> fromMap;
		final EntryTransformer<? super K, ? super V1, V2> transformer;

		TransformedEntriesMap(Map<K, V1> fromMap,
				EntryTransformer<? super K, ? super V1, V2> transformer) {
			this.fromMap = Preconditions.checkNotNull(fromMap);
			this.transformer = Preconditions.checkNotNull(transformer);
		}

		@Override
		public int size() {
			return fromMap.size();
		}

		@Override
		public boolean containsKey(Object key) {
			return fromMap.containsKey(key);
		}

		// safe as long as the user followed the <b>Warning</b> in the javadoc
		@SuppressWarnings("unchecked")
		@Override
		public V2 get(Object key) {
			V1 value = fromMap.get(key);
			return (value != null || fromMap.containsKey(key)) ? transformer
					.transformEntry((K) key, value) : null;
		}

		// safe as long as the user followed the <b>Warning</b> in the javadoc
		@SuppressWarnings("unchecked")
		@Override
		public V2 remove(Object key) {
			return fromMap.containsKey(key) ? transformer.transformEntry(
					(K) key, fromMap.remove(key)) : null;
		}

		@Override
		public void clear() {
			fromMap.clear();
		}

		EntrySet entrySet;

		@Override
		public Set<Entry<K, V2>> entrySet() {
			EntrySet result = entrySet;
			if (result == null) {
				entrySet = result = new EntrySet();
			}
			return result;
		}

		class EntrySet extends AbstractSet<Entry<K, V2>> {
			@Override
			public int size() {
				return TransformedEntriesMap.this.size();
			}

			@Override
			public Iterator<Entry<K, V2>> iterator() {
				final Iterator<Entry<K, V1>> mapIterator = fromMap.entrySet()
						.iterator();

				return new Iterator<Entry<K, V2>>() {
					public boolean hasNext() {
						return mapIterator.hasNext();
					}

					public Entry<K, V2> next() {
						final Entry<K, V1> entry = mapIterator.next();
						return new AbstractMapEntry<K, V2>() {
							@Override
							public K getKey() {
								return entry.getKey();
							}

							@Override
							public V2 getValue() {
								return transformer.transformEntry(
										entry.getKey(), entry.getValue());
							}
						};
					}

					public void remove() {
						mapIterator.remove();
					}
				};
			}

			@Override
			public void clear() {
				fromMap.clear();
			}

			@Override
			public boolean contains(Object o) {
				if (!(o instanceof Entry)) {
					return false;
				}
				Entry<?, ?> entry = (Entry<?, ?>) o;
				Object entryKey = entry.getKey();
				Object entryValue = entry.getValue();
				V2 mapValue = TransformedEntriesMap.this.get(entryKey);
				if (mapValue != null) {
					return mapValue.equals(entryValue);
				}
				return entryValue == null && containsKey(entryKey);
			}

			@Override
			public boolean remove(Object o) {
				if (contains(o)) {
					Entry<?, ?> entry = (Entry<?, ?>) o;
					Object key = entry.getKey();
					fromMap.remove(key);
					return true;
				}
				return false;
			}
		}
	}

	/**
	 * Returns a map containing the mappings in {@code unfiltered} whose keys
	 * satisfy a predicate. The returned map is a live view of
	 * {@code unfiltered}; changes to one affect the other.
	 * 
	 * <p>
	 * The resulting map's {@code keySet()}, {@code entrySet()}, and
	 * {@code values()} views have iterators that don't support {@code remove()}
	 * , but all other methods are supported by the map and its views. When
	 * given a key that doesn't satisfy the predicate, the map's {@code put()}
	 * and {@code putAll()} methods throw an {@link IllegalArgumentException}.
	 * 
	 * <p>
	 * When methods such as {@code removeAll()} and {@code clear()} are called
	 * on the filtered map or its views, only mappings whose keys satisfy the
	 * filter will be removed from the underlying map.
	 * 
	 * <p>
	 * The returned map isn't threadsafe or serializable, even if
	 * {@code unfiltered} is.
	 * 
	 * <p>
	 * Many of the filtered map's methods, such as {@code size()}, iterate
	 * across every key/value mapping in the underlying map and determine which
	 * satisfy the filter. When a live view is <i>not</i> needed, it may be
	 * faster to copy the filtered map and use the copy.
	 * 
	 * <p>
	 * <b>Warning:</b> {@code keyPredicate} must be <i>consistent with
	 * equals</i>, as documented at {@link Predicate#apply}. Do not provide a
	 * predicate such as {@code Predicates.instanceOf(ArrayList.class)}, which
	 * is inconsistent with equals.
	 */
	public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered,
			final Predicate<? super K> keyPredicate) {
		Preconditions.checkNotNull(keyPredicate);
		Predicate<Entry<K, V>> entryPredicate = new Predicate<Entry<K, V>>() {
			public boolean apply(Entry<K, V> input) {
				return keyPredicate.apply(input.getKey());
			}
		};
		return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered(
				(AbstractFilteredMap<K, V>) unfiltered, entryPredicate)
				: new FilteredKeyMap<K, V>(
						Preconditions.checkNotNull(unfiltered), keyPredicate,
						entryPredicate);
	}

	/**
	 * Returns a map containing the mappings in {@code unfiltered} whose values
	 * satisfy a predicate. The returned map is a live view of
	 * {@code unfiltered}; changes to one affect the other.
	 * 
	 * <p>
	 * The resulting map's {@code keySet()}, {@code entrySet()}, and
	 * {@code values()} views have iterators that don't support {@code remove()}
	 * , but all other methods are supported by the map and its views. When
	 * given a value that doesn't satisfy the predicate, the map's {@code put()}, {@code putAll()}, and {@link Entry#setValue} methods throw an
	 * {@link IllegalArgumentException}.
	 * 
	 * <p>
	 * When methods such as {@code removeAll()} and {@code clear()} are called
	 * on the filtered map or its views, only mappings whose values satisfy the
	 * filter will be removed from the underlying map.
	 * 
	 * <p>
	 * The returned map isn't threadsafe or serializable, even if
	 * {@code unfiltered} is.
	 * 
	 * <p>
	 * Many of the filtered map's methods, such as {@code size()}, iterate
	 * across every key/value mapping in the underlying map and determine which
	 * satisfy the filter. When a live view is <i>not</i> needed, it may be
	 * faster to copy the filtered map and use the copy.
	 * 
	 * <p>
	 * <b>Warning:</b> {@code valuePredicate} must be <i>consistent with
	 * equals</i>, as documented at {@link Predicate#apply}. Do not provide a
	 * predicate such as {@code Predicates.instanceOf(ArrayList.class)}, which
	 * is inconsistent with equals.
	 */
	public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered,
			final Predicate<? super V> valuePredicate) {
		Preconditions.checkNotNull(valuePredicate);
		Predicate<Entry<K, V>> entryPredicate = new Predicate<Entry<K, V>>() {
			public boolean apply(Entry<K, V> input) {
				return valuePredicate.apply(input.getValue());
			}
		};
		return filterEntries(unfiltered, entryPredicate);
	}

	/**
	 * Returns a map containing the mappings in {@code unfiltered} that satisfy
	 * a predicate. The returned map is a live view of {@code unfiltered};
	 * changes to one affect the other.
	 * 
	 * <p>
	 * The resulting map's {@code keySet()}, {@code entrySet()}, and
	 * {@code values()} views have iterators that don't support {@code remove()}
	 * , but all other methods are supported by the map and its views. When
	 * given a key/value pair that doesn't satisfy the predicate, the map's
	 * {@code put()} and {@code putAll()} methods throw an
	 * {@link IllegalArgumentException}. Similarly, the map's entries have a
	 * {@link Entry#setValue} method that throws an
	 * {@link IllegalArgumentException} when the existing key and the provided
	 * value don't satisfy the predicate.
	 * 
	 * <p>
	 * When methods such as {@code removeAll()} and {@code clear()} are called
	 * on the filtered map or its views, only mappings that satisfy the filter
	 * will be removed from the underlying map.
	 * 
	 * <p>
	 * The returned map isn't threadsafe or serializable, even if
	 * {@code unfiltered} is.
	 * 
	 * <p>
	 * Many of the filtered map's methods, such as {@code size()}, iterate
	 * across every key/value mapping in the underlying map and determine which
	 * satisfy the filter. When a live view is <i>not</i> needed, it may be
	 * faster to copy the filtered map and use the copy.
	 * 
	 * <p>
	 * <b>Warning:</b> {@code entryPredicate} must be <i>consistent with
	 * equals</i>, as documented at {@link Predicate#apply}.
	 */
	public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered,
			Predicate<? super Entry<K, V>> entryPredicate) {
		Preconditions.checkNotNull(entryPredicate);
		return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered(
				(AbstractFilteredMap<K, V>) unfiltered, entryPredicate)
				: new FilteredEntryMap<K, V>(
						Preconditions.checkNotNull(unfiltered), entryPredicate);
	}

	/**
	 * Support {@code clear()}, {@code removeAll()}, and {@code retainAll()}
	 * when filtering a filtered map.
	 */
	private static <K, V> Map<K, V> filterFiltered(
			AbstractFilteredMap<K, V> map,
			Predicate<? super Entry<K, V>> entryPredicate) {
		Predicate<Entry<K, V>> predicate = Predicates.and(map.predicate,
				entryPredicate);
		return new FilteredEntryMap<K, V>(map.unfiltered, predicate);
	}

	private abstract static class AbstractFilteredMap<K, V> extends
			AbstractMap<K, V> {
		final Map<K, V> unfiltered;
		final Predicate<? super Entry<K, V>> predicate;

		AbstractFilteredMap(Map<K, V> unfiltered,
				Predicate<? super Entry<K, V>> predicate) {
			this.unfiltered = unfiltered;
			this.predicate = predicate;
		}

		boolean apply(Object key, V value) {
			// This method is called only when the key is in the map, implying
			// that
			// key is a K.
			@SuppressWarnings("unchecked")
			K k = (K) key;
			return predicate.apply(Maps.immutableEntry(k, value));
		}

		@Override
		public V put(K key, V value) {
			Preconditions.checkArgument(apply(key, value));
			return unfiltered.put(key, value);
		}

		@Override
		public void putAll(Map<? extends K, ? extends V> map) {
			for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
				Preconditions.checkArgument(apply(entry.getKey(),
						entry.getValue()));
			}
			unfiltered.putAll(map);
		}

		@Override
		public boolean containsKey(Object key) {
			return unfiltered.containsKey(key)
					&& apply(key, unfiltered.get(key));
		}

		@Override
		public V get(Object key) {
			V value = unfiltered.get(key);
			return ((value != null) && apply(key, value)) ? value : null;
		}

		@Override
		public boolean isEmpty() {
			return entrySet().isEmpty();
		}

		@Override
		public V remove(Object key) {
			return containsKey(key) ? unfiltered.remove(key) : null;
		}

		Collection<V> values;

		@Override
		public Collection<V> values() {
			Collection<V> result = values;
			return (result == null) ? values = new Values() : result;
		}

		class Values extends AbstractCollection<V> {
			@Override
			public Iterator<V> iterator() {
				final Iterator<Entry<K, V>> entryIterator = entrySet()
						.iterator();
				return new UnmodifiableIterator<V>() {
					public boolean hasNext() {
						return entryIterator.hasNext();
					}

					public V next() {
						return entryIterator.next().getValue();
					}
				};
			}

			@Override
			public int size() {
				return entrySet().size();
			}

			@Override
			public void clear() {
				entrySet().clear();
			}

			@Override
			public boolean isEmpty() {
				return entrySet().isEmpty();
			}

			@Override
			public boolean remove(Object o) {
				Iterator<Entry<K, V>> iterator = unfiltered.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<K, V> entry = iterator.next();
					if (Objects.equal(o, entry.getValue())
							&& predicate.apply(entry)) {
						iterator.remove();
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean removeAll(Collection<?> collection) {
				Preconditions.checkNotNull(collection);
				boolean changed = false;
				Iterator<Entry<K, V>> iterator = unfiltered.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<K, V> entry = iterator.next();
					if (collection.contains(entry.getValue())
							&& predicate.apply(entry)) {
						iterator.remove();
						changed = true;
					}
				}
				return changed;
			}

			@Override
			public boolean retainAll(Collection<?> collection) {
				Preconditions.checkNotNull(collection);
				boolean changed = false;
				Iterator<Entry<K, V>> iterator = unfiltered.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<K, V> entry = iterator.next();
					if (!collection.contains(entry.getValue())
							&& predicate.apply(entry)) {
						iterator.remove();
						changed = true;
					}
				}
				return changed;
			}

			@Override
			public Object[] toArray() {
				// creating an ArrayList so filtering happens once
				return Lists.newArrayList(iterator()).toArray();
			}

			@Override
			public <T> T[] toArray(T[] array) {
				return Lists.newArrayList(iterator()).toArray(array);
			}
		}
	}

	private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
		Predicate<? super K> keyPredicate;

		FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate,
				Predicate<Entry<K, V>> entryPredicate) {
			super(unfiltered, entryPredicate);
			this.keyPredicate = keyPredicate;
		}

		Set<Entry<K, V>> entrySet;

		@Override
		public Set<Entry<K, V>> entrySet() {
			Set<Entry<K, V>> result = entrySet;
			return (result == null) ? entrySet = Sets.filter(
					unfiltered.entrySet(), predicate) : result;
		}

		Set<K> keySet;

		@Override
		public Set<K> keySet() {
			Set<K> result = keySet;
			return (result == null) ? keySet = Sets.filter(unfiltered.keySet(),
					keyPredicate) : result;
		}

		// The cast is called only when the key is in the unfiltered map,
		// implying
		// that key is a K.
		@Override
		@SuppressWarnings("unchecked")
		public boolean containsKey(Object key) {
			return unfiltered.containsKey(key) && keyPredicate.apply((K) key);
		}
	}

	static class FilteredEntryMap<K, V> extends AbstractFilteredMap<K, V> {
		/**
		 * Entries in this set satisfy the predicate, but they don't validate
		 * the input to {@code Entry.setValue()}.
		 */
		final Set<Entry<K, V>> filteredEntrySet;

		FilteredEntryMap(Map<K, V> unfiltered,
				Predicate<? super Entry<K, V>> entryPredicate) {
			super(unfiltered, entryPredicate);
			filteredEntrySet = Sets.filter(unfiltered.entrySet(), predicate);
		}

		Set<Entry<K, V>> entrySet;

		@Override
		public Set<Entry<K, V>> entrySet() {
			Set<Entry<K, V>> result = entrySet;
			return (result == null) ? entrySet = new EntrySet() : result;
		}

		private class EntrySet extends ForwardingSet<Entry<K, V>> {
			@Override
			protected Set<Entry<K, V>> delegate() {
				return filteredEntrySet;
			}

			@Override
			public Iterator<Entry<K, V>> iterator() {
				final Iterator<Entry<K, V>> iterator = filteredEntrySet
						.iterator();
				return new UnmodifiableIterator<Entry<K, V>>() {
					public boolean hasNext() {
						return iterator.hasNext();
					}

					public Entry<K, V> next() {
						final Entry<K, V> entry = iterator.next();
						return new ForwardingMapEntry<K, V>() {
							@Override
							protected Entry<K, V> delegate() {
								return entry;
							}

							@Override
							public V setValue(V value) {
								Preconditions.checkArgument(apply(
										entry.getKey(), value));
								return super.setValue(value);
							}
						};
					}
				};
			}
		}

		Set<K> keySet;

		@Override
		public Set<K> keySet() {
			Set<K> result = keySet;
			return (result == null) ? keySet = new KeySet() : result;
		}

		private class KeySet extends AbstractSet<K> {
			@Override
			public Iterator<K> iterator() {
				final Iterator<Entry<K, V>> iterator = filteredEntrySet
						.iterator();
				return new UnmodifiableIterator<K>() {
					public boolean hasNext() {
						return iterator.hasNext();
					}

					public K next() {
						return iterator.next().getKey();
					}
				};
			}

			@Override
			public int size() {
				return filteredEntrySet.size();
			}

			@Override
			public void clear() {
				filteredEntrySet.clear();
			}

			@Override
			public boolean contains(Object o) {
				return containsKey(o);
			}

			@Override
			public boolean remove(Object o) {
				if (containsKey(o)) {
					unfiltered.remove(o);
					return true;
				}
				return false;
			}

			@Override
			public boolean removeAll(Collection<?> collection) {
				Preconditions.checkNotNull(collection); // for GWT
				boolean changed = false;
				for (Object obj : collection) {
					changed |= remove(obj);
				}
				return changed;
			}

			@Override
			public boolean retainAll(Collection<?> collection) {
				Preconditions.checkNotNull(collection); // for GWT
				boolean changed = false;
				Iterator<Entry<K, V>> iterator = unfiltered.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<K, V> entry = iterator.next();
					if (!collection.contains(entry.getKey())
							&& predicate.apply(entry)) {
						iterator.remove();
						changed = true;
					}
				}
				return changed;
			}

			@Override
			public Object[] toArray() {
				// creating an ArrayList so filtering happens once
				return Lists.newArrayList(iterator()).toArray();
			}

			@Override
			public <T> T[] toArray(T[] array) {
				return Lists.newArrayList(iterator()).toArray(array);
			}
		}
	}

	/**
	 * {@code AbstractMap} extension that implements {@link #isEmpty()} as
	 * {@code entrySet().isEmpty()} instead of {@code size() == 0} to speed up
	 * implementations where {@code size()} is O(n), and it delegates the
	 * {@code isEmpty()} methods of its key set and value collection to this
	 * implementation.
	 */
	@GwtCompatible
	abstract static class ImprovedAbstractMap<K, V> extends AbstractMap<K, V> {
		/**
		 * Creates the entry set to be returned by {@link #entrySet()}. This
		 * method is invoked at most once on a given map, at the time when
		 * {@code entrySet} is first called.
		 */
		protected abstract Set<Entry<K, V>> createEntrySet();

		private Set<Entry<K, V>> entrySet;

		@Override
		public Set<Entry<K, V>> entrySet() {
			Set<Entry<K, V>> result = entrySet;
			if (result == null) {
				entrySet = result = createEntrySet();
			}
			return result;
		}

		private Set<K> keySet;

		@Override
		public Set<K> keySet() {
			Set<K> result = keySet;
			if (result == null) {
				final Set<K> delegate = super.keySet();
				keySet = result = new ForwardingSet<K>() {
					@Override
					protected Set<K> delegate() {
						return delegate;
					}

					@Override
					public boolean isEmpty() {
						return ImprovedAbstractMap.this.isEmpty();
					}

					@Override
					public boolean remove(Object object) {
						if (contains(object)) {
							ImprovedAbstractMap.this.remove(object);
							return true;
						}
						return false;
					}
				};
			}
			return result;
		}

		private Collection<V> values;

		@Override
		public Collection<V> values() {
			Collection<V> result = values;
			if (result == null) {
				final Collection<V> delegate = super.values();
				values = result = new ForwardingCollection<V>() {
					@Override
					protected Collection<V> delegate() {
						return delegate;
					}

					@Override
					public boolean isEmpty() {
						return ImprovedAbstractMap.this.isEmpty();
					}
				};
			}
			return result;
		}

		/**
		 * Returns {@code true} if this map contains no key-value mappings.
		 * 
		 * <p>
		 * The implementation returns {@code entrySet().isEmpty()}.
		 * 
		 * @return {@code true} if this map contains no key-value mappings
		 */
		@Override
		public boolean isEmpty() {
			return entrySet().isEmpty();
		}
	}

	static final MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER
			.withKeyValueSeparator("=");

	/**
	 * Delegates to {@link Map#get}. Returns {@code null} on
	 * {@code ClassCastException}.
	 */
	static <V> V safeGet(Map<?, V> map, Object key) {
		try {
			return map.get(key);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Delegates to {@link Map#containsKey}. Returns {@code false} on
	 * {@code ClassCastException}
	 */
	static boolean safeContainsKey(Map<?, ?> map, Object key) {
		try {
			return map.containsKey(key);
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * An implementation of {@link Map#entrySet}.
	 */
	static <K, V> Set<Entry<K, V>> entrySetImpl(Map<K, V> map,
			Supplier<Iterator<Entry<K, V>>> entryIteratorSupplier) {
		return new EntrySetImpl<K, V>(map, entryIteratorSupplier);
	}

	private static class EntrySetImpl<K, V> extends AbstractSet<Entry<K, V>> {
		private final Map<K, V> map;
		private final Supplier<Iterator<Entry<K, V>>> entryIteratorSupplier;

		EntrySetImpl(Map<K, V> map,
				Supplier<Iterator<Entry<K, V>>> entryIteratorSupplier) {
			this.map = Preconditions.checkNotNull(map);
			this.entryIteratorSupplier = Preconditions
					.checkNotNull(entryIteratorSupplier);
		}

		@Override
		public Iterator<Entry<K, V>> iterator() {
			return entryIteratorSupplier.get();
		}

		@Override
		public int size() {
			return map.size();
		}

		@Override
		public void clear() {
			map.clear();
		}

		@Override
		public boolean contains(Object o) {
			if (o instanceof Entry) {
				Entry<?, ?> entry = (Entry<?, ?>) o;
				Object key = entry.getKey();
				if (map.containsKey(key)) {
					V value = map.get(entry.getKey());
					return Objects.equal(value, entry.getValue());
				}
			}
			return false;
		}

		@Override
		public boolean isEmpty() {
			return map.isEmpty();
		}

		@Override
		public boolean remove(Object o) {
			if (contains(o)) {
				Entry<?, ?> entry = (Entry<?, ?>) o;
				map.remove(entry.getKey());
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return map.hashCode();
		}
	}

	/**
	 * Implements {@code Collection.contains} safely for forwarding collections
	 * of map entries. If {@code o} is an instance of {@code Map.Entry}, it is
	 * wrapped using {@link #unmodifiableEntry} to protect against a possible
	 * nefarious equals method.
	 * 
	 * <p>
	 * Note that {@code c} is the backing (delegate) collection, rather than the
	 * forwarding collection.
	 * 
	 * @param c
	 *            the delegate (unwrapped) collection of map entries
	 * @param o
	 *            the object that might be contained in {@code c}
	 * @return {@code true} if {@code c} contains {@code o}
	 */
	static <K, V> boolean containsEntryImpl(Collection<Entry<K, V>> c, Object o) {
		if (!(o instanceof Entry)) {
			return false;
		}
		return c.contains(unmodifiableEntry((Entry<?, ?>) o));
	}

	/**
	 * Implements {@code Collection.remove} safely for forwarding collections of
	 * map entries. If {@code o} is an instance of {@code Map.Entry}, it is
	 * wrapped using {@link #unmodifiableEntry} to protect against a possible
	 * nefarious equals method.
	 * 
	 * <p>
	 * Note that {@code c} is backing (delegate) collection, rather than the
	 * forwarding collection.
	 * 
	 * @param c
	 *            the delegate (unwrapped) collection of map entries
	 * @param o
	 *            the object to remove from {@code c}
	 * @return {@code true} if {@code c} was changed
	 */
	static <K, V> boolean removeEntryImpl(Collection<Entry<K, V>> c, Object o) {
		if (!(o instanceof Entry)) {
			return false;
		}
		return c.remove(unmodifiableEntry((Entry<?, ?>) o));
	}

	/**
	 * An implementation of {@link Map#equals}.
	 */
	static boolean equalsImpl(Map<?, ?> map, Object object) {
		if (map == object) {
			return true;
		}
		if (object instanceof Map) {
			Map<?, ?> o = (Map<?, ?>) object;
			return map.entrySet().equals(o.entrySet());
		}
		return false;
	}

	/**
	 * An implementation of {@link Map#hashCode}.
	 */
	static int hashCodeImpl(Map<?, ?> map) {
		return Sets.hashCodeImpl(map.entrySet());
	}

	/**
	 * An implementation of {@link Map#toString}.
	 */
	static String toStringImpl(Map<?, ?> map) {
		StringBuilder sb = Collections2.newStringBuilderForCollection(
				map.size()).append('{');
		STANDARD_JOINER.appendTo(sb, map);
		return sb.append('}').toString();
	}

	/**
	 * An implementation of {@link Map#putAll}.
	 */
	static <K, V> void putAllImpl(Map<K, V> self,
			Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			self.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * An implementation of {@link Map#keySet}.
	 */
	static <K, V> Set<K> keySetImpl(final Map<K, V> map) {
		return new AbstractMapWrapper<K, V>(map).keySet();
	}

	/**
	 * An admittedly inefficient implementation of {@link Map#containsKey}.
	 */
	static boolean containsKeyImpl(Map<?, ?> map, Object key) {
		for (Entry<?, ?> entry : map.entrySet()) {
			if (Objects.equal(entry.getKey(), key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * An implementation of {@link Map#values}.
	 */
	static <K, V> Collection<V> valuesImpl(Map<K, V> map) {
		return new AbstractMapWrapper<K, V>(map).values();
	}

	/**
	 * An implementation of {@link Map#containsValue}.
	 */
	static boolean containsValueImpl(Map<?, ?> map, Object value) {
		for (Entry<?, ?> entry : map.entrySet()) {
			if (Objects.equal(entry.getValue(), value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * A wrapper on a map that supplies the {@code ImprovedAbstractMap}
	 * implementations of {@code keySet()} and {@code values()}.
	 */
	private static final class AbstractMapWrapper<K, V> extends
			ImprovedAbstractMap<K, V> {
		private final Map<K, V> map;

		AbstractMapWrapper(Map<K, V> map) {
			this.map = Preconditions.checkNotNull(map);
		}

		@Override
		public void clear() {
			map.clear();
		}

		@Override
		public boolean containsKey(Object key) {
			return map.containsKey(key);
		}

		@Override
		public V remove(Object key) {
			return map.remove(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return map.containsValue(value);
		}

		@Override
		protected Set<Entry<K, V>> createEntrySet() {
			return map.entrySet();
		}

		@Override
		public boolean isEmpty() {
			return map.isEmpty();
		}

		@Override
		public int size() {
			return map.size();
		}
	}
}
