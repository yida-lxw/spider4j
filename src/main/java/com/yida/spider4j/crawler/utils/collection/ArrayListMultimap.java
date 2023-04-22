package com.yida.spider4j.crawler.utils.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;
import com.yida.spider4j.crawler.utils.collection.anno.VisibleForTesting;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Implementation of {@code Multimap} that uses an {@code ArrayList} to store
 * the values for a given key. A {@link HashMap} associates each key with an
 * {@link ArrayList} of values.
 *
 * <p>When iterating through the collections supplied by this class, the
 * ordering of values for a given key agrees with the order in which the values
 * were added.
 *
 * <p>This multimap allows duplicate key-value pairs. After adding a new
 * key-value pair equal to an existing key-value pair, the {@code
 * ArrayListMultimap} will contain entries for both the new value and the old
 * value.
 *
 * <p>Keys and values may be null. All optional multimap methods are supported,
 * and all returned views are modifiable.
 *
 * <p>The lists returned by {@link #get}, {@link #removeAll}, and {@link
 * #replaceValues} all implement {@link java.util.RandomAccess}.
 *
 * <p>This class is not threadsafe when any concurrent operations update the
 * multimap. Concurrent read operations will work correctly. To allow concurrent
 * update operations, wrap your multimap with a call to {@link
 * Multimaps#synchronizedListMultimap}.
 *
 * @author Jared Levy
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible(serializable = true, emulated = true)
public final class ArrayListMultimap<K, V> extends AbstractListMultimap<K, V> {
  // Default from ArrayList
  private static final int DEFAULT_VALUES_PER_KEY = 10;

  @VisibleForTesting transient int expectedValuesPerKey;

  /**
   * Creates a new, empty {@code ArrayListMultimap} with the default initial
   * capacities.
   */
  public static <K, V> ArrayListMultimap<K, V> create() {
    return new ArrayListMultimap<K, V>();
  }

  /**
   * Constructs an empty {@code ArrayListMultimap} with enough capacity to hold
   * the specified numbers of keys and values without resizing.
   *
   * @param expectedKeys the expected number of distinct keys
   * @param expectedValuesPerKey the expected average number of values per key
   * @throws IllegalArgumentException if {@code expectedKeys} or {@code
   *      expectedValuesPerKey} is negative
   */
  public static <K, V> ArrayListMultimap<K, V> create(
      int expectedKeys, int expectedValuesPerKey) {
    return new ArrayListMultimap<K, V>(expectedKeys, expectedValuesPerKey);
  }

  /**
   * Constructs an {@code ArrayListMultimap} with the same mappings as the
   * specified multimap.
   *
   * @param multimap the multimap whose contents are copied to this multimap
   */
  public static <K, V> ArrayListMultimap<K, V> create(
      Multimap<? extends K, ? extends V> multimap) {
    return new ArrayListMultimap<K, V>(multimap);
  }

  private ArrayListMultimap() {
    super(new HashMap<K, Collection<V>>());
    expectedValuesPerKey = DEFAULT_VALUES_PER_KEY;
  }

  private ArrayListMultimap(int expectedKeys, int expectedValuesPerKey) {
    super(Maps.<K, Collection<V>>newHashMapWithExpectedSize(expectedKeys));
    Preconditions.checkArgument(expectedValuesPerKey >= 0);
    this.expectedValuesPerKey = expectedValuesPerKey;
  }

  private ArrayListMultimap(Multimap<? extends K, ? extends V> multimap) {
    this(multimap.keySet().size(),
        (multimap instanceof ArrayListMultimap) ?
            ((ArrayListMultimap<?, ?>) multimap).expectedValuesPerKey :
            DEFAULT_VALUES_PER_KEY);
    putAll(multimap);
  }

  /**
   * Creates a new, empty {@code ArrayList} to hold the collection of values for
   * an arbitrary key.
   */
  @Override List<V> createCollection() {
    return new ArrayList<V>(expectedValuesPerKey);
  }

  /**
   * Reduces the memory used by this {@code ArrayListMultimap}, if feasible.
   */
  public void trimToSize() {
    for (Collection<V> collection : backingMap().values()) {
      ArrayList<V> arrayList = (ArrayList<V>) collection;
      arrayList.trimToSize();
    }
  }

  /**
   * @serialData expectedValuesPerKey, number of distinct keys, and then for
   *     each distinct key: the key, number of values for that key, and the
   *     key's values
   */
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeInt(expectedValuesPerKey);
    Serialization.writeMultimap(this, stream);
  }

  @GwtIncompatible("java.io.ObjectOutputStream")
  private void readObject(ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    expectedValuesPerKey = stream.readInt();
    int distinctKeys = Serialization.readCount(stream);
    Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(distinctKeys);
    setMap(map);
    Serialization.populateMultimap(this, stream, distinctKeys);
  }

  @GwtIncompatible("Not needed in emulated source.")
  private static final long serialVersionUID = 0;
}
