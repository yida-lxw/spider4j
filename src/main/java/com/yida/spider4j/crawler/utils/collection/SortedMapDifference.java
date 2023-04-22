package com.yida.spider4j.crawler.utils.collection;

import java.util.SortedMap;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * An object representing the differences between two sorted maps.
 *
 * @author Louis Wasserman
 * @since 8
 */
@Beta
@GwtCompatible
public interface SortedMapDifference<K, V> extends MapDifference<K, V> {

  SortedMap<K, V> entriesOnlyOnLeft();

  SortedMap<K, V> entriesOnlyOnRight();

  SortedMap<K, V> entriesInCommon();

  SortedMap<K, ValueDifference<V>> entriesDiffering();
}
