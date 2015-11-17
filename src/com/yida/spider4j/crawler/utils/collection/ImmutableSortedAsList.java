package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * List returned by {@code ImmutableSortedSet.asList()} when the set isn't
 * empty.
 *
 * @author Jared Levy
 * @author Louis Wasserman
 */
@SuppressWarnings("serial")
final class ImmutableSortedAsList<E> extends ImmutableList<E> {
  private final transient ImmutableSortedSet<E> backingSet;
  private final transient ImmutableList<E> backingList;

  ImmutableSortedAsList(
      ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList) {
    this.backingSet = backingSet;
    this.backingList = backingList;
  }

  // Override contains(), indexOf(), and lastIndexOf() to be O(log N) instead of
  // O(N).

  @Override public boolean contains(Object target) {
    return backingSet.indexOf(target) >= 0;
  }

  @Override public int indexOf(Object target) {
    return backingSet.indexOf(target);
  }

  @Override public int lastIndexOf(Object target) {
    return backingSet.indexOf(target);
  }

  // The returned ImmutableSortedAsList maintains the contains(), indexOf(), and
  // lastIndexOf() performance benefits.
  @Override public ImmutableList<E> subList(int fromIndex, int toIndex) {
    Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
    return (fromIndex == toIndex) ? ImmutableList.<E>of()
        : new RegularImmutableSortedSet<E>(
            backingList.subList(fromIndex, toIndex), backingSet.comparator())
            .asList();
  }

  // The ImmutableAsList serialized form has the correct behavior.
  @Override Object writeReplace() {
    return new ImmutableAsList.SerializedForm(backingSet);
  }

  @Override public UnmodifiableIterator<E> iterator() {
    return backingList.iterator();
  }

  @Override public E get(int index) {
    return backingList.get(index);
  }

  @Override public UnmodifiableListIterator<E> listIterator() {
    return backingList.listIterator();
  }

  @Override public UnmodifiableListIterator<E> listIterator(int index) {
    return backingList.listIterator(index);
  }

  @Override public int size() {
    return backingList.size();
  }

  @Override public boolean equals(Object obj) {
    return backingList.equals(obj);
  }

  @Override public int hashCode() {
    return backingList.hashCode();
  }

  @Override boolean isPartialView() {
    return backingList.isPartialView();
  }
}
