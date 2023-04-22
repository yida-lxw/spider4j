package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;


/** An ordering that treats {@code null} as less than all other values. */
@GwtCompatible(serializable = true)
@SuppressWarnings({ "unchecked", "rawtypes" }) 
final class NullsFirstOrdering<T> extends Ordering<T> implements Serializable {
  final Ordering<? super T> ordering;

  NullsFirstOrdering(Ordering<? super T> ordering) {
    this.ordering = ordering;
  }

  @Override public int compare(T left, T right) {
    if (left == right) {
      return 0;
    }
    if (left == null) {
      return RIGHT_IS_GREATER;
    }
    if (right == null) {
      return LEFT_IS_GREATER;
    }
    return ordering.compare(left, right);
  }

  @Override public <S extends T> Ordering<S> reverse() {
    // ordering.reverse() might be optimized, so let it do its thing
    return ordering.reverse().nullsLast();
  }

  // still need the right way to explain this
  @Override public <S extends T> Ordering<S> nullsFirst() {
    return (Ordering) this;
  }

  @Override public <S extends T> Ordering<S> nullsLast() {
    return ordering.nullsLast();
  }

  @Override public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (object instanceof NullsFirstOrdering) {
      NullsFirstOrdering<?> that = (NullsFirstOrdering<?>) object;
      return this.ordering.equals(that.ordering);
    }
    return false;
  }

  @Override public int hashCode() {
    return ordering.hashCode() ^ 957692532; // meaningless
  }

  @Override public String toString() {
    return ordering + ".nullsFirst()";
  }

  private static final long serialVersionUID = 0;
}
