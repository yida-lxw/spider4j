package com.yida.spider4j.crawler.utils.collection.base;

/**
 * Implemented by references that have code to run after garbage collection of their referents.
 *
 * @see FinalizableReferenceQueue
 * @author Bob Lee
 * @since 2 (imported from Google Collections Library)
 */
public interface FinalizableReference {
  /**
   * Invoked on a background thread after the referent has been garbage collected unless security
   * restrictions prevented starting a background thread, in which case this method is invoked when
   * new references are created.
   */
  void finalizeReferent();
}
