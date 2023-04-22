package com.yida.spider4j.crawler.utils.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * An empty immutable sorted set.
 *
 * @author Jared Levy
 */
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings("serial") // uses writeReplace(), not default serialization
class EmptyImmutableSortedSet<E> extends ImmutableSortedSet<E> {
  EmptyImmutableSortedSet(Comparator<? super E> comparator) {
    super(comparator);
  }

  public int size() {
    return 0;
  }

  @Override public boolean isEmpty() {
    return true;
  }

  @Override public boolean contains(Object target) {
    return false;
  }

  @Override public UnmodifiableIterator<E> iterator() {
    return Iterators.emptyIterator();
  }

  @Override boolean isPartialView() {
    return false;
  }

  private static final Object[] EMPTY_ARRAY = new Object[0];

  @Override public Object[] toArray() {
    return EMPTY_ARRAY;
  }

  @Override public <T> T[] toArray(T[] a) {
    if (a.length > 0) {
      a[0] = null;
    }
    return a;
  }

  @Override public boolean containsAll(Collection<?> targets) {
    return targets.isEmpty();
  }

  @Override public boolean equals(Object object) {
    if (object instanceof Set) {
      Set<?> that = (Set<?>) object;
      return that.isEmpty();
    }
    return false;
  }

  @Override public int hashCode() {
    return 0;
  }

  @Override public String toString() {
    return "[]";
  }

  public E first() {
    throw new NoSuchElementException();
  }

  public E last() {
    throw new NoSuchElementException();
  }

  @Override ImmutableSortedSet<E> headSetImpl(E toElement) {
    return this;
  }

  @Override ImmutableSortedSet<E> subSetImpl(E fromElement, E toElement) {
    return this;
  }

  @Override ImmutableSortedSet<E> tailSetImpl(E fromElement) {
    return this;
  }

  @Override int indexOf(Object target) {
    return -1;
  }
}
