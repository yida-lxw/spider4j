package com.yida.spider4j.crawler.utils.collection.base;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
/**
 * A class that can supply objects of a single type.  Semantically, this could
 * be a factory, generator, builder, closure, or something else entirely. No
 * guarantees are implied by this interface.
 *
 * @author Harry Heymann
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public interface Supplier<T> {
  /**
   * Retrieves an instance of the appropriate type. The returned object may or
   * may not be a new instance, depending on the implementation.
   *
   * @return an instance of the appropriate type
   */
  T get();
}
