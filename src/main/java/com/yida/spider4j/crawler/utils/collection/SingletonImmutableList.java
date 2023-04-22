package com.yida.spider4j.crawler.utils.collection;

import java.util.List;
import java.util.NoSuchElementException;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Implementation of {@link ImmutableList} with exactly one element.
 *
 * @author Hayward Chan
 */
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings("serial") // uses writeReplace(), not default serialization
final class SingletonImmutableList<E> extends ImmutableList<E> {

  final transient E element;

  SingletonImmutableList(E element) {
    this.element = Preconditions.checkNotNull(element);
  }

  public E get(int index) {
    Preconditions.checkElementIndex(index, 1);
    return element;
  }

  @Override public int indexOf(Object object) {
    return element.equals(object) ? 0 : -1;
  }

  @Override public UnmodifiableIterator<E> iterator() {
    return Iterators.singletonIterator(element);
  }

  @Override public int lastIndexOf(Object object) {
    return element.equals(object) ? 0 : -1;
  }

  @Override public UnmodifiableListIterator<E> listIterator(final int start) {
    Preconditions.checkPositionIndex(start, 1);
    return new UnmodifiableListIterator<E>() {

      boolean hasNext = start == 0;

      @Override public boolean hasNext() {
        return hasNext;
      }

      @Override public boolean hasPrevious() {
        return !hasNext;
      }

      @Override public E next() {
        if (!hasNext) {
          throw new NoSuchElementException();
        }
        hasNext = false;
        return element;
      }

      @Override public int nextIndex() {
        return hasNext ? 0 : 1;
      }

      @Override public E previous() {
        if (hasNext) {
          throw new NoSuchElementException();
        }
        hasNext = true;
        return element;
      }

      @Override public int previousIndex() {
        return hasNext ? -1 : 0;
      }
    };
  }

  public int size() {
    return 1;
  }

  @Override public ImmutableList<E> subList(int fromIndex, int toIndex) {
    Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
    return (fromIndex == toIndex) ? ImmutableList.<E>of() : this;
  }

  @Override public ImmutableList<E> reverse() {
    return this;
  }

  @Override public boolean contains(Object object) {
    return element.equals(object);
  }

  @Override public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (object instanceof List) {
      List<?> that = (List<?>) object;
      return that.size() == 1 && element.equals(that.get(0));
    }
    return false;
  }

  @Override public int hashCode() {
    // not caching hash code since it could change if the element is mutable
    // in a way that modifies its hash code.
    return 31 + element.hashCode();
  }

  @Override public String toString() {
    String elementToString = element.toString();
    return new StringBuilder(elementToString.length() + 2)
        .append('[')
        .append(elementToString)
        .append(']')
        .toString();
  }

  @Override public boolean isEmpty() {
    return false;
  }

  @Override boolean isPartialView() {
    return false;
  }

  @Override public Object[] toArray() {
    return new Object[] { element };
  }

  @Override public <T> T[] toArray(T[] array) {
    if (array.length == 0) {
      array = ObjectArrays.newArray(array, 1);
    } else if (array.length > 1) {
      array[1] = null;
    }
    // Writes will produce ArrayStoreException when the toArray() doc requires.
    Object[] objectArray = array;
    objectArray[0] = element;
    return array;
  }
}
