package com.yida.spider4j.crawler.utils.collection;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A {@code Multimap} that cannot hold duplicate key-value pairs. Adding a
 * key-value pair that's already in the multimap has no effect.
 *
 * <p>The {@link #get}, {@link #removeAll}, and {@link #replaceValues} methods
 * each return a {@link Set} of values, while {@link #entries} returns a {@code
 * Set} of map entries. Though the method signature doesn't say so explicitly,
 * the map returned by {@link #asMap} has {@code Set} values.
 *
 * @author Jared Levy
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V> {
  /**
   * {@inheritDoc}
   *
   * <p>Because a {@code SetMultimap} has unique values for a given key, this
   * method returns a {@link Set}, instead of the {@link java.util.Collection}
   * specified in the {@link Multimap} interface.
   */
  Set<V> get(K key);

  /**
   * {@inheritDoc}
   *
   * <p>Because a {@code SetMultimap} has unique values for a given key, this
   * method returns a {@link Set}, instead of the {@link java.util.Collection}
   * specified in the {@link Multimap} interface.
   */
  Set<V> removeAll(Object key);

  /**
   * {@inheritDoc}
   *
   * <p>Because a {@code SetMultimap} has unique values for a given key, this
   * method returns a {@link Set}, instead of the {@link java.util.Collection}
   * specified in the {@link Multimap} interface.
   *
   * <p>Any duplicates in {@code values} will be stored in the multimap once.
   */
  Set<V> replaceValues(K key, Iterable<? extends V> values);

  /**
   * {@inheritDoc}
   *
   * <p>Because a {@code SetMultimap} has unique values for a given key, this
   * method returns a {@link Set}, instead of the {@link java.util.Collection}
   * specified in the {@link Multimap} interface.
   */
  Set<Map.Entry<K, V>> entries();

  /**
   * {@inheritDoc}
   *
   * <p>Though the method signature doesn't say so explicitly, the returned map
   * has {@link Set} values.
   */
  Map<K, Collection<V>> asMap();

  /**
   * Compares the specified object to this multimap for equality.
   *
   * <p>Two {@code SetMultimap} instances are equal if, for each key, they
   * contain the same values. Equality does not depend on the ordering of keys
   * or values.
   */
  boolean equals(Object obj);
}
