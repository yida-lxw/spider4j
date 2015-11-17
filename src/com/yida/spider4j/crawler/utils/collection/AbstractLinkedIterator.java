package com.yida.spider4j.crawler.utils.collection;

import java.util.NoSuchElementException;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * This class provides a skeletal implementation of the {@code Iterator}
 * interface for sequences whose next element can always be derived from the
 * previous element. Null elements are not supported, nor is the
 * {@link #remove()} method.
 *
 * @author Chris Povirk
 * @since 8
 */
@Beta
@GwtCompatible
public abstract class AbstractLinkedIterator<T>
    extends UnmodifiableIterator<T> {
  private T nextOrNull;

  /**
   * Creates a new iterator with the given first element, or, if {@code
   * firstOrNull} is null, creates a new empty iterator.
   */
  protected AbstractLinkedIterator(T firstOrNull) {
    this.nextOrNull = firstOrNull;
  }

  /**
   * Returns the element that follows {@code previous}, or returns {@code null}
   * if no elements remain. This method is invoked during each call to
   * {@link #next()} in order to compute the result of a <i>future</i> call to
   * {@code next()}.
   */
  protected abstract T computeNext(T previous);

  @Override
  public final boolean hasNext() {
    return nextOrNull != null;
  }

  @Override
  public final T next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    try {
      return nextOrNull;
    } finally {
      nextOrNull = computeNext(nextOrNull);
    }
  }
}
