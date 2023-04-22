package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/** An ordering that uses the natural order of the values. */
@GwtCompatible(serializable = true)
@SuppressWarnings({"unchecked","rawtypes"}) // TODO(kevinb): the right way to explain this??
final class NaturalOrdering
    extends Ordering<Comparable> implements Serializable {
  static final NaturalOrdering INSTANCE = new NaturalOrdering();

  @Override public int compare(Comparable left, Comparable right) {
    Preconditions.checkNotNull(right); // left null is caught later
    if (left == right) {
      return 0;
    }

    // we're permitted to throw CCE
    int result = left.compareTo(right);
    return result;
  }

  // TODO(kevinb): the right way to explain this??
  @Override public <S extends Comparable> Ordering<S> reverse() {
    return (Ordering) ReverseNaturalOrdering.INSTANCE;
  }

  // Override to remove a level of indirection from inner loop
  // TODO(kevinb): the right way to explain this??
  @Override public int binarySearch(
      List<? extends Comparable> sortedList, Comparable key) {
    return Collections.binarySearch((List) sortedList, key);
  }

  // Override to remove a level of indirection from inner loop
  @Override public <E extends Comparable> List<E> sortedCopy(
      Iterable<E> iterable) {
    List<E> list = Lists.newArrayList(iterable);
    Collections.sort(list);
    return list;
  }

  // preserving singleton-ness gives equals()/hashCode() for free
  private Object readResolve() {
    return INSTANCE;
  }

  @Override public String toString() {
    return "Ordering.natural()";
  }

  private NaturalOrdering() {}

  private static final long serialVersionUID = 0;
}
