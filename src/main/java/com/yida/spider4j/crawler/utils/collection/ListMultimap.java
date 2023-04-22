package com.yida.spider4j.crawler.utils.collection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A {@code Multimap} that can hold duplicate key-value pairs and that maintains
 * the insertion ordering of values for a given key.
 *
 * <p>The {@link #get}, {@link #removeAll}, and {@link #replaceValues} methods
 * each return a {@link List} of values. Though the method signature doesn't say
 * so explicitly, the map returned by {@link #asMap} has {@code List} values.
 *
 * @author Jared Levy
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public interface ListMultimap<K, V> extends Multimap<K, V> {
  /**
   * {@inheritDoc}
   *
   * <p>Because the values for a given key may have duplicates and follow the
   * insertion ordering, this method returns a {@link List}, instead of the
   * {@link java.util.Collection} specified in the {@link Multimap} interface.
   */
  List<V> get(K key);

  /**
   * {@inheritDoc}
   *
   * <p>Because the values for a given key may have duplicates and follow the
   * insertion ordering, this method returns a {@link List}, instead of the
   * {@link java.util.Collection} specified in the {@link Multimap} interface.
   */
  List<V> removeAll(Object key);

  /**
   * {@inheritDoc}
   *
   * <p>Because the values for a given key may have duplicates and follow the
   * insertion ordering, this method returns a {@link List}, instead of the
   * {@link java.util.Collection} specified in the {@link Multimap} interface.
   */
  List<V> replaceValues(K key, Iterable<? extends V> values);

  /**
   * {@inheritDoc}
   *
   * <p>Though the method signature doesn't say so explicitly, the returned map
   * has {@link List} values.
   */
  Map<K, Collection<V>> asMap();

  /**
   * Compares the specified object to this multimap for equality.
   *
   * <p>Two {@code ListMultimap} instances are equal if, for each key, they
   * contain the same values in the same order. If the value orderings disagree,
   * the multimaps will not be considered equal.
   */
  boolean equals(Object obj);
}
