package com.yida.spider4j.crawler.utils.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * An iterator that supports a one-element lookahead while iterating.
 *
 * @author Mick Killianey
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public interface PeekingIterator<E> extends Iterator<E> {
  /**
   * Returns the next element in the iteration, without advancing the iteration.
   *
   * <p>Calls to {@code peek()} should not change the state of the iteration,
   * except that it <i>may</i> prevent removal of the most recent element via
   * {@link #remove()}.
   *
   * @throws NoSuchElementException if the iteration has no more elements
   *     according to {@link #hasNext()}
   */
  E peek();

  /**
   * {@inheritDoc}
   *
   * <p>The objects returned by consecutive calls to {@link #peek()} then {@link
   * #next()} are guaranteed to be equal to each other.
   */
  E next();

  /**
   * {@inheritDoc}
   *
   * <p>Implementations may or may not support removal when a call to {@link
   * #peek()} has occurred since the most recent call to {@link #next()}.
   *
   * @throws IllegalStateException if there has been a call to {@link #peek()}
   *     since the most recent call to {@link #next()} and this implementation
   *     does not support this sequence of calls (optional)
   */
  void remove();
}
