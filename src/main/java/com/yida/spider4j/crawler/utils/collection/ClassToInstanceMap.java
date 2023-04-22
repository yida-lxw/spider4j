package com.yida.spider4j.crawler.utils.collection;

import java.util.Map;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A map, each entry of which maps a Java
 * <a href="http://tinyurl.com/2cmwkz">raw type</a> to an instance of that type.
 * In addition to implementing {@code Map}, the additional type-safe operations
 * {@link #putInstance} and {@link #getInstance} are available.
 *
 * <p>Like any other {@code Map<Class, Object>}, this map may contain entries
 * for primitive types, and a primitive type and its corresponding wrapper type
 * may map to different values.
 *
 * @param <B> the common supertype that all entries must share; often this is
 *     simply {@link Object}
 *
 * @author Kevin Bourrillion
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public interface ClassToInstanceMap<B> extends Map<Class<? extends B>, B> {
  /**
   * Returns the value the specified class is mapped to, or {@code null} if no
   * entry for this class is present. This will only return a value that was
   * bound to this specific class, not a value that may have been bound to a
   * subtype.
   */
  <T extends B> T getInstance(Class<T> type);

  /**
   * Maps the specified class to the specified value. Does <i>not</i> associate
   * this value with any of the class's supertypes.
   *
   * @return the value previously associated with this class (possibly {@code
   *     null}), or {@code null} if there was no previous entry.
   */
  <T extends B> T putInstance(Class<T> type, T value);
}
