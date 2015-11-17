package com.yida.spider4j.crawler.utils.collection;

import java.util.Iterator;

/**
 * An iterator that does not support {@link #remove}.
 *
 * @author Jared Levy
 * @since 2 (imported from Google Collections Library)
 */
public abstract class UnmodifiableIterator<E> implements Iterator<E> {
  /** Constructor for use by subclasses. */
  protected UnmodifiableIterator() {}
  
  /**
   * Guaranteed to throw an exception and leave the underlying data unmodified.
   *
   * @throws UnsupportedOperationException always
   */
  public final void remove() {
    throw new UnsupportedOperationException();
  }
}
