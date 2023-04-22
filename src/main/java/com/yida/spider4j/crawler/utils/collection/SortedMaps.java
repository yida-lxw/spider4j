package com.yida.spider4j.crawler.utils.collection;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import com.yida.spider4j.crawler.utils.collection.MapDifference.ValueDifference;
import com.yida.spider4j.crawler.utils.collection.Maps.EntryTransformer;
import com.yida.spider4j.crawler.utils.collection.Maps.MapDifferenceImpl;
import com.yida.spider4j.crawler.utils.collection.Maps.TransformedEntriesMap;
import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;
import com.yida.spider4j.crawler.utils.collection.base.Function;
import com.yida.spider4j.crawler.utils.collection.base.Objects;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.base.Predicates;



/**
 * Static utility methods pertaining to {@link SortedMap} instances.
 *
 * @author Louis Wasserman
 * @since 8
 */
@Beta
@GwtCompatible
public final class SortedMaps {
  private SortedMaps() {}

  /**
   * Returns a view of a sorted map where each value is transformed by a
   * function. All other properties of the map, such as iteration order, are
   * left intact. For example, the code: <pre>   {@code
   *
   *   SortedMap<String, Integer> map = ImmutableSortedMap.of("a", 4, "b", 9);
   *   Function<Integer, Double> sqrt =
   *       new Function<Integer, Double>() {
   *         public Double apply(Integer in) {
   *           return Math.sqrt((int) in);
   *         }
   *       };
   *   SortedMap<String, Double> transformed =
   *        Maps.transformSortedValues(map, sqrt);
   *   System.out.println(transformed);}</pre>
   *
   * ... prints {@code {a=2.0, b=3.0}}.
   *
   * <p>Changes in the underlying map are reflected in this view. Conversely,
   * this view supports removal operations, and these are reflected in the
   * underlying map.
   *
   * <p>It's acceptable for the underlying map to contain null keys, and even
   * null values provided that the function is capable of accepting null input.
   * The transformed map might contain null values, if the function sometimes
   * gives a null result.
   *
   * <p>The returned map is not thread-safe or serializable, even if the
   * underlying map is.
   *
   * <p>The function is applied lazily, invoked when needed. This is necessary
   * for the returned map to be a view, but it means that the function will be
   * applied many times for bulk operations like {@link Map#containsValue} and
   * {@code Map.toString()}. For this to perform well, {@code function} should
   * be fast. To avoid lazy evaluation when the returned map doesn't need to be
   * a view, copy the returned map into a new map of your choosing.
   */
  public static <K, V1, V2> SortedMap<K, V2> transformValues(
      SortedMap<K, V1> fromMap, final Function<? super V1, V2> function) {
    Preconditions.checkNotNull(function);
    EntryTransformer<K, V1, V2> transformer =
        new EntryTransformer<K, V1, V2>() {
          public V2 transformEntry(K key, V1 value) {
            return function.apply(value);
          }
        };
    return transformEntries(fromMap, transformer);
  }

  /**
   * Returns a view of a sorted map whose values are derived from the original
   * sorted map's entries. In contrast to {@link #transformValues}, this
   * method's entry-transformation logic may depend on the key as well as the
   * value.
   *
   * <p>All other properties of the transformed map, such as iteration order,
   * are left intact. For example, the code: <pre>   {@code
   *
   *   Map<String, Boolean> options =
   *       ImmutableSortedMap.of("verbose", true, "sort", false);
   *   EntryTransformer<String, Boolean, String> flagPrefixer =
   *       new EntryTransformer<String, Boolean, String>() {
   *         public String transformEntry(String key, Boolean value) {
   *           return value ? key : "yes" + key;
   *         }
   *       };
   *   SortedMap<String, String> transformed =
   *       LabsMaps.transformSortedEntries(options, flagPrefixer);
   *   System.out.println(transformed);}</pre>
   *
   * ... prints {@code {sort=yessort, verbose=verbose}}.
   *
   * <p>Changes in the underlying map are reflected in this view. Conversely,
   * this view supports removal operations, and these are reflected in the
   * underlying map.
   *
   * <p>It's acceptable for the underlying map to contain null keys and null
   * values provided that the transformer is capable of accepting null inputs.
   * The transformed map might contain null values if the transformer sometimes
   * gives a null result.
   *
   * <p>The returned map is not thread-safe or serializable, even if the
   * underlying map is.
   *
   * <p>The transformer is applied lazily, invoked when needed. This is
   * necessary for the returned map to be a view, but it means that the
   * transformer will be applied many times for bulk operations like {@link
   * Map#containsValue} and {@link Object#toString}. For this to perform well,
   * {@code transformer} should be fast. To avoid lazy evaluation when the
   * returned map doesn't need to be a view, copy the returned map into a new
   * map of your choosing.
   *
   * <p><b>Warning:</b> This method assumes that for any instance {@code k} of
   * {@code EntryTransformer} key type {@code K}, {@code k.equals(k2)} implies
   * that {@code k2} is also of type {@code K}. Using an {@code
   * EntryTransformer} key type for which this may not hold, such as {@code
   * ArrayList}, may risk a {@code ClassCastException} when calling methods on
   * the transformed map.
   */
  public static <K, V1, V2> SortedMap<K, V2> transformEntries(
      final SortedMap<K, V1> fromMap,
      EntryTransformer<? super K, ? super V1, V2> transformer) {
    return new TransformedEntriesSortedMap<K, V1, V2>(fromMap, transformer);
  }

  static class TransformedEntriesSortedMap<K, V1, V2>
      extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {

    protected SortedMap<K, V1> fromMap() {
      return (SortedMap<K, V1>) fromMap;
    }

    TransformedEntriesSortedMap(SortedMap<K, V1> fromMap,
        EntryTransformer<? super K, ? super V1, V2> transformer) {
      super(fromMap, transformer);
    }

    @Override public Comparator<? super K> comparator() {
      return fromMap().comparator();
    }

    @Override public K firstKey() {
      return fromMap().firstKey();
    }

    @Override public SortedMap<K, V2> headMap(K toKey) {
      return transformEntries(fromMap().headMap(toKey), transformer);
    }

    @Override public K lastKey() {
      return fromMap().lastKey();
    }

    @Override public SortedMap<K, V2> subMap(K fromKey, K toKey) {
      return transformEntries(
          fromMap().subMap(fromKey, toKey), transformer);
    }

    @Override public SortedMap<K, V2> tailMap(K fromKey) {
      return transformEntries(fromMap().tailMap(fromKey), transformer);
    }

  }

  /**
   * Computes the difference between two sorted maps, using the comparator of
   * the left map, or {@code Ordering.natural()} if the left map uses the
   * natural ordering of its elements. This difference is an immutable snapshot
   * of the state of the maps at the time this method is called. It will never
   * change, even if the maps change at a later time.
   *
   * <p>Since this method uses {@code TreeMap} instances internally, the keys of
   * the right map must all compare as distinct according to the comparator
   * of the left map.
   *
   * <p><b>Note:</b>If you only need to know whether two sorted maps have the
   * same mappings, call {@code left.equals(right)} instead of this method.
   *
   * @param left the map to treat as the "left" map for purposes of comparison
   * @param right the map to treat as the "right" map for purposes of comparison
   * @return the difference between the two maps
   */
  public static <K, V> SortedMapDifference<K, V> difference(
      SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
    Comparator<? super K> comparator = orNaturalOrder(left.comparator());
    SortedMap<K, V> onlyOnLeft = Maps.newTreeMap(comparator);
    SortedMap<K, V> onlyOnRight = Maps.newTreeMap(comparator);
    onlyOnRight.putAll(right); // will whittle it down
    SortedMap<K, V> onBoth = Maps.newTreeMap(comparator);
    SortedMap<K, MapDifference.ValueDifference<V>> differences =
        Maps.newTreeMap(comparator);
    boolean eq = true;

    for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
      K leftKey = entry.getKey();
      V leftValue = entry.getValue();
      if (right.containsKey(leftKey)) {
        V rightValue = onlyOnRight.remove(leftKey);
        if (Objects.equal(leftValue, rightValue)) {
          onBoth.put(leftKey, leftValue);
        } else {
          eq = false;
          differences.put(
              leftKey, new Maps.ValueDifferenceImpl<V>(leftValue, rightValue));
        }
      } else {
        eq = false;
        onlyOnLeft.put(leftKey, leftValue);
      }
    }

    boolean areEqual = eq && onlyOnRight.isEmpty();
    return sortedMapDifference(
        areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
  }

  private static <K, V> SortedMapDifference<K, V> sortedMapDifference(
      boolean areEqual, SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight,
      SortedMap<K, V> onBoth, SortedMap<K, ValueDifference<V>> differences) {
    return new SortedMapDifferenceImpl<K, V>(areEqual,
        Collections.unmodifiableSortedMap(onlyOnLeft),
        Collections.unmodifiableSortedMap(onlyOnRight),
        Collections.unmodifiableSortedMap(onBoth),
        Collections.unmodifiableSortedMap(differences));
  }

  static class SortedMapDifferenceImpl<K, V> extends MapDifferenceImpl<K, V>
      implements SortedMapDifference<K, V> {
    SortedMapDifferenceImpl(boolean areEqual, SortedMap<K, V> onlyOnLeft,
        SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth,
        SortedMap<K, ValueDifference<V>> differences) {
      super(areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
    }

    @Override public SortedMap<K, ValueDifference<V>> entriesDiffering() {
      return (SortedMap<K, ValueDifference<V>>) super.entriesDiffering();
    }

    @Override public SortedMap<K, V> entriesInCommon() {
      return (SortedMap<K, V>) super.entriesInCommon();
    }

    @Override public SortedMap<K, V> entriesOnlyOnLeft() {
      return (SortedMap<K, V>) super.entriesOnlyOnLeft();
    }

    @Override public SortedMap<K, V> entriesOnlyOnRight() {
      return (SortedMap<K, V>) super.entriesOnlyOnRight();
    }
  }

  /**
   * Returns the specified comparator if not null; otherwise returns {@code
   * Ordering.natural()}. This method is an abomination of generics; the only
   * purpose of this method is to contain the ugly type-casting in one place.
   */
  @SuppressWarnings("unchecked")
  static <E> Comparator<? super E> orNaturalOrder(
      Comparator<? super E> comparator) {
    if (comparator != null) { // can't use ? : because of javac bug 5080917
      return comparator;
    }
    return (Comparator<E>) Ordering.natural();
  }
  
  /**
   * Returns a sorted map containing the mappings in {@code unfiltered} whose
   * keys satisfy a predicate. The returned map is a live view of {@code
   * unfiltered}; changes to one affect the other.
   *
   * <p>The resulting map's {@code keySet()}, {@code entrySet()}, and {@code
   * values()} views have iterators that don't support {@code remove()}, but all
   * other methods are supported by the map and its views. When given a key that
   * doesn't satisfy the predicate, the map's {@code put()} and {@code putAll()}
   * methods throw an {@link IllegalArgumentException}.
   *
   * <p>When methods such as {@code removeAll()} and {@code clear()} are called
   * on the filtered map or its views, only mappings whose keys satisfy the
   * filter will be removed from the underlying map.
   *
   * <p>The returned map isn't threadsafe or serializable, even if {@code
   * unfiltered} is.
   *
   * <p>Many of the filtered map's methods, such as {@code size()},
   * iterate across every key/value mapping in the underlying map and determine
   * which satisfy the filter. When a live view is <i>not</i> needed, it may be
   * faster to copy the filtered map and use the copy.
   *
   * <p><b>Warning:</b> {@code keyPredicate} must be <i>consistent with
   * equals</i>, as documented at {@link Predicate#apply}. Do not provide a
   * predicate such as {@code Predicates.instanceOf(ArrayList.class)}, which is
   * inconsistent with equals.
   */
  @GwtIncompatible("untested")
  public static <K, V> SortedMap<K, V> filterKeys(
      SortedMap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
    // TODO: Return a subclass of Maps.FilteredKeyMap for slightly better
    // performance.
    Preconditions.checkNotNull(keyPredicate);
    Predicate<Entry<K, V>> entryPredicate = new Predicate<Entry<K, V>>() {
      public boolean apply(Entry<K, V> input) {
        return keyPredicate.apply(input.getKey());
      }
    };
    return filterEntries(unfiltered, entryPredicate);
  }
    
  /**
   * Returns a sorted map containing the mappings in {@code unfiltered} whose
   * values satisfy a predicate. The returned map is a live view of {@code
   * unfiltered}; changes to one affect the other.
   *
   * <p>The resulting map's {@code keySet()}, {@code entrySet()}, and {@code
   * values()} views have iterators that don't support {@code remove()}, but all
   * other methods are supported by the map and its views. When given a value
   * that doesn't satisfy the predicate, the map's {@code put()}, {@code
   * putAll()}, and {@link Entry#setValue} methods throw an {@link 
   * IllegalArgumentException}.
   *
   * <p>When methods such as {@code removeAll()} and {@code clear()} are called
   * on the filtered map or its views, only mappings whose values satisfy the
   * filter will be removed from the underlying map.
   *
   * <p>The returned map isn't threadsafe or serializable, even if {@code
   * unfiltered} is.
   *
   * <p>Many of the filtered map's methods, such as {@code size()},
   * iterate across every key/value mapping in the underlying map and determine
   * which satisfy the filter. When a live view is <i>not</i> needed, it may be
   * faster to copy the filtered map and use the copy.
   *
   * <p><b>Warning:</b> {@code valuePredicate} must be <i>consistent with
   * equals</i>, as documented at {@link Predicate#apply}. Do not provide a
   * predicate such as {@code Predicates.instanceOf(ArrayList.class)}, which is
   * inconsistent with equals.
   */
  @GwtIncompatible("untested")
  public static <K, V> SortedMap<K, V> filterValues(
      SortedMap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
    Preconditions.checkNotNull(valuePredicate);
    Predicate<Entry<K, V>> entryPredicate =
        new Predicate<Entry<K, V>>() {
          public boolean apply(Entry<K, V> input) {
            return valuePredicate.apply(input.getValue());
          }
        };
    return filterEntries(unfiltered, entryPredicate);
  }

  /**
   * Returns a sorted map containing the mappings in {@code unfiltered} that
   * satisfy a predicate. The returned map is a live view of {@code unfiltered};
   * changes to one affect the other.
   *
   * <p>The resulting map's {@code keySet()}, {@code entrySet()}, and {@code
   * values()} views have iterators that don't support {@code remove()}, but all
   * other methods are supported by the map and its views. When given a
   * key/value pair that doesn't satisfy the predicate, the map's {@code put()}
   * and {@code putAll()} methods throw an {@link IllegalArgumentException}.
   * Similarly, the map's entries have a {@link Entry#setValue} method that
   * throws an {@link IllegalArgumentException} when the existing key and the
   * provided value don't satisfy the predicate.
   *
   * <p>When methods such as {@code removeAll()} and {@code clear()} are called
   * on the filtered map or its views, only mappings that satisfy the filter
   * will be removed from the underlying map.
   *
   * <p>The returned map isn't threadsafe or serializable, even if {@code
   * unfiltered} is.
   *
   * <p>Many of the filtered map's methods, such as {@code size()},
   * iterate across every key/value mapping in the underlying map and determine
   * which satisfy the filter. When a live view is <i>not</i> needed, it may be
   * faster to copy the filtered map and use the copy.
   *
   * <p><b>Warning:</b> {@code entryPredicate} must be <i>consistent with
   * equals</i>, as documented at {@link Predicate#apply}.
   */
  @GwtIncompatible("untested")
  public static <K, V> SortedMap<K, V> filterEntries(
      SortedMap<K, V> unfiltered, 
      Predicate<? super Entry<K, V>> entryPredicate) {
    Preconditions.checkNotNull(entryPredicate);
    return (unfiltered instanceof FilteredSortedMap)
        ? filterFiltered((FilteredSortedMap<K, V>) unfiltered, entryPredicate)
        : new FilteredSortedMap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
  }

  /**
   * Support {@code clear()}, {@code removeAll()}, and {@code retainAll()} when
   * filtering a filtered sorted map.
   */
  private static <K, V> SortedMap<K, V> filterFiltered(
      FilteredSortedMap<K, V> map, 
      Predicate<? super Entry<K, V>> entryPredicate) {
    Predicate<Entry<K, V>> predicate
        = Predicates.and(map.predicate, entryPredicate);
    return new FilteredSortedMap<K, V>(map.sortedMap(), predicate);
  }
  
  private static class FilteredSortedMap<K, V>
      extends Maps.FilteredEntryMap<K, V> implements SortedMap<K, V> {

    FilteredSortedMap(SortedMap<K, V> unfiltered,
        Predicate<? super Entry<K, V>> entryPredicate) {
      super(unfiltered, entryPredicate);
    }

    SortedMap<K, V> sortedMap() {
      return (SortedMap<K, V>) unfiltered;
    }
    
    @Override public Comparator<? super K> comparator() {
      return sortedMap().comparator();
    }

    @Override public K firstKey() {
      // correctly throws NoSuchElementException when filtered map is empty.
      return keySet().iterator().next();
    }

    @Override public K lastKey() {
      SortedMap<K, V> headMap = sortedMap();
      while (true) {
        // correctly throws NoSuchElementException when filtered map is empty.
        K key = headMap.lastKey();
        if (apply(key, unfiltered.get(key))) {
          return key;
        }
        headMap = sortedMap().headMap(key);
      }
    }

    @Override public SortedMap<K, V> headMap(K toKey) {
      return new FilteredSortedMap<K, V>(sortedMap().headMap(toKey), predicate);
    }

    @Override public SortedMap<K, V> subMap(K fromKey, K toKey) {
      return new FilteredSortedMap<K, V>(
          sortedMap().subMap(fromKey, toKey), predicate);
    }

    @Override public SortedMap<K, V> tailMap(K fromKey) {
      return new FilteredSortedMap<K, V>(
          sortedMap().tailMap(fromKey), predicate);
    }
  }
}
