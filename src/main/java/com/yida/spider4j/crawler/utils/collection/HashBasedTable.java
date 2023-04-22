package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.base.Supplier;

/**
 * Implementation of {@link Table} using hash tables.
 *
 * <p>The views returned by {@link #column}, {@link #columnKeySet()}, and {@link
 * #columnMap()} have iterators that don't support {@code remove()}. Otherwise,
 * all optional operations are supported. Null row keys, columns keys, and
 * values are not supported.
 *
 * <p>Lookups by row key are often faster than lookups by column key, because
 * the data is stored in a {@code Map<R, Map<C, V>>}. A method call like {@code
 * column(columnKey).get(rowKey)} still runs quickly, since the row key is
 * provided. However, {@code column(columnKey).size()} takes longer, since an
 * iteration across all row keys occurs.
 *
 * <p>Note that this implementation is not synchronized. If multiple threads
 * access this table concurrently and one of the threads modifies the table, it
 * must be synchronized externally.
 *
 * @author Jared Levy
 * @since 7
 */
@GwtCompatible(serializable = true)
@Beta
public class HashBasedTable<R, C, V> extends StandardTable<R, C, V> {
  private static class Factory<C, V>
      implements Supplier<Map<C, V>>, Serializable {
    final int expectedSize;
    Factory(int expectedSize) {
      this.expectedSize = expectedSize;
    }
    public Map<C, V> get() {
      return Maps.newHashMapWithExpectedSize(expectedSize);
    }
    private static final long serialVersionUID = 0;
  }

  /**
   * Creates an empty {@code HashBasedTable}.
   */
  public static <R, C, V> HashBasedTable<R, C, V> create() {
    return new HashBasedTable<R, C, V>(
        new HashMap<R, Map<C, V>>(), new Factory<C, V>(0));
  }

  /**
   * Creates an empty {@code HashBasedTable} with the specified map sizes.
   *
   * @param expectedRows the expected number of distinct row keys
   * @param expectedCellsPerRow the expected number of column key / value
   *     mappings in each row
   * @throws IllegalArgumentException if {@code expectedRows} or {@code
   *     expectedCellsPerRow} is negative
   */
  public static <R, C, V> HashBasedTable<R, C, V> create(
      int expectedRows, int expectedCellsPerRow) {
    Preconditions.checkArgument(expectedCellsPerRow >= 0);
    Map<R, Map<C, V>> backingMap = Maps.newHashMapWithExpectedSize(expectedRows);
    return new HashBasedTable<R, C, V>(
        backingMap, new Factory<C, V>(expectedCellsPerRow));
  }

  /**
   * Creates a {@code HashBasedTable} with the same mappings as the specified
   * table.
   *
   * @param table the table to copy
   * @throws NullPointerException if any of the row keys, column keys, or values
   *     in {@code table} are null.
   */
  public static <R, C, V> HashBasedTable<R, C, V> create(
      Table<? extends R, ? extends C, ? extends V> table) {
    HashBasedTable<R, C, V> result = create();
    result.putAll(table);
    return result;
  }

  HashBasedTable(Map<R, Map<C, V>> backingMap, Factory<C, V> factory) {
    super(backingMap, factory);
  }

  // Overriding so NullPointerTester test passes.

  @Override public boolean contains(
      Object rowKey, Object columnKey) {
    return super.contains(rowKey, columnKey);
  }

  @Override public boolean containsColumn(Object columnKey) {
    return super.containsColumn(columnKey);
  }

  @Override public boolean containsRow(Object rowKey) {
    return super.containsRow(rowKey);
  }

  @Override public boolean containsValue(Object value) {
    return super.containsValue(value);
  }

  @Override public V get(Object rowKey, Object columnKey) {
    return super.get(rowKey, columnKey);
  }

  @Override public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override public V remove(
      Object rowKey, Object columnKey) {
    return super.remove(rowKey, columnKey);
  }

  private static final long serialVersionUID = 0;
}
