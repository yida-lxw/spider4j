package com.yida.spider4j.crawler.utils.collection;

import java.util.List;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A list multimap which forwards all its method calls to another list multimap.
 * Subclasses should override one or more methods to modify the behavior of
 * the backing multimap as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Kurt Alfred Kluever
 * @since 3
 */
@GwtCompatible
public abstract class ForwardingListMultimap<K, V>
    extends ForwardingMultimap<K, V> implements ListMultimap<K, V> {

  /** Constructor for use by subclasses. */
  protected ForwardingListMultimap() {}

  @Override protected abstract ListMultimap<K, V> delegate();

  @Override public List<V> get(K key) {
    return delegate().get(key);
  }

  @Override public List<V> removeAll(Object key) {
    return delegate().removeAll(key);
  }

  @Override public List<V> replaceValues(K key, Iterable<? extends V> values) {
    return delegate().replaceValues(key, values);
  }
}
