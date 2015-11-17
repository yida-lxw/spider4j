package com.yida.spider4j.crawler.utils.collection;

import java.util.Map.Entry;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Objects;


/**
 * Implementation of the {@code equals}, {@code hashCode}, and {@code toString}
 * methods of {@code Entry}.
 *
 * @author Jared Levy
 */
@GwtCompatible
abstract class AbstractMapEntry<K, V> implements Entry<K, V> {

  public abstract K getKey();

  public abstract V getValue();

  public V setValue(V value) {
    throw new UnsupportedOperationException();
  }

  @Override public boolean equals(Object object) {
    if (object instanceof Entry) {
      Entry<?, ?> that = (Entry<?, ?>) object;
      return Objects.equal(this.getKey(), that.getKey())
          && Objects.equal(this.getValue(), that.getValue());
    }
    return false;
  }

  @Override public int hashCode() {
    K k = getKey();
    V v = getValue();
    return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
  }

  /**
   * Returns a string representation of the form <code>{key}={value}</code>.
   */
  @Override public String toString() {
    return getKey() + "=" + getValue();
  }
}
