package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A constraint on the keys and values that may be added to a {@code Map} or
 * {@code Multimap}. For example, {@link MapConstraints#notNull()}, which
 * prevents a map from including any null keys or values, could be implemented
 * like this: <pre>   {@code
 *
 *   public void checkKeyValue(Object key, Object value) {
 *     if (key == null || value == null) {
 *       throw new NullPointerException();
 *     }
 *   }}</pre>
 *
 * In order to be effective, constraints should be deterministic; that is, they
 * should not depend on state that can change (such as external state, random
 * variables, and time) and should only depend on the value of the passed-in key
 * and value. A non-deterministic constraint cannot reliably enforce that all
 * the collection's elements meet the constraint, since the constraint is only
 * enforced when elements are added.
 *
 * @author Mike Bostock
 * @see MapConstraints
 * @see Constraint
 * @since 3
 */
@GwtCompatible
@Beta
public interface MapConstraint<K, V> {
  /**
   * Throws a suitable {@code RuntimeException} if the specified key or value is
   * illegal. Typically this is either a {@link NullPointerException}, an
   * {@link IllegalArgumentException}, or a {@link ClassCastException}, though
   * an application-specific exception class may be used if appropriate.
   */
  void checkKeyValue(K key, V value);

  /**
   * Returns a brief human readable description of this constraint, such as
   * "Not null".
   */
  String toString();
}
