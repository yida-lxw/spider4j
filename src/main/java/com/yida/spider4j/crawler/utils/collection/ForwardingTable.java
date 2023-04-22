package com.yida.spider4j.crawler.utils.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A table which forwards all its method calls to another table. Subclasses
 * should override one or more methods to modify the behavior of the backing
 * map as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Gregory Kick
 * @since 7
 */
@Beta
@GwtCompatible
public abstract class ForwardingTable<R, C, V> extends ForwardingObject
    implements Table<R, C, V> {
  /** Constructor for use by subclasses. */
  protected ForwardingTable() {}

  @Override protected abstract Table<R, C, V> delegate();

  public Set<Cell<R, C, V>> cellSet() {
    return delegate().cellSet();
  }

  public void clear() {
    delegate().clear();
  }

  public Map<R, V> column(C columnKey) {
    return delegate().column(columnKey);
  }

  public Set<C> columnKeySet() {
    return delegate().columnKeySet();
  }

  public Map<C, Map<R, V>> columnMap() {
    return delegate().columnMap();
  }

  public boolean contains(Object rowKey, Object columnKey) {
    return delegate().contains(rowKey, columnKey);
  }

  public boolean containsColumn(Object columnKey) {
    return delegate().containsColumn(columnKey);
  }

  public boolean containsRow(Object rowKey) {
    return delegate().containsRow(rowKey);
  }

  public boolean containsValue(Object value) {
    return delegate().containsValue(value);
  }

  public V get(Object rowKey, Object columnKey) {
    return delegate().get(rowKey, columnKey);
  }

  public boolean isEmpty() {
    return delegate().isEmpty();
  }

  public V put(R rowKey, C columnKey, V value) {
    return delegate().put(rowKey, columnKey, value);
  }

  public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
    delegate().putAll(table);
  }

  public V remove(Object rowKey, Object columnKey) {
    return delegate().remove(rowKey, columnKey);
  }

  public Map<C, V> row(R rowKey) {
    return delegate().row(rowKey);
  }

  public Set<R> rowKeySet() {
    return delegate().rowKeySet();
  }

  public Map<R, Map<C, V>> rowMap() {
    return delegate().rowMap();
  }

  public int size() {
    return delegate().size();
  }

  public Collection<V> values() {
    return delegate().values();
  }

  @Override public boolean equals(Object obj) {
    return (obj == this) || delegate().equals(obj);
  }

  @Override public int hashCode() {
    return delegate().hashCode();
  }
}
