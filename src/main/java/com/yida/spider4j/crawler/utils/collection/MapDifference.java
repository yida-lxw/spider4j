package com.yida.spider4j.crawler.utils.collection;

import java.util.Map;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * An object representing the differences between two maps.
 *
 * @author Kevin Bourrillion
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public interface MapDifference<K, V> {
  /**
   * Returns {@code true} if there are no differences between the two maps;
   * that is, if the maps are equal.
   */
  boolean areEqual();

  /**
   * Returns an unmodifiable map containing the entries from the left map whose
   * keys are not present in the right map.
   */
  Map<K, V> entriesOnlyOnLeft();

  /**
   * Returns an unmodifiable map containing the entries from the right map whose
   * keys are not present in the left map.
   */
  Map<K, V> entriesOnlyOnRight();

  /**
   * Returns an unmodifiable map containing the entries that appear in both
   * maps; that is, the intersection of the two maps.
   */
  Map<K, V> entriesInCommon();

  /**
   * Returns an unmodifiable map describing keys that appear in both maps, but
   * with different values.
   */
  Map<K, ValueDifference<V>> entriesDiffering();

  /**
   * Compares the specified object with this instance for equality. Returns
   * {@code true} if the given object is also a {@code MapDifference} and the
   * values returned by the {@link #entriesOnlyOnLeft()}, {@link
   * #entriesOnlyOnRight()}, {@link #entriesInCommon()} and {@link
   * #entriesDiffering()} of the two instances are equal.
   */
  boolean equals(Object object);

  /**
   * Returns the hash code for this instance. This is defined as the hash code
   * of <pre>   {@code
   *
   *   Arrays.asList(entriesOnlyOnLeft(), entriesOnlyOnRight(),
   *       entriesInCommon(), entriesDiffering())}</pre>
   */
  int hashCode();

  /**
   * A difference between the mappings from two maps with the same key. The
   * {@code leftValue()} and {@code rightValue} are not equal, and one but not
   * both of them may be null.
   *
   * @since 2 (imported from Google Collections Library)
   */
  interface ValueDifference<V> {
    /**
     * Returns the value from the left map (possibly null).
     */
    V leftValue();

    /**
     * Returns the value from the right map (possibly null).
     */
    V rightValue();

    /**
     * Two instances are considered equal if their {@link #leftValue()}
     * values are equal and their {@link #rightValue()} values are also equal.
     */
    @Override boolean equals(Object other);

    /**
     * The hash code equals the value
     * {@code Arrays.asList(leftValue(), rightValue()).hashCode()}.
     */
    @Override int hashCode();
  }

}