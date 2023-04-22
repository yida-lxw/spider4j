package com.yida.spider4j.crawler.utils.collection;

import java.util.Map.Entry;
import java.util.Set;

import com.sun.istack.internal.Nullable;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;


/**
 * A set multimap which forwards all its method calls to another set multimap.
 * Subclasses should override one or more methods to modify the behavior of
 * the backing multimap as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Kurt Alfred Kluever
 * @since 3
 */
@GwtCompatible
public abstract class ForwardingSetMultimap<K, V>
    extends ForwardingMultimap<K, V> implements SetMultimap<K, V> {

  @Override protected abstract SetMultimap<K, V> delegate();

  @Override public Set<Entry<K, V>> entries() {
    return delegate().entries();
  }

  @Override public Set<V> get(@Nullable K key) {
    return delegate().get(key);
  }

  @Override public Set<V> removeAll(@Nullable Object key) {
    return delegate().removeAll(key);
  }

  @Override public Set<V> replaceValues(K key, Iterable<? extends V> values) {
    return delegate().replaceValues(key, values);
  }
}
