package com.yida.spider4j.crawler.utils.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A multimap which forwards all its method calls to another multimap.
 * Subclasses should override one or more methods to modify the behavior of
 * the backing multimap as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Robert Konigsberg
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public abstract class ForwardingMultimap<K, V> extends ForwardingObject
    implements Multimap<K, V> {

  /** Constructor for use by subclasses. */
  protected ForwardingMultimap() {}

  @Override protected abstract Multimap<K, V> delegate();

  public Map<K, Collection<V>> asMap() {
    return delegate().asMap();
  }

  public void clear() {
    delegate().clear();
  }

  public boolean containsEntry(Object key, Object value) {
    return delegate().containsEntry(key, value);
  }

  public boolean containsKey(Object key) {
    return delegate().containsKey(key);
  }

  public boolean containsValue(Object value) {
    return delegate().containsValue(value);
  }

  public Collection<Entry<K, V>> entries() {
    return delegate().entries();
  }

  public Collection<V> get(K key) {
    return delegate().get(key);
  }

  public boolean isEmpty() {
    return delegate().isEmpty();
  }

  public Multiset<K> keys() {
    return delegate().keys();
  }

  public Set<K> keySet() {
    return delegate().keySet();
  }

  public boolean put(K key, V value) {
    return delegate().put(key, value);
  }

  public boolean putAll(K key, Iterable<? extends V> values) {
    return delegate().putAll(key, values);
  }

  public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
    return delegate().putAll(multimap);
  }

  public boolean remove(Object key, Object value) {
    return delegate().remove(key, value);
  }

  public Collection<V> removeAll(Object key) {
    return delegate().removeAll(key);
  }

  public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
    return delegate().replaceValues(key, values);
  }

  public int size() {
    return delegate().size();
  }

  public Collection<V> values() {
    return delegate().values();
  }

  @Override public boolean equals(Object object) {
    return object == this || delegate().equals(object);
  }

  @Override public int hashCode() {
    return delegate().hashCode();
  }
}
