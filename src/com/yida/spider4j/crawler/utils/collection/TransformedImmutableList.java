package com.yida.spider4j.crawler.utils.collection;

import java.util.List;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * A transforming wrapper around an ImmutableList. For internal use only. {@link
 * #transform(Object)} must be functional.
 *
 * @author Louis Wasserman
 */
@GwtCompatible
@SuppressWarnings("serial") // uses writeReplace(), not default serialization
abstract class TransformedImmutableList<D, E> extends ImmutableList<E> {
  private class TransformedView extends TransformedImmutableList<D, E> {
    TransformedView(ImmutableList<D> backingList) {
      super(backingList);
    }

    @Override E transform(D d) {
      return TransformedImmutableList.this.transform(d);
    }
  }

  private transient final ImmutableList<D> backingList;

  TransformedImmutableList(ImmutableList<D> backingList) {
    this.backingList = Preconditions.checkNotNull(backingList);
  }

  abstract E transform(D d);

  @Override public int indexOf(Object object) {
    if (object == null) {
      return -1;
    }
    for (int i = 0; i < size(); i++) {
      if (get(i).equals(object)) {
        return i;
      }
    }
    return -1;
  }

  @Override public int lastIndexOf(Object object) {
    if (object == null) {
      return -1;
    }
    for (int i = size() - 1; i >= 0; i--) {
      if (get(i).equals(object)) {
        return i;
      }
    }
    return -1;
  }

  @Override public E get(int index) {
    return transform(backingList.get(index));
  }

  @Override public UnmodifiableListIterator<E> listIterator(int index) {
    return new AbstractIndexedListIterator<E>(size(), index) {
      @Override protected E get(int index) {
        return TransformedImmutableList.this.get(index);
      }
    };
  }

  @Override public int size() {
    return backingList.size();
  }

  @Override public ImmutableList<E> subList(int fromIndex, int toIndex) {
    return new TransformedView(backingList.subList(fromIndex, toIndex));
  }

  @Override public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof List) {
      List<?> list = (List<?>) obj;
      return size() == list.size()
          && Iterators.elementsEqual(iterator(), list.iterator());
    }
    return false;
  }

  @Override public int hashCode() {
    int hashCode = 1;
    for (E e : this) {
      hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
    }
    return hashCode;
  }

  @Override public Object[] toArray() {
    return ObjectArrays.toArrayImpl(this);
  }

  @Override public <T> T[] toArray(T[] array) {
    return ObjectArrays.toArrayImpl(this, array);
  }

  @Override boolean isPartialView() {
    return true;
  }
}
