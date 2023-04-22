package com.yida.spider4j.crawler.utils.collection;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.base.Supplier;

/**
 * Implementation of {@code Table} whose iteration ordering across row keys is
 * sorted by their natural ordering or by a supplied comparator. Note that
 * iterations across the columns keys for a single row key may or may not be
 * ordered, depending on the implementation. When rows and columns are both
 * sorted, it's easier to use the {@link TreeBasedTable} subclass.
 *
 * <p>The {@link #rowKeySet} method returns a {@link SortedSet} and the {@link
 * #rowMap} method returns a {@link SortedMap}, instead of the {@link Set} and
 * {@link Map} specified by the {@link Table} interface.
 *
 * <p>Null keys and values are not supported.
 *
 * <p>See the {@link StandardTable} superclass for more information about the
 * behavior of this class.
 *
 * @author Jared Levy
 */
@GwtCompatible
class StandardRowSortedTable<R, C, V> extends StandardTable<R, C, V>
    implements RowSortedTable<R, C, V> {
  /*
   * TODO(jlevy): Consider adding headTable, tailTable, and subTable methods,
   * which return a Table view with rows keys in a given range. Create a
   * RowSortedTable subinterface with the revised methods?
   */

  StandardRowSortedTable(SortedMap<R, Map<C, V>> backingMap,
      Supplier<? extends Map<C, V>> factory) {
    super(backingMap, factory);
  }

  private SortedMap<R, Map<C, V>> sortedBackingMap() {
    return (SortedMap<R, Map<C, V>>) backingMap;
  }

  private transient SortedSet<R> rowKeySet;

  /**
   * {@inheritDoc}
   *
   * <p>This method returns a {@link SortedSet}, instead of the {@code Set}
   * specified in the {@link Table} interface.
   */
  @Override public SortedSet<R> rowKeySet() {
    SortedSet<R> result = rowKeySet;
    return (result == null) ? rowKeySet = new RowKeySortedSet() : result;
  }

  private class RowKeySortedSet extends RowKeySet implements SortedSet<R> {
    public Comparator<? super R> comparator() {
      return sortedBackingMap().comparator();
    }

    public R first() {
      return sortedBackingMap().firstKey();
    }

    public R last() {
      return sortedBackingMap().lastKey();
    }

    public SortedSet<R> headSet(R toElement) {
      Preconditions.checkNotNull(toElement);
      return new StandardRowSortedTable<R, C, V>(
          sortedBackingMap().headMap(toElement), factory).rowKeySet();
    }

    public SortedSet<R> subSet(R fromElement, R toElement) {
      Preconditions.checkNotNull(fromElement);
      Preconditions.checkNotNull(toElement);
      return new StandardRowSortedTable<R, C, V>(
          sortedBackingMap().subMap(fromElement, toElement), factory)
          .rowKeySet();
    }

    public SortedSet<R> tailSet(R fromElement) {
      Preconditions.checkNotNull(fromElement);
      return new StandardRowSortedTable<R, C, V>(
          sortedBackingMap().tailMap(fromElement), factory).rowKeySet();
    }
  }

  private transient RowSortedMap rowMap;

  /**
   * {@inheritDoc}
   *
   * <p>This method returns a {@link SortedMap}, instead of the {@code Map}
   * specified in the {@link Table} interface.
   */
  @Override public SortedMap<R, Map<C, V>> rowMap() {
    RowSortedMap result = rowMap;
    return (result == null) ? rowMap = new RowSortedMap() : result;
  }

  private class RowSortedMap extends RowMap implements SortedMap<R, Map<C, V>> {
    public Comparator<? super R> comparator() {
      return sortedBackingMap().comparator();
    }

    public R firstKey() {
      return sortedBackingMap().firstKey();
    }

    public R lastKey() {
      return sortedBackingMap().lastKey();
    }

    public SortedMap<R, Map<C, V>> headMap(R toKey) {
      Preconditions.checkNotNull(toKey);
      return new StandardRowSortedTable<R, C, V>(
          sortedBackingMap().headMap(toKey), factory).rowMap();
    }

    public SortedMap<R, Map<C, V>> subMap(R fromKey, R toKey) {
      Preconditions.checkNotNull(fromKey);
      Preconditions.checkNotNull(toKey);
      return new StandardRowSortedTable<R, C, V>(
          sortedBackingMap().subMap(fromKey, toKey), factory).rowMap();
    }

    public SortedMap<R, Map<C, V>> tailMap(R fromKey) {
      Preconditions.checkNotNull(fromKey);
      return new StandardRowSortedTable<R, C, V>(
          sortedBackingMap().tailMap(fromKey), factory).rowMap();
    }
  }

  private static final long serialVersionUID = 0;
}
