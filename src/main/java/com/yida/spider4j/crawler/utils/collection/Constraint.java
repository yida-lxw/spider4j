package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A constraint that an element must satisfy in order to be added to a
 * collection. For example, {@link Constraints#notNull()}, which prevents a
 * collection from including any null elements, could be implemented like this:
 * <pre>   {@code
 *
 *   public Object checkElement(Object element) {
 *     if (element == null) {
 *       throw new NullPointerException();
 *     }
 *     return element;
 *   }}</pre>
 *
 * In order to be effective, constraints should be deterministic; that is,
 * they should not depend on state that can change (such as external state,
 * random variables, and time) and should only depend on the value of the
 * passed-in element. A non-deterministic constraint cannot reliably enforce
 * that all the collection's elements meet the constraint, since the constraint
 * is only enforced when elements are added.
 *
 * @see Constraints
 * @see MapConstraint
 * @author Mike Bostock
 * @since 3
 */
@Beta
@GwtCompatible
public interface Constraint<E> {
  /**
   * Throws a suitable {@code RuntimeException} if the specified element is
   * illegal. Typically this is either a {@link NullPointerException}, an
   * {@link IllegalArgumentException}, or a {@link ClassCastException}, though
   * an application-specific exception class may be used if appropriate.
   *
   * @param element the element to check
   * @return the provided element
   */
  E checkElement(E element);

  /**
   * Returns a brief human readable description of this constraint, such as
   * "Not null" or "Positive number".
   */
  String toString();
}
