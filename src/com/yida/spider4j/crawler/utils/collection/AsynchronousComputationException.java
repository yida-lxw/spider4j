package com.yida.spider4j.crawler.utils.collection;


/**
 * Wraps an exception that occurred during a computation in a different thread.
 *
 * @author Bob Lee
 */
public class AsynchronousComputationException extends ComputationException {
  /**
   * Creates a new instance with the given cause.
   */
  public AsynchronousComputationException(Throwable cause) {
    super(cause);
  }
  private static final long serialVersionUID = 0;
}
