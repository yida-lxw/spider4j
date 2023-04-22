package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.ImmutableSet.ArrayImmutableSet;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.VisibleForTesting;


/**
 * Implementation of {@link ImmutableSet} with two or more elements.
 *
 * @author Kevin Bourrillion
 */
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings("serial") // uses writeReplace(), not default serialization
final class RegularImmutableSet<E> extends ArrayImmutableSet<E> {
  // the same elements in hashed positions (plus nulls)
  @VisibleForTesting final transient Object[] table;
  // 'and' with an int to get a valid table index.
  private final transient int mask;
  private final transient int hashCode;

  RegularImmutableSet(
      Object[] elements, int hashCode, Object[] table, int mask) {
    super(elements);
    this.table = table;
    this.mask = mask;
    this.hashCode = hashCode;
  }

  @Override public boolean contains(Object target) {
    if (target == null) {
      return false;
    }
    for (int i = Hashing.smear(target.hashCode()); true; i++) {
      Object candidate = table[i & mask];
      if (candidate == null) {
        return false;
      }
      if (candidate.equals(target)) {
        return true;
      }
    }
  }

  @Override public int hashCode() {
    return hashCode;
  }

  @Override boolean isHashCodeFast() {
    return true;
  }
}
