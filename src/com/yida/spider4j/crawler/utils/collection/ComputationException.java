package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * Wraps an exception that occurred during a computation.
 */
@GwtCompatible
public class ComputationException extends RuntimeException {
  /**
   * Creates a new instance with the given cause.
   */
  public ComputationException(Throwable cause) {
    super(cause);
  }
  private static final long serialVersionUID = 0;
}
