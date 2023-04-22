package com.yida.spider4j.crawler.utils.collection;

import java.util.List;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Implementation of {@link ImmutableList} with one or more elements.
 *
 * @author Kevin Bourrillion
 */
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings("serial") // uses writeReplace(), not default serialization
class RegularImmutableList<E> extends ImmutableList<E> {
  private final transient int offset;
  private final transient int size;
  private final transient Object[] array;

  RegularImmutableList(Object[] array, int offset, int size) {
    this.offset = offset;
    this.size = size;
    this.array = array;
  }

  RegularImmutableList(Object[] array) {
    this(array, 0, array.length);
  }

  public int size() {
    return size;
  }

  @Override public boolean isEmpty() {
    return false;
  }

  @Override boolean isPartialView() {
    return offset != 0 || size != array.length;
  }

  @Override public boolean contains(Object target) {
    return indexOf(target) != -1;
  }

  // The fake cast to E is safe because the creation methods only allow E's
  @SuppressWarnings("unchecked")
  @Override public UnmodifiableIterator<E> iterator() {
    return (UnmodifiableIterator<E>) Iterators.forArray(array, offset, size);
  }

  @Override public Object[] toArray() {
    Object[] newArray = new Object[size()];
    System.arraycopy(array, offset, newArray, 0, size);
    return newArray;
  }

  @Override public <T> T[] toArray(T[] other) {
    if (other.length < size) {
      other = ObjectArrays.newArray(other, size);
    } else if (other.length > size) {
      other[size] = null;
    }
    System.arraycopy(array, offset, other, 0, size);
    return other;
  }

  // The fake cast to E is safe because the creation methods only allow E's
  @SuppressWarnings("unchecked")
  public E get(int index) {
    Preconditions.checkElementIndex(index, size);
    return (E) array[index + offset];
  }

  @Override public int indexOf(Object target) {
    if (target != null) {
      for (int i = offset; i < offset + size; i++) {
        if (array[i].equals(target)) {
          return i - offset;
        }
      }
    }
    return -1;
  }

  @Override public int lastIndexOf(Object target) {
    if (target != null) {
      for (int i = offset + size - 1; i >= offset; i--) {
        if (array[i].equals(target)) {
          return i - offset;
        }
      }
    }
    return -1;
  }

  @Override public ImmutableList<E> subList(int fromIndex, int toIndex) {
    Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
    return (fromIndex == toIndex)
        ? ImmutableList.<E>of()
        : new RegularImmutableList<E>(
            array, offset + fromIndex, toIndex - fromIndex);
  }

  @Override public UnmodifiableListIterator<E> listIterator(final int start) {
    return new AbstractIndexedListIterator<E>(size, start) {
      // The fake cast to E is safe because the creation methods only allow E's
      @SuppressWarnings("unchecked")
      @Override protected E get(int index) {
        return (E) array[index + offset];
      }

    };
  }

  @Override public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof List)) {
      return false;
    }

    List<?> that = (List<?>) object;
    if (this.size() != that.size()) {
      return false;
    }

    int index = offset;
    if (object instanceof RegularImmutableList) {
      RegularImmutableList<?> other = (RegularImmutableList<?>) object;
      for (int i = other.offset; i < other.offset + other.size; i++) {
        if (!array[index++].equals(other.array[i])) {
          return false;
        }
      }
    } else {
      for (Object element : that) {
        if (!array[index++].equals(element)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override public int hashCode() {
    // not caching hash code since it could change if the elements are mutable
    // in a way that modifies their hash codes
    int hashCode = 1;
    for (int i = offset; i < offset + size; i++) {
      hashCode = 31 * hashCode + array[i].hashCode();
    }
    return hashCode;
  }

  @Override public String toString() {
    StringBuilder sb = Collections2.newStringBuilderForCollection(size())
        .append('[').append(array[offset]);
    for (int i = offset + 1; i < offset + size; i++) {
      sb.append(", ").append(array[i]);
    }
    return sb.append(']').toString();
  }

  int offset() {
    return offset;
  }

  Object[] array() {
    return array;
  }
}
