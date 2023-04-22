package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.base.Supplier;

/**
 * Implementation of {@code Table} whose row keys and column keys are ordered by
 * their natural ordering or by supplied comparators. When constructing a
 * {@code TreeBasedTable}, you may provide comparators for the row keys and the
 * column keys, or you may use natural ordering for both.
 * 
 * <p>
 * The {@link #rowKeySet} method returns a {@link SortedSet} and the
 * {@link #rowMap} method returns a {@link SortedMap}, instead of the
 * {@link Set} and {@link Map} specified by the {@link Table} interface.
 * 
 * <p>
 * Note that the column keys as returned by {@link #columnKeySet()} and
 * {@link #columnMap()} are <i>not</i> sorted. Sorted column keys can be
 * observed in {@link Table#row(Object)} and the values of {@link #rowMap()}.
 * 
 * <p>
 * The views returned by {@link #column}, {@link #columnKeySet()}, and
 * {@link #columnMap()} have iterators that don't support {@code remove()}.
 * Otherwise, all optional operations are supported. Null row keys, columns
 * keys, and values are not supported.
 * 
 * <p>
 * Lookups by row key are often faster than lookups by column key, because the
 * data is stored in a {@code Map<R, Map<C, V>>}. A method call like
 * {@code column(columnKey).get(rowKey)} still runs quickly, since the row key
 * is provided. However, {@code column(columnKey).size()} takes longer, since an
 * iteration across all row keys occurs.
 * 
 * <p>
 * Note that this implementation is not synchronized. If multiple threads access
 * this table concurrently and one of the threads modifies the table, it must be
 * synchronized externally.
 * 
 * @author Jared Levy
 * @since 7
 */
@Beta
@GwtCompatible(serializable = true)
@SuppressWarnings("rawtypes")
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V> {
	private final Comparator<? super C> columnComparator;

	private static class Factory<C, V> implements Supplier<TreeMap<C, V>>,
			Serializable {
		final Comparator<? super C> comparator;

		Factory(Comparator<? super C> comparator) {
			this.comparator = comparator;
		}

		public TreeMap<C, V> get() {
			return new TreeMap<C, V>(comparator);
		}

		private static final long serialVersionUID = 0;
	}

	/**
	 * Creates an empty {@code TreeBasedTable} that uses the natural orderings
	 * of both row and column keys.
	 * 
	 * <p>
	 * The method signature specifies {@code R extends Comparable} with a raw
	 * {@link Comparable}, instead of {@code R extends Comparable<? super R>},
	 * and the same for {@code C}. That's necessary to support classes defined
	 * without generics.
	 */
	// eclipse doesn't like the raw Comparable

	public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
		return new TreeBasedTable<R, C, V>(Ordering.natural(),
				Ordering.natural());
	}

	/**
	 * Creates an empty {@code TreeBasedTable} that is ordered by the specified
	 * comparators.
	 * 
	 * @param rowComparator
	 *            the comparator that orders the row keys
	 * @param columnComparator
	 *            the comparator that orders the column keys
	 */
	public static <R, C, V> TreeBasedTable<R, C, V> create(
			Comparator<? super R> rowComparator,
			Comparator<? super C> columnComparator) {
		Preconditions.checkNotNull(rowComparator);
		Preconditions.checkNotNull(columnComparator);
		return new TreeBasedTable<R, C, V>(rowComparator, columnComparator);
	}

	/**
	 * Creates a {@code TreeBasedTable} with the same mappings and sort order as
	 * the specified {@code TreeBasedTable}.
	 */
	public static <R, C, V> TreeBasedTable<R, C, V> create(
			TreeBasedTable<R, C, ? extends V> table) {
		TreeBasedTable<R, C, V> result = new TreeBasedTable<R, C, V>(
				table.rowComparator(), table.columnComparator());
		result.putAll(table);
		return result;
	}

	TreeBasedTable(Comparator<? super R> rowComparator,
			Comparator<? super C> columnComparator) {
		super(new TreeMap<R, Map<C, V>>(rowComparator), new Factory<C, V>(
				columnComparator));
		this.columnComparator = columnComparator;
	}

	// TODO(jlevy): Move to StandardRowSortedTable?

	/**
	 * Returns the comparator that orders the rows. With natural ordering,
	 * {@link Ordering#natural()} is returned.
	 */
	public Comparator<? super R> rowComparator() {
		return rowKeySet().comparator();
	}

	/**
	 * Returns the comparator that orders the columns. With natural ordering,
	 * {@link Ordering#natural()} is returned.
	 */
	public Comparator<? super C> columnComparator() {
		return columnComparator;
	}

	// rowKeySet() and rowMap() are defined here so they appear in the Javadoc.

	@Override
	public SortedSet<R> rowKeySet() {
		return super.rowKeySet();
	}

	@Override
	public SortedMap<R, Map<C, V>> rowMap() {
		return super.rowMap();
	}

	// Overriding so NullPointerTester test passes.

	@Override
	public boolean contains(Object rowKey, Object columnKey) {
		return super.contains(rowKey, columnKey);
	}

	@Override
	public boolean containsColumn(Object columnKey) {
		return super.containsColumn(columnKey);
	}

	@Override
	public boolean containsRow(Object rowKey) {
		return super.containsRow(rowKey);
	}

	@Override
	public boolean containsValue(Object value) {
		return super.containsValue(value);
	}

	@Override
	public V get(Object rowKey, Object columnKey) {
		return super.get(rowKey, columnKey);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public V remove(Object rowKey, Object columnKey) {
		return super.remove(rowKey, columnKey);
	}

	private static final long serialVersionUID = 0;
}
