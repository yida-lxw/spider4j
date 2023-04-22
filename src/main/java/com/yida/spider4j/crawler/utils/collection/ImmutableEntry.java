package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * @see com.google.common.collect.Maps#immutableEntry(Object, Object)
 */
@GwtCompatible(serializable = true)
class ImmutableEntry<K, V> extends AbstractMapEntry<K, V>
    implements Serializable {
  private final K key;
  private final V value;

  ImmutableEntry(K key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override public K getKey() {
    return key;
  }

  @Override public V getValue() {
    return value;
  }
  
  @Override public final V setValue(V value){
    throw new UnsupportedOperationException();
  }
  
  private static final long serialVersionUID = 0;
}
