package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;


/** An ordering for a pre-existing {@code comparator}. */
@GwtCompatible(serializable = true)
final class ComparatorOrdering<T> extends Ordering<T> implements Serializable {
  final Comparator<T> comparator;

  ComparatorOrdering(Comparator<T> comparator) {
    this.comparator = Preconditions.checkNotNull(comparator);
  }

  @Override public int compare(T a, T b) {
    return comparator.compare(a, b);
  }

  // Override just to remove a level of indirection from inner loops
  @Override public int binarySearch(List<? extends T> sortedList, T key) {
    return Collections.binarySearch(sortedList, key, comparator);
  }

  // Override just to remove a level of indirection from inner loops
  @Override public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
    List<E> list = Lists.newArrayList(iterable);
    Collections.sort(list, comparator);
    return list;
  }

  @Override public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (object instanceof ComparatorOrdering) {
      ComparatorOrdering<?> that = (ComparatorOrdering<?>) object;
      return this.comparator.equals(that.comparator);
    }
    return false;
  }

  @Override public int hashCode() {
    return comparator.hashCode();
  }

  @Override public String toString() {
    return comparator.toString();
  }

  private static final long serialVersionUID = 0;
}
