package com.yida.spider4j.crawler.utils.collection;

import java.util.concurrent.ConcurrentMap;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A concurrent map which forwards all its method calls to another concurrent
 * map. Subclasses should override one or more methods to modify the behavior of
 * the backing map as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Charles Fry
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public abstract class ForwardingConcurrentMap<K, V> extends ForwardingMap<K, V>
    implements ConcurrentMap<K, V> {

  /** Constructor for use by subclasses. */
  protected ForwardingConcurrentMap() {}

  @Override protected abstract ConcurrentMap<K, V> delegate();

  public V putIfAbsent(K key, V value) {
    return delegate().putIfAbsent(key, value);
  }

  public boolean remove(Object key, Object value) {
    return delegate().remove(key, value);
  }

  public V replace(K key, V value) {
    return delegate().replace(key, value);
  }

  public boolean replace(K key, V oldValue, V newValue) {
    return delegate().replace(key, oldValue, newValue);
  }

}
