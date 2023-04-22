package com.yida.spider4j.crawler.utils.collection;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * An immutable {@code SortedSet} that stores its elements in a sorted array.
 * Some instances are ordered by an explicit comparator, while others follow the
 * natural sort ordering of their elements. Either way, null elements are not
 * supported.
 * 
 * <p>
 * Unlike {@link Collections#unmodifiableSortedSet}, which is a <i>view</i> of a
 * separate collection that can still change, an instance of
 * {@code ImmutableSortedSet} contains its own private data and will
 * <i>never</i> change. This class is convenient for {@code public static final}
 * sets ("constant sets") and also lets you easily make a "defensive copy" of a
 * set provided to your class by a caller.
 * 
 * <p>
 * The sets returned by {@link #headSet}, {@link #tailSet}, and {@link #subSet}
 * methods share the same array as the original set, preventing that array from
 * being garbage collected. If this is a concern, the data may be copied into a
 * correctly-sized array by calling {@link #copyOfSorted}.
 * 
 * <p>
 * <b>Note on element equivalence:</b> The {@link #contains(Object)},
 * {@link #containsAll(Collection)}, and {@link #equals(Object)} implementations
 * must check whether a provided object is equivalent to an element in the
 * collection. Unlike most collections, an {@code ImmutableSortedSet} doesn't
 * use {@link Object#equals} to determine if two elements are equivalent.
 * Instead, with an explicit comparator, the following relation determines
 * whether elements {@code x} and {@code y} are equivalent:
 * 
 * <pre>
 * {@code
 * 
 *   {(x, y) | comparator.compare(x, y) == 0}}
 * </pre>
 * 
 * With natural ordering of elements, the following relation determines whether
 * two elements are equivalent:
 * 
 * <pre>
 * {@code
 * 
 *   {(x, y) | x.compareTo(y) == 0}}
 * </pre>
 * 
 * <b>Warning:</b> Like most sets, an {@code ImmutableSortedSet} will not
 * function correctly if an element is modified after being placed in the set.
 * For this reason, and to avoid general confusion, it is strongly recommended
 * to place only immutable objects into this collection.
 * 
 * <p>
 * <b>Note</b>: Although this class is not final, it cannot be subclassed as it
 * has no public or protected constructors. Thus, instances of this type are
 * guaranteed to be immutable.
 * 
 * @see ImmutableSet
 * @author Jared Levy
 * @author Louis Wasserman
 * @since 2 (imported from Google Collections Library)
 */
// TODO(benyu): benchmark and optimize all creation paths, which are a mess now
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
// we're overriding default serialization
public abstract class ImmutableSortedSet<E> extends
		ImmutableSortedSetFauxverideShim<E> implements SortedSet<E> {

	// TODO(cpovirk): find a way to remove this @SuppressWarnings even for
	// eclipse?

	private static final Comparator NATURAL_ORDER = Ordering.natural();

	private static final ImmutableSortedSet<Object> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet<Object>(
			NATURAL_ORDER);

	private static <E> ImmutableSortedSet<E> emptySet() {
		return (ImmutableSortedSet<E>) NATURAL_EMPTY_SET;
	}

	static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
		if (NATURAL_ORDER.equals(comparator)) {
			return emptySet();
		} else {
			return new EmptyImmutableSortedSet<E>(comparator);
		}
	}

	/**
	 * Returns the empty immutable sorted set.
	 */
	public static <E> ImmutableSortedSet<E> of() {
		return emptySet();
	}

	/**
	 * Returns an immutable sorted set containing a single element.
	 */
	public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(
			E element) {
		return new RegularImmutableSortedSet<E>(ImmutableList.of(element),
				Ordering.natural());
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@link Comparable#compareTo}, only the first one specified is
	 * included.
	 * 
	 * @throws NullPointerException
	 *             if any element is null
	 */

	public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(
			E e1, E e2) {
		return copyOf(Ordering.natural(), Arrays.asList(e1, e2));
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@link Comparable#compareTo}, only the first one specified is
	 * included.
	 * 
	 * @throws NullPointerException
	 *             if any element is null
	 */

	public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(
			E e1, E e2, E e3) {
		return copyOf(Ordering.natural(), Arrays.asList(e1, e2, e3));
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@link Comparable#compareTo}, only the first one specified is
	 * included.
	 * 
	 * @throws NullPointerException
	 *             if any element is null
	 */

	public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(
			E e1, E e2, E e3, E e4) {
		return copyOf(Ordering.natural(), Arrays.asList(e1, e2, e3, e4));
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@link Comparable#compareTo}, only the first one specified is
	 * included.
	 * 
	 * @throws NullPointerException
	 *             if any element is null
	 */

	public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(
			E e1, E e2, E e3, E e4, E e5) {
		return copyOf(Ordering.natural(), Arrays.asList(e1, e2, e3, e4, e5));
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@link Comparable#compareTo}, only the first one specified is
	 * included.
	 * 
	 * @throws NullPointerException
	 *             if any element is null
	 * @since 3 (source-compatible since release 2)
	 */

	public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(
			E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
		int size = remaining.length + 6;
		List<E> all = new ArrayList<E>(size);
		Collections.addAll(all, e1, e2, e3, e4, e5, e6);
		Collections.addAll(all, remaining);
		return copyOf(Ordering.natural(), all);
	}

	// TODO(kevinb): Consider factory methods that reject duplicates

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@link Comparable#compareTo}, only the first one specified is
	 * included.
	 * 
	 * @throws NullPointerException
	 *             if any of {@code elements} is null
	 * @deprecated use {@link #copyOf(Comparable[])}. <b>This method is
	 *             scheduled for deletion in October 2011.</b>
	 * @since 2 (changed from varargs in release 3)
	 */
	@Deprecated
	public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(
			E[] elements) {
		return copyOf(elements);
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@link Comparable#compareTo}, only the first one specified is
	 * included.
	 * 
	 * @throws NullPointerException
	 *             if any of {@code elements} is null
	 * @since 3
	 */
	public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(
			E[] elements) {
		return copyOf(Ordering.natural(), Arrays.asList(elements));
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@code compareTo()}, only the first one specified is included. To
	 * create a copy of a {@code SortedSet} that preserves the comparator, call
	 * {@link #copyOfSorted} instead. This method iterates over {@code elements}
	 * at most once.
	 * 
	 * 
	 * <p>
	 * Note that if {@code s} is a {@code Set<String>}, then
	 * {@code ImmutableSortedSet.copyOf(s)} returns an
	 * {@code ImmutableSortedSet<String>} containing each of the strings in
	 * {@code s}, while {@code ImmutableSortedSet.of(s)} returns an
	 * {@code ImmutableSortedSet<Set<String>>} containing one element (the given
	 * set itself).
	 * 
	 * <p>
	 * Despite the method name, this method attempts to avoid actually copying
	 * the data when it is safe to do so. The exact circumstances under which a
	 * copy will or will not be performed are undocumented and subject to
	 * change.
	 * 
	 * <p>
	 * This method is not type-safe, as it may be called on elements that are
	 * not mutually comparable.
	 * 
	 * @throws ClassCastException
	 *             if the elements are not mutually comparable
	 * @throws NullPointerException
	 *             if any of {@code elements} is null
	 */
	public static <E> ImmutableSortedSet<E> copyOf(
			Iterable<? extends E> elements) {
		// Hack around K not being a subtype of Comparable.
		// Unsafe, see ImmutableSortedSetFauxverideShim.

		Ordering<E> naturalOrder = (Ordering) Ordering.<Comparable> natural();
		return copyOf(naturalOrder, elements);
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@code compareTo()}, only the first one specified is included. To
	 * create a copy of a {@code SortedSet} that preserves the comparator, call
	 * {@link #copyOfSorted} instead. This method iterates over {@code elements}
	 * at most once.
	 * 
	 * <p>
	 * Note that if {@code s} is a {@code Set<String>}, then
	 * {@code ImmutableSortedSet.copyOf(s)} returns an
	 * {@code ImmutableSortedSet<String>} containing each of the strings in
	 * {@code s}, while {@code ImmutableSortedSet.of(s)} returns an
	 * {@code ImmutableSortedSet<Set<String>>} containing one element (the given
	 * set itself).
	 * 
	 * <p>
	 * <b>Note:</b> Despite what the method name suggests, if {@code elements}
	 * is an {@code ImmutableSortedSet}, it may be returned instead of a copy.
	 * 
	 * <p>
	 * This method is not type-safe, as it may be called on elements that are
	 * not mutually comparable.
	 * 
	 * <p>
	 * This method is safe to use even when {@code elements} is a synchronized
	 * or concurrent collection that is currently being modified by another
	 * thread.
	 * 
	 * @throws ClassCastException
	 *             if the elements are not mutually comparable
	 * @throws NullPointerException
	 *             if any of {@code elements} is null
	 * @since 7 (source-compatible since release 2)
	 */
	public static <E> ImmutableSortedSet<E> copyOf(
			Collection<? extends E> elements) {
		// Hack around K not being a subtype of Comparable.
		// Unsafe, see ImmutableSortedSetFauxverideShim.

		Ordering<E> naturalOrder = (Ordering) Ordering.<Comparable> natural();
		return copyOf(naturalOrder, elements);
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * their natural ordering. When multiple elements are equivalent according
	 * to {@code compareTo()}, only the first one specified is included.
	 * 
	 * <p>
	 * This method is not type-safe, as it may be called on elements that are
	 * not mutually comparable.
	 * 
	 * @throws ClassCastException
	 *             if the elements are not mutually comparable
	 * @throws NullPointerException
	 *             if any of {@code elements} is null
	 */
	public static <E> ImmutableSortedSet<E> copyOf(
			Iterator<? extends E> elements) {
		// Hack around K not being a subtype of Comparable.
		// Unsafe, see ImmutableSortedSetFauxverideShim.

		Ordering<E> naturalOrder = (Ordering) Ordering.<Comparable> natural();
		return copyOfInternal(naturalOrder, elements);
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * the given {@code Comparator}. When multiple elements are equivalent
	 * according to {@code compareTo()}, only the first one specified is
	 * included.
	 * 
	 * @throws NullPointerException
	 *             if {@code comparator} or any of {@code elements} is null
	 */
	public static <E> ImmutableSortedSet<E> copyOf(
			Comparator<? super E> comparator, Iterator<? extends E> elements) {
		Preconditions.checkNotNull(comparator);
		return copyOfInternal(comparator, elements);
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * the given {@code Comparator}. When multiple elements are equivalent
	 * according to {@code compare()}, only the first one specified is included.
	 * This method iterates over {@code elements} at most once.
	 * 
	 * <p>
	 * Despite the method name, this method attempts to avoid actually copying
	 * the data when it is safe to do so. The exact circumstances under which a
	 * copy will or will not be performed are undocumented and subject to
	 * change.
	 * 
	 * @throws NullPointerException
	 *             if {@code comparator} or any of {@code elements} is null
	 */
	public static <E> ImmutableSortedSet<E> copyOf(
			Comparator<? super E> comparator, Iterable<? extends E> elements) {
		Preconditions.checkNotNull(comparator);
		return copyOfInternal(comparator, elements, false);
	}

	/**
	 * Returns an immutable sorted set containing the given elements sorted by
	 * the given {@code Comparator}. When multiple elements are equivalent
	 * according to {@code compareTo()}, only the first one specified is
	 * included.
	 * 
	 * <p>
	 * Despite the method name, this method attempts to avoid actually copying
	 * the data when it is safe to do so. The exact circumstances under which a
	 * copy will or will not be performed are undocumented and subject to
	 * change.
	 * 
	 * <p>
	 * This method is safe to use even when {@code elements} is a synchronized
	 * or concurrent collection that is currently being modified by another
	 * thread.
	 * 
	 * @throws NullPointerException
	 *             if {@code comparator} or any of {@code elements} is null
	 * @since 7 (source-compatible since release 2)
	 */
	public static <E> ImmutableSortedSet<E> copyOf(
			Comparator<? super E> comparator, Collection<? extends E> elements) {
		Preconditions.checkNotNull(comparator);
		return copyOfInternal(comparator, elements, false);
	}

	/**
	 * Returns an immutable sorted set containing the elements of a sorted set,
	 * sorted by the same {@code Comparator}. That behavior differs from
	 * {@link #copyOf(Iterable)}, which always uses the natural ordering of the
	 * elements.
	 * 
	 * <p>
	 * Despite the method name, this method attempts to avoid actually copying
	 * the data when it is safe to do so. The exact circumstances under which a
	 * copy will or will not be performed are undocumented and subject to
	 * change.
	 * 
	 * <p>
	 * This method is safe to use even when {@code elements} is a synchronized
	 * or concurrent collection that is currently being modified by another
	 * thread.
	 * 
	 * @throws NullPointerException
	 *             if {@code sortedSet} or any of its elements is null
	 */

	public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
		Comparator<? super E> comparator = sortedSet.comparator();
		if (comparator == null) {
			comparator = NATURAL_ORDER;
		}
		return copyOfInternal(comparator, sortedSet, true);
	}

	private static <E> ImmutableSortedSet<E> copyOfInternal(
			Comparator<? super E> comparator, Iterable<? extends E> elements,
			boolean fromSortedSet) {
		boolean hasSameComparator = fromSortedSet
				|| hasSameComparator(elements, comparator);

		if (hasSameComparator && (elements instanceof ImmutableSortedSet)) {

			ImmutableSortedSet<E> original = (ImmutableSortedSet<E>) elements;
			if (original.isEmpty()) {
				return original;
			}
			ImmutableList<E> elementsList = original.asList();
			ImmutableList<E> copiedElementsList = ImmutableList
					.copyOf(elements);
			if (elementsList == copiedElementsList) {
				return original;
			}
			return new RegularImmutableSortedSet<E>(copiedElementsList,
					comparator);
		}

		ImmutableList<E> list = immutableSortedUniqueCopy(comparator,
				Lists.newArrayList(elements));
		if (list.isEmpty()) {
			return emptySet(comparator);
		}
		return new RegularImmutableSortedSet<E>(list, comparator);
	}

	private static <E> ImmutableSortedSet<E> copyOfInternal(
			Comparator<? super E> comparator, Iterator<? extends E> elements) {
		if (!elements.hasNext()) {
			return emptySet(comparator);
		}
		ImmutableList<E> list = immutableSortedUniqueCopy(comparator,
				Lists.newArrayList(elements));
		return new RegularImmutableSortedSet<E>(list, comparator);
	}

	/**
	 * The list will get modified. Sorts the list, eliminates duplicate
	 * elements, returns an immutable copy.
	 */
	private static <E> ImmutableList<E> immutableSortedUniqueCopy(
			Comparator<? super E> comparator, List<E> list) {
		if (list.isEmpty()) {
			return ImmutableList.of();
		}
		Collections.sort(list, comparator);
		int size = 1;
		for (int i = 1; i < list.size(); i++) {
			E elem = list.get(i);
			if (comparator.compare(elem, list.get(size - 1)) != 0) {
				list.set(size++, elem);
			}
		}
		return ImmutableList.copyOf(list.subList(0, size));
	}

	/**
	 * Returns {@code true} if {@code elements} is a {@code SortedSet} that uses
	 * {@code comparator} to order its elements. Note that equivalent
	 * comparators may still return {@code false}, if {@code equals} doesn't
	 * consider them equal. If one comparator is {@code null} and the other is
	 * {@link Ordering#natural()}, this method returns {@code true}.
	 */
	static boolean hasSameComparator(Iterable<?> elements,
			Comparator<?> comparator) {
		if (elements instanceof SortedSet) {
			SortedSet<?> sortedSet = (SortedSet<?>) elements;
			Comparator<?> comparator2 = sortedSet.comparator();
			return (comparator2 == null) ? comparator == Ordering.natural()
					: comparator.equals(comparator2);
		}
		return false;
	}

	/**
	 * Returns an immutable sorted set containing the elements in the given list
	 * in the same order. It is useful when the elements already have the
	 * desired order but constructing the appropriate comparator is difficult.
	 * 
	 * @throws NullPointerException
	 *             if any of the elements is null
	 * @throws IllegalArgumentException
	 *             if {@code elements} contains any duplicate values (according
	 *             to {@link Object#equals})
	 * @since 3
	 */
	@Beta
	public static <E> ImmutableSortedSet<E> withExplicitOrder(List<E> elements) {
		return ExplicitOrderedImmutableSortedSet.create(elements);
	}

	/**
	 * Returns an immutable sorted set containing the provided elements in the
	 * same order. It is useful when the elements already have the desired order
	 * but constructing the appropriate comparator is difficult.
	 * 
	 * @param firstElement
	 *            the value which should appear first in the generated set
	 * @param remainingElementsInOrder
	 *            the rest of the values in the generated set, in the order they
	 *            should appear
	 * @throws NullPointerException
	 *             if any of the elements is null
	 * @throws IllegalArgumentException
	 *             if any duplicate values (according to
	 *             {@link Object#equals(Object)}) are present among the method
	 *             arguments
	 * @since 3
	 */
	@Beta
	public static <E> ImmutableSortedSet<E> withExplicitOrder(E firstElement,
			E... remainingElementsInOrder) {
		return withExplicitOrder(Lists.asList(firstElement,
				remainingElementsInOrder));
	}

	/**
	 * Returns a builder that creates immutable sorted sets with an explicit
	 * comparator. If the comparator has a more general type than the set being
	 * generated, such as creating a {@code SortedSet<Integer>} with a
	 * {@code Comparator<Number>}, use the {@link Builder} constructor instead.
	 * 
	 * @throws NullPointerException
	 *             if {@code comparator} is null
	 */
	public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
		return new Builder<E>(comparator);
	}

	/**
	 * Returns a builder that creates immutable sorted sets whose elements are
	 * ordered by the reverse of their natural ordering.
	 * 
	 * <p>
	 * Note: the type parameter {@code E} extends {@code Comparable<E>} rather
	 * than {@code Comparable<? super E>} as a workaround for javac <a
	 * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6468354">bug
	 * 6468354</a>.
	 */
	public static <E extends Comparable<E>> Builder<E> reverseOrder() {
		return new Builder<E>(Ordering.natural().reverse());
	}

	/**
	 * Returns a builder that creates immutable sorted sets whose elements are
	 * ordered by their natural ordering. The sorted sets use
	 * {@link Ordering#natural()} as the comparator. This method provides more
	 * type-safety than {@link #builder}, as it can be called only for classes
	 * that implement {@link Comparable}.
	 * 
	 * <p>
	 * Note: the type parameter {@code E} extends {@code Comparable<E>} rather
	 * than {@code Comparable<? super E>} as a workaround for javac <a
	 * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6468354">bug
	 * 6468354</a>.
	 */
	public static <E extends Comparable<E>> Builder<E> naturalOrder() {
		return new Builder<E>(Ordering.natural());
	}

	/**
	 * A builder for creating immutable sorted set instances, especially
	 * {@code public static final} sets ("constant sets"), with a given
	 * comparator. Example:
	 * 
	 * <pre>
	 * {
	 * 	&#064;code
	 * 	public static final ImmutableSortedSet&lt;Number&gt; LUCKY_NUMBERS = new ImmutableSortedSet.Builder&lt;Number&gt;(
	 * 			ODDS_FIRST_COMPARATOR).addAll(SINGLE_DIGIT_PRIMES).add(42).build();
	 * }
	 * </pre>
	 * 
	 * Builder instances can be reused; it is safe to call {@link #build}
	 * multiple times to build multiple sets in series. Each set is a superset
	 * of the set created before it.
	 * 
	 * @since 2 (imported from Google Collections Library)
	 */
	public static final class Builder<E> extends ImmutableSet.Builder<E> {
		private final Comparator<? super E> comparator;

		/**
		 * Creates a new builder. The returned builder is equivalent to the
		 * builder generated by {@link ImmutableSortedSet#orderedBy}.
		 */
		public Builder(Comparator<? super E> comparator) {
			this.comparator = Preconditions.checkNotNull(comparator);
		}

		/**
		 * Adds {@code element} to the {@code ImmutableSortedSet}. If the
		 * {@code ImmutableSortedSet} already contains {@code element}, then
		 * {@code add} has no effect. (only the previously added element is
		 * retained).
		 * 
		 * @param element
		 *            the element to add
		 * @return this {@code Builder} object
		 * @throws NullPointerException
		 *             if {@code element} is null
		 */
		@Override
		public Builder<E> add(E element) {
			super.add(element);
			return this;
		}

		/**
		 * Adds each element of {@code elements} to the
		 * {@code ImmutableSortedSet}, ignoring duplicate elements (only the
		 * first duplicate element is added).
		 * 
		 * @param elements
		 *            the elements to add
		 * @return this {@code Builder} object
		 * @throws NullPointerException
		 *             if {@code elements} contains a null element
		 */
		@Override
		public Builder<E> add(E... elements) {
			super.add(elements);
			return this;
		}

		/**
		 * Adds each element of {@code elements} to the
		 * {@code ImmutableSortedSet}, ignoring duplicate elements (only the
		 * first duplicate element is added).
		 * 
		 * @param elements
		 *            the elements to add to the {@code ImmutableSortedSet}
		 * @return this {@code Builder} object
		 * @throws NullPointerException
		 *             if {@code elements} contains a null element
		 */
		@Override
		public Builder<E> addAll(Iterable<? extends E> elements) {
			super.addAll(elements);
			return this;
		}

		/**
		 * Adds each element of {@code elements} to the
		 * {@code ImmutableSortedSet}, ignoring duplicate elements (only the
		 * first duplicate element is added).
		 * 
		 * @param elements
		 *            the elements to add to the {@code ImmutableSortedSet}
		 * @return this {@code Builder} object
		 * @throws NullPointerException
		 *             if {@code elements} contains a null element
		 */
		@Override
		public Builder<E> addAll(Iterator<? extends E> elements) {
			super.addAll(elements);
			return this;
		}

		/**
		 * Returns a newly-created {@code ImmutableSortedSet} based on the
		 * contents of the {@code Builder} and its comparator.
		 */
		@Override
		public ImmutableSortedSet<E> build() {
			return copyOfInternal(comparator, contents.iterator());
		}
	}

	int unsafeCompare(Object a, Object b) {
		return unsafeCompare(comparator, a, b);
	}

	static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
		// Pretend the comparator can compare anything. If it turns out it can't
		// compare a and b, we should get a CCE on the subsequent line. Only
		// methods
		// that are spec'd to throw CCE should call this.

		Comparator<Object> unsafeComparator = (Comparator<Object>) comparator;
		return unsafeComparator.compare(a, b);
	}

	final transient Comparator<? super E> comparator;

	ImmutableSortedSet(Comparator<? super E> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Returns the comparator that orders the elements, which is
	 * {@link Ordering#natural()} when the natural ordering of the elements is
	 * used. Note that its behavior is not consistent with
	 * {@link SortedSet#comparator()}, which returns {@code null} to indicate
	 * natural ordering.
	 */
	public Comparator<? super E> comparator() {
		return comparator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * This method returns a serializable {@code ImmutableSortedSet}.
	 * 
	 * <p>
	 * The {@link SortedSet#headSet} documentation states that a subset of a
	 * subset throws an {@link IllegalArgumentException} if passed a
	 * {@code toElement} greater than an earlier {@code toElement}. However,
	 * this method doesn't throw an exception in that situation, but instead
	 * keeps the original {@code toElement}.
	 */
	public ImmutableSortedSet<E> headSet(E toElement) {
		return headSetImpl(Preconditions.checkNotNull(toElement));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * This method returns a serializable {@code ImmutableSortedSet}.
	 * 
	 * <p>
	 * The {@link SortedSet#subSet} documentation states that a subset of a
	 * subset throws an {@link IllegalArgumentException} if passed a
	 * {@code fromElement} smaller than an earlier {@code fromElement}. However,
	 * this method doesn't throw an exception in that situation, but instead
	 * keeps the original {@code fromElement}. Similarly, this method keeps the
	 * original {@code toElement}, instead of throwing an exception, if passed a
	 * {@code toElement} greater than an earlier {@code toElement}.
	 */
	public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
		Preconditions.checkNotNull(fromElement);
		Preconditions.checkNotNull(toElement);
		Preconditions
				.checkArgument(comparator.compare(fromElement, toElement) <= 0);
		return subSetImpl(fromElement, toElement);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * This method returns a serializable {@code ImmutableSortedSet}.
	 * 
	 * <p>
	 * The {@link SortedSet#tailSet} documentation states that a subset of a
	 * subset throws an {@link IllegalArgumentException} if passed a
	 * {@code fromElement} smaller than an earlier {@code fromElement}. However,
	 * this method doesn't throw an exception in that situation, but instead
	 * keeps the original {@code fromElement}.
	 */
	public ImmutableSortedSet<E> tailSet(E fromElement) {
		return tailSetImpl(Preconditions.checkNotNull(fromElement));
	}

	/*
	 * These methods perform most headSet, subSet, and tailSet logic, besides
	 * parameter validation.
	 */
	abstract ImmutableSortedSet<E> headSetImpl(E toElement);

	abstract ImmutableSortedSet<E> subSetImpl(E fromElement, E toElement);

	abstract ImmutableSortedSet<E> tailSetImpl(E fromElement);

	/**
	 * Returns the position of an element within the set, or -1 if not present.
	 */
	abstract int indexOf(Object target);

	/*
	 * This class is used to serialize all ImmutableSortedSet instances,
	 * regardless of implementation type. It captures their "logical contents"
	 * only. This is necessary to ensure that the existence of a particular
	 * implementation type is an implementation detail.
	 */
	private static class SerializedForm<E> implements Serializable {
		final Comparator<? super E> comparator;
		final Object[] elements;

		public SerializedForm(Comparator<? super E> comparator,
				Object[] elements) {
			this.comparator = comparator;
			this.elements = elements;
		}

		Object readResolve() {
			return new Builder<E>(comparator).add((E[]) elements).build();
		}

		private static final long serialVersionUID = 0;
	}

	private void readObject(ObjectInputStream stream)
			throws InvalidObjectException {
		throw new InvalidObjectException("Use SerializedForm");
	}

	@Override
	Object writeReplace() {
		return new SerializedForm<E>(comparator, toArray());
	}
}
