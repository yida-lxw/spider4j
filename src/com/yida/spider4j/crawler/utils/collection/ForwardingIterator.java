package com.yida.spider4j.crawler.utils.collection;

import java.util.Iterator;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * An iterator which forwards all its method calls to another iterator.
 * Subclasses should override one or more methods to modify the behavior of the
 * backing iterator as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @author Kevin Bourrillion
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public abstract class ForwardingIterator<T>
    extends ForwardingObject implements Iterator<T> {

  /** Constructor for use by subclasses. */
  protected ForwardingIterator() {}

  @Override protected abstract Iterator<T> delegate();

  public boolean hasNext() {
    return delegate().hasNext();
  }

  public T next() {
    return delegate().next();
  }

  public void remove() {
    delegate().remove();
  }
}
