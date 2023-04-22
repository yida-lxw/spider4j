package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/** An ordering that tries several comparators in order. */
@GwtCompatible(serializable = true)
final class CompoundOrdering<T> extends Ordering<T> implements Serializable {
  final ImmutableList<Comparator<? super T>> comparators;

  CompoundOrdering(Comparator<? super T> primary,
      Comparator<? super T> secondary) {
    this.comparators
        = ImmutableList.<Comparator<? super T>>of(primary, secondary);
  }

  CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) {
    this.comparators = ImmutableList.copyOf(comparators);
  }

  CompoundOrdering(List<? extends Comparator<? super T>> comparators,
      Comparator<? super T> lastComparator) {
    this.comparators = new ImmutableList.Builder<Comparator<? super T>>()
        .addAll(comparators).add(lastComparator).build();
  }

  @Override public int compare(T left, T right) {
    for (Comparator<? super T> comparator : comparators) {
      int result = comparator.compare(left, right);
      if (result != 0) {
        return result;
      }
    }
    return 0;
  }

  @Override public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (object instanceof CompoundOrdering) {
      CompoundOrdering<?> that = (CompoundOrdering<?>) object;
      return this.comparators.equals(that.comparators);
    }
    return false;
  }

  @Override public int hashCode() {
    return comparators.hashCode();
  }

  @Override public String toString() {
    return "Ordering.compound(" + comparators + ")";
  }

  private static final long serialVersionUID = 0;
}
