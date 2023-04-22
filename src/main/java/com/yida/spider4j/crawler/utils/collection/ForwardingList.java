package com.yida.spider4j.crawler.utils.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A list which forwards all its method calls to another list. Subclasses should
 * override one or more methods to modify the behavior of the backing list as
 * desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p>This class does not implement {@link java.util.RandomAccess}. If the
 * delegate supports random access, the {@code ForwardingList} subclass should
 * implement the {@code RandomAccess} interface.
 *
 * <p><b>Warning:</b> The methods of {@code ForwardingList} forward
 * <b>indiscriminately</b> to the methods of the delegate. For example,
 * overriding {@link #add} alone <b>will not</b> change the behavior of {@link
 * #addAll}, which can lead to unexpected behavior. In this case, you should
 * override {@code addAll} as well, either providing your own implementation, or
 * delegating to the provided {@code standardAddAll} method.
 *
 * <p>The {@code standard} methods and any collection views they return are not
 * guaranteed to be thread-safe, even when all of the methods that they depend
 * on are thread-safe.
 *
 * @author Mike Bostock
 * @author Louis Wasserman
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public abstract class ForwardingList<E> extends ForwardingCollection<E>
    implements List<E> {
  // TODO(user): identify places where thread safety is actually lost

  /** Constructor for use by subclasses. */
  protected ForwardingList() {}

  @Override protected abstract List<E> delegate();

  public void add(int index, E element) {
    delegate().add(index, element);
  }

  public boolean addAll(int index, Collection<? extends E> elements) {
    return delegate().addAll(index, elements);
  }

  public E get(int index) {
    return delegate().get(index);
  }

  public int indexOf(Object element) {
    return delegate().indexOf(element);
  }

  public int lastIndexOf(Object element) {
    return delegate().lastIndexOf(element);
  }

  public ListIterator<E> listIterator() {
    return delegate().listIterator();
  }

  public ListIterator<E> listIterator(int index) {
    return delegate().listIterator(index);
  }

  public E remove(int index) {
    return delegate().remove(index);
  }

  public E set(int index, E element) {
    return delegate().set(index, element);
  }

  public List<E> subList(int fromIndex, int toIndex) {
    return delegate().subList(fromIndex, toIndex);
  }

  @Override public boolean equals(Object object) {
    return object == this || delegate().equals(object);
  }

  @Override public int hashCode() {
    return delegate().hashCode();
  }

  /**
   * A sensible default implementation of {@link #add(Object)}, in terms of
   * {@link #add(int, Object)}. If you override {@link #add(int, Object)}, you
   * may wish to override {@link #add(Object)} to forward to this
   * implementation.
   *
   * @since 7
   */
  @Beta protected boolean standardAdd(E element){
    add(size(), element);
    return true;
  }

  /**
   * A sensible default implementation of {@link #addAll(int, Collection)}, in
   * terms of the {@code add} method of {@link #listIterator(int)}. If you
   * override {@link #listIterator(int)}, you may wish to override {@link
   * #addAll(int, Collection)} to forward to this implementation.
   *
   * @since 7
   */
  @Beta protected boolean standardAddAll(
      int index, Iterable<? extends E> elements) {
    return Lists.addAllImpl(this, index, elements);
  }

  /**
   * A sensible default implementation of {@link #indexOf}, in terms of {@link
   * #listIterator()}. If you override {@link #listIterator()}, you may wish to
   * override {@link #indexOf} to forward to this implementation.
   *
   * @since 7
   */
  @Beta protected int standardIndexOf(Object element) {
    return Lists.indexOfImpl(this, element);
  }

  /**
   * A sensible default implementation of {@link #lastIndexOf}, in terms of
   * {@link #listIterator(int)}. If you override {@link #listIterator(int)}, you
   * may wish to override {@link #lastIndexOf} to forward to this
   * implementation.
   *
   * @since 7
   */
  @Beta protected int standardLastIndexOf(Object element) {
    return Lists.lastIndexOfImpl(this, element);
  }

  /**
   * A sensible default implementation of {@link #iterator}, in terms of
   * {@link #listIterator()}. If you override {@link #listIterator()}, you may
   * wish to override {@link #iterator} to forward to this implementation.
   *
   * @since 7
   */
  @Beta protected Iterator<E> standardIterator() {
    return listIterator();
  }

  /**
   * A sensible default implementation of {@link #listIterator()}, in terms of
   * {@link #listIterator(int)}. If you override {@link #listIterator(int)}, you
   * may wish to override {@link #listIterator()} to forward to this
   * implementation.
   *
   * @since 7
   */
  @Beta protected ListIterator<E> standardListIterator(){
    return listIterator(0);
  }

  /**
   * A sensible default implementation of {@link #listIterator(int)}, in terms
   * of {@link #size} and {@link #get(int)}. If you override either of these
   * methods you may wish to override {@link #listIterator(int)} to forward to
   * this implementation.
   *
   * @since 7
   */
  @Beta protected ListIterator<E> standardListIterator(int start) {
    return Lists.listIteratorImpl(this, start);
  }

  /**
   * A sensible default implementation of {@link #subList(int, int)}. If you
   * override any other methods, you may wish to override {@link #subList(int,
   * int)} to forward to this implementation.
   *
   * @since 7
   */
  @Beta protected List<E> standardSubList(int fromIndex, int toIndex) {
    return Lists.subListImpl(this, fromIndex, toIndex);
  }

  /**
   * A sensible definition of {@link #equals(Object)} in terms of {@link #size}
   * and {@link #iterator}. If you override either of those methods, you may
   * wish to override {@link #equals(Object)} to forward to this implementation.
   *
   * @since 7
   */
  @Beta protected boolean standardEquals(Object object) {
    return Lists.equalsImpl(this, object);
  }

  /**
   * A sensible definition of {@link #hashCode} in terms of {@link #iterator}.
   * If you override {@link #iterator}, you may wish to override {@link
   * #hashCode} to forward to this implementation.
   *
   * @since 7
   */
  @Beta protected int standardHashCode() {
    return Lists.hashCodeImpl(this);
  }
}
