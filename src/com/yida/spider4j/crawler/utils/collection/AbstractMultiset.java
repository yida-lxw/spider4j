package com.yida.spider4j.crawler.utils.collection;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Objects;



/**
 * This class provides a skeletal implementation of the {@link Multiset}
 * interface. A new multiset implementation can be created easily by extending
 * this class and implementing the {@link Multiset#entrySet()} method, plus
 * optionally overriding {@link #add(Object, int)} and
 * {@link #remove(Object, int)} to enable modifications to the multiset.
 *
 * <p>The {@link #count} and {@link #size} implementations all iterate across
 * the set returned by {@link Multiset#entrySet()}, as do many methods acting on
 * the set returned by {@link #elementSet()}. Override those methods for better
 * performance.
 *
 * @author Kevin Bourrillion
 * @author Louis Wasserman
 */
@GwtCompatible
abstract class AbstractMultiset<E> extends AbstractCollection<E>
    implements Multiset<E> {
  public abstract Set<Entry<E>> entrySet();

  // Query Operations

  @Override public int size() {
    return Multisets.sizeImpl(this);
  }

  @Override public boolean isEmpty() {
    return entrySet().isEmpty();
  }

  @Override public boolean contains(Object element) {
    return count(element) > 0;
  }

  @Override public Iterator<E> iterator() {
    return Multisets.iteratorImpl(this);
  }

  public int count(Object element) {
    for (Entry<E> entry : entrySet()) {
      if (Objects.equal(entry.getElement(), element)) {
        return entry.getCount();
      }
    }
    return 0;
  }

  // Modification Operations

  @Override public boolean add(E element) {
    add(element, 1);
    return true;
  }

  public int add(E element, int occurrences) {
    throw new UnsupportedOperationException();
  }

  @Override public boolean remove(Object element) {
    return remove(element, 1) > 0;
  }

  public int remove(Object element, int occurrences) {
    throw new UnsupportedOperationException();
  }

  public int setCount(E element, int count) {
    return Multisets.setCountImpl(this, element, count);
  }

  public boolean setCount(E element, int oldCount, int newCount) {
    return Multisets.setCountImpl(this, element, oldCount, newCount);
  }

  // Bulk Operations

  @Override public boolean addAll(Collection<? extends E> elementsToAdd) {
    return Multisets.addAllImpl(this, elementsToAdd);
  }

  @Override public boolean removeAll(Collection<?> elementsToRemove) {
    return Multisets.removeAllImpl(this, elementsToRemove);
  }

  @Override public boolean retainAll(Collection<?> elementsToRetain) {
    return Multisets.retainAllImpl(this, elementsToRetain);
  }

  @Override public void clear() {
    entrySet().clear();
  }

  // Views

  private transient Set<E> elementSet;

  public Set<E> elementSet() {
    Set<E> result = elementSet;
    if (result == null) {
      elementSet = result = createElementSet();
    }
    return result;
  }

  /**
   * Creates a new instance of this multiset's element set, which will be
   * returned by {@link #elementSet()}.
   */
  Set<E> createElementSet() {
    return Multisets.elementSetImpl(this);
  }

  // Object methods

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns {@code true} if {@code other} is a multiset
   * of the same size and if, for each element, the two multisets have the same
   * count.
   */
  @Override public boolean equals(Object object) {
    return Multisets.equalsImpl(this, object);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns the hash code of {@link
   * Multiset#entrySet()}.
   */
  @Override public int hashCode() {
    return entrySet().hashCode();
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns the result of invoking {@code toString} on
   * {@link Multiset#entrySet()}.
   */
  @Override public String toString() {
    return entrySet().toString();
  }
}
