package com.yida.spider4j.crawler.utils.collection;

import java.util.Comparator;
import java.util.SortedSet;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A sorted set multimap which forwards all its method calls to another sorted
 * set multimap. Subclasses should override one or more methods to modify the
 * behavior of the backing multimap as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Kurt Alfred Kluever
 * @since 3
 */
@GwtCompatible
public abstract class ForwardingSortedSetMultimap<K, V>
    extends ForwardingSetMultimap<K, V> implements SortedSetMultimap<K, V> {

  /** Constructor for use by subclasses. */
  protected ForwardingSortedSetMultimap() {}

  @Override protected abstract SortedSetMultimap<K, V> delegate();

  @Override public SortedSet<V> get(K key) {
    return delegate().get(key);
  }

  @Override public SortedSet<V> removeAll(Object key) {
    return delegate().removeAll(key);
  }

  @Override public SortedSet<V> replaceValues(
      K key, Iterable<? extends V> values) {
    return delegate().replaceValues(key, values);
  }

  @Override public Comparator<? super V> valueComparator() {
    return delegate().valueComparator();
  }
}
