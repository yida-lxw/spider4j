package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * Bimap with one or more mappings.
 * 
 * @author Jared Levy
 */
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings("serial") // uses writeReplace(), not default serialization
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
  final transient ImmutableMap<K, V> delegate;
  final transient ImmutableBiMap<V, K> inverse;

  RegularImmutableBiMap(ImmutableMap<K, V> delegate) {
    this.delegate = delegate;

    ImmutableMap.Builder<V, K> builder = ImmutableMap.builder();
    for (Entry<K, V> entry : delegate.entrySet()) {
      builder.put(entry.getValue(), entry.getKey());
    }
    ImmutableMap<V, K> backwardMap = builder.build();
    this.inverse = new RegularImmutableBiMap<V, K>(backwardMap, this);
  }

  RegularImmutableBiMap(ImmutableMap<K, V> delegate,
      ImmutableBiMap<V, K> inverse) {
    this.delegate = delegate;
    this.inverse = inverse;
  }

  @Override ImmutableMap<K, V> delegate() {
    return delegate;
  }

  @Override public ImmutableBiMap<V, K> inverse() {
    return inverse;
  }

  @Override boolean isPartialView() {
    return delegate.isPartialView() || inverse.delegate().isPartialView();
  }
}
