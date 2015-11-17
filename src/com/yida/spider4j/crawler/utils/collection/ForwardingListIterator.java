package com.yida.spider4j.crawler.utils.collection;

import java.util.ListIterator;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A list iterator which forwards all its method calls to another list
 * iterator. Subclasses should override one or more methods to modify the
 * behavior of the backing iterator as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Mike Bostock
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public abstract class ForwardingListIterator<E> extends ForwardingIterator<E>
    implements ListIterator<E> {

  /** Constructor for use by subclasses. */
  protected ForwardingListIterator() {}

  @Override protected abstract ListIterator<E> delegate();

  public void add(E element) {
    delegate().add(element);
  }

  public boolean hasPrevious() {
    return delegate().hasPrevious();
  }

  public int nextIndex() {
    return delegate().nextIndex();
  }

  public E previous() {
    return delegate().previous();
  }

  public int previousIndex() {
    return delegate().previousIndex();
  }

  public void set(E element) {
    delegate().set(element);
  }
}
