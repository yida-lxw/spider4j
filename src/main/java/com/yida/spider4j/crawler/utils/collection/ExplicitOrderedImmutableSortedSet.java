package com.yida.spider4j.crawler.utils.collection;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * An immutable sorted set consisting of, and ordered by, a list of provided
 * elements.
 *
 * @author Jared Levy
 */
// TODO(jlevy): Create superclass with code shared by this class and
// RegularImmutableSortedSet.
@SuppressWarnings({ "unchecked", "rawtypes","serial" })
@GwtCompatible(serializable = true, emulated = true)
final class ExplicitOrderedImmutableSortedSet<E>
    extends ImmutableSortedSet<E> {

  static <E> ImmutableSortedSet<E> create(List<E> list) {
    ExplicitOrdering<E> ordering = new ExplicitOrdering<E>(list);
    if (ordering.rankMap.isEmpty()) {
      return emptySet(ordering);
    }
    // Not using list.toArray() to avoid iterating across the input list twice.
    Object[] elements = ordering.rankMap.keySet().toArray();
    return new ExplicitOrderedImmutableSortedSet<E>(elements, ordering);
  }

  private final Object[] elements;
  /**
   * The index of the first element that's in the sorted set (inclusive
   * index).
   */
  private final int fromIndex;
  /**
   * The index after the last element that's in the sorted set (exclusive
   * index).
   */
  private final int toIndex;

  ExplicitOrderedImmutableSortedSet(Object[] elements,
      Comparator<? super E> comparator) {
    this(elements, comparator, 0, elements.length);
  }

  ExplicitOrderedImmutableSortedSet(Object[] elements,
      Comparator<? super E> comparator, int fromIndex, int toIndex) {
    super(comparator);
    this.elements = elements;
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  // create() generates an ImmutableMap<E, Integer> rankMap.
  private ImmutableMap<E, Integer> rankMap() {
    return ((ExplicitOrdering) comparator()).rankMap;
  }

  // create() ensures that every element is an E.
  @Override public UnmodifiableIterator<E> iterator() {
    return (UnmodifiableIterator<E>)
        Iterators.forArray(elements, fromIndex, size());
  }

  @Override public boolean isEmpty() {
    return false;
  }

  public int size() {
    return toIndex - fromIndex;
  }

  @Override public boolean contains(Object o) {
    Integer index = rankMap().get(o);
    return (index != null && index >= fromIndex && index < toIndex);
  }

  @Override boolean isPartialView() {
    return fromIndex != 0 || toIndex != elements.length;
  }

  @Override public Object[] toArray() {
    Object[] array = new Object[size()];
    Platform.unsafeArrayCopy(elements, fromIndex, array, 0, size());
    return array;
  }

  // TODO(jlevy): Move to ObjectArrays (same code in ImmutableList).
  @Override public <T> T[] toArray(T[] array) {
    int size = size();
    if (array.length < size) {
      array = ObjectArrays.newArray(array, size);
    } else if (array.length > size) {
      array[size] = null;
    }
    Platform.unsafeArrayCopy(elements, fromIndex, array, 0, size);
    return array;
  }

  @Override public int hashCode() {
    // TODO(jlevy): Cache hash code?
    int hash = 0;
    for (int i = fromIndex; i < toIndex; i++) {
      hash += elements[i].hashCode();
    }
    return hash;
  }

  // The factory methods ensure that every element is an E.
  public E first() {
    return (E) elements[fromIndex];
  }

  // The factory methods ensure that every element is an E.
  public E last() {
    return (E) elements[toIndex - 1];
  }

  @Override ImmutableSortedSet<E> headSetImpl(E toElement) {
    return createSubset(fromIndex, findSubsetIndex(toElement));
  }

  // TODO(jlevy): Override subSet to avoid redundant map lookups.
  @Override ImmutableSortedSet<E> subSetImpl(E fromElement, E toElement) {
    return createSubset(
        findSubsetIndex(fromElement), findSubsetIndex(toElement));
  }

  @Override ImmutableSortedSet<E> tailSetImpl(E fromElement) {
    return createSubset(findSubsetIndex(fromElement), toIndex);
  }

  private int findSubsetIndex(E element) {
    Integer index = rankMap().get(element);
    if (index == null) {
      // TODO(kevinb): Make Ordering.IncomparableValueException public, use it
      throw new ClassCastException();
    }
    if (index <= fromIndex) {
      return fromIndex;
    } else if (index >= toIndex) {
      return toIndex;
    } else {
      return index;
    }
  }

  private ImmutableSortedSet<E> createSubset(
      int newFromIndex, int newToIndex) {
    if (newFromIndex < newToIndex) {
      return new ExplicitOrderedImmutableSortedSet<E>(elements, comparator,
          newFromIndex, newToIndex);
    } else {
      return emptySet(comparator);
    }
  }

  @Override int indexOf(Object target) {
    Integer index = rankMap().get(target);
    return (index != null && index >= fromIndex && index < toIndex)
        ? index - fromIndex : -1;
  }

  /*
   * TODO(jlevy): Modify ImmutableSortedAsList.subList() so it creates a list
   * based on an ExplicitOrderedImmutableSortedSet when the original list was
   * constructed from one, for faster contains(), indexOf(), and lastIndexOf().
   */
  @Override ImmutableList<E> createAsList() {
    return new ImmutableSortedAsList<E>(
        this, new RegularImmutableList<E>(elements, fromIndex, size()));
  }

  /*
   * Generates an ExplicitOrderedImmutableSortedSet when deserialized, for
   * better performance.
   */
  private static class SerializedForm<E> implements Serializable {
    final Object[] elements;

    public SerializedForm(Object[] elements) {
      this.elements = elements;
    }

    Object readResolve() {
      return ImmutableSortedSet.withExplicitOrder(Arrays.asList(elements));
    }

    private static final long serialVersionUID = 0;
  }

  private void readObject(ObjectInputStream stream)
      throws InvalidObjectException {
    throw new InvalidObjectException("Use SerializedForm");
  }

  @Override Object writeReplace() {
    return new SerializedForm<E>(toArray());
  }
}
