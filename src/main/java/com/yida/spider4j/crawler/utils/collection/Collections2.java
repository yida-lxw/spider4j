/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.base.Function;
import com.yida.spider4j.crawler.utils.collection.base.Joiner;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.base.Predicates;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

/**
 * Provides static methods for working with {@code Collection} instances.
 *
 * @author Chris Povirk
 * @author Mike Bostock
 * @author Jared Levy
 * @since 2 (imported from Google Collections Library)
 */
public final class Collections2 {
  private Collections2() {}

  /**
   * Returns the elements of {@code unfiltered} that satisfy a predicate. The
   * returned collection is a live view of {@code unfiltered}; changes to one
   * affect the other.
   *
   * <p>The resulting collection's iterator does not support {@code remove()},
   * but all other collection methods are supported. When given an element that
   * doesn't satisfy the predicate, the collection's {@code add()} and {@code
   * addAll()} methods throw an {@link IllegalArgumentException}. When methods
   * such as {@code removeAll()} and {@code clear()} are called on the filtered
   * collection, only elements that satisfy the filter will be removed from the
   * underlying collection.
   *
   * <p>The returned collection isn't threadsafe or serializable, even if
   * {@code unfiltered} is.
   *
   * <p>Many of the filtered collection's methods, such as {@code size()},
   * iterate across every element in the underlying collection and determine
   * which elements satisfy the filter. When a live view is <i>not</i> needed,
   * it may be faster to copy {@code Iterables.filter(unfiltered, predicate)}
   * and use the copy.
   *
   * <p><b>Warning:</b> {@code predicate} must be <i>consistent with equals</i>,
   * as documented at {@link Predicate#apply}. Do not provide a predicate such
   * as {@code Predicates.instanceOf(ArrayList.class)}, which is inconsistent
   * with equals. (See {@link Iterables#filter(Iterable, Class)} for related
   * functionality.)
   */
  public static <E> Collection<E> filter(
      Collection<E> unfiltered, Predicate<? super E> predicate) {
    if (unfiltered instanceof FilteredCollection) {
      // Support clear(), removeAll(), and retainAll() when filtering a filtered
      // collection.
      return ((FilteredCollection<E>) unfiltered).createCombined(predicate);
    }

    return new FilteredCollection<E>(
        Preconditions.checkNotNull(unfiltered), Preconditions.checkNotNull(predicate));
  }

  /**
   * Delegates to {@link Collection#contains}.  Returns {@code false} on {@code
   * ClassCastException}
   */
  static boolean safeContains(Collection<?> collection, Object object) {
    try {
      return collection.contains(object);
    } catch (ClassCastException e) {
      return false;
    }
  }

  static class FilteredCollection<E> implements Collection<E> {
    final Collection<E> unfiltered;
    final Predicate<? super E> predicate;

    FilteredCollection(Collection<E> unfiltered,
        Predicate<? super E> predicate) {
      this.unfiltered = unfiltered;
      this.predicate = predicate;
    }

    FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
      return new FilteredCollection<E>(unfiltered,
          Predicates.<E>and(predicate, newPredicate));
      // .<E> above needed to compile in JDK 5
    }

    public boolean add(E element) {
      Preconditions.checkArgument(predicate.apply(element));
      return unfiltered.add(element);
    }

    public boolean addAll(Collection<? extends E> collection) {
      for (E element : collection) {
        Preconditions.checkArgument(predicate.apply(element));
      }
      return unfiltered.addAll(collection);
    }

    public void clear() {
      Iterables.removeIf(unfiltered, predicate);
    }

    public boolean contains(Object element) {
      try {
        // unsafe cast can result in a CCE from predicate.apply(), which we
        // will catch
        @SuppressWarnings("unchecked")
        E e = (E) element;

        /*
         * We check whether e satisfies the predicate, when we really mean to
         * check whether the element contained in the set does. This is ok as
         * long as the predicate is consistent with equals, as required.
         */
        return predicate.apply(e) && unfiltered.contains(element);
      } catch (NullPointerException e) {
        return false;
      } catch (ClassCastException e) {
        return false;
      }
    }

    public boolean containsAll(Collection<?> collection) {
      for (Object element : collection) {
        if (!contains(element)) {
          return false;
        }
      }
      return true;
    }

    public boolean isEmpty() {
      return !Iterators.any(unfiltered.iterator(), predicate);
    }

    public Iterator<E> iterator() {
      return Iterators.filter(unfiltered.iterator(), predicate);
    }

    public boolean remove(Object element) {
      try {
        // unsafe cast can result in a CCE from predicate.apply(), which we
        // will catch
        @SuppressWarnings("unchecked")
        E e = (E) element;

        // See comment in contains() concerning predicate.apply(e)
        return predicate.apply(e) && unfiltered.remove(element);
      } catch (NullPointerException e) {
        return false;
      } catch (ClassCastException e) {
        return false;
      }
    }

    public boolean removeAll(final Collection<?> collection) {
      Preconditions.checkNotNull(collection);
      Predicate<E> combinedPredicate = new Predicate<E>() {
        public boolean apply(E input) {
          return predicate.apply(input) && collection.contains(input);
        }
      };
      return Iterables.removeIf(unfiltered, combinedPredicate);
    }

    public boolean retainAll(final Collection<?> collection) {
      Preconditions.checkNotNull(collection);
      Predicate<E> combinedPredicate = new Predicate<E>() {
        public boolean apply(E input) {
          // See comment in contains() concerning predicate.apply(e)
          return predicate.apply(input) && !collection.contains(input);
        }
      };
      return Iterables.removeIf(unfiltered, combinedPredicate);
    }

    public int size() {
      return Iterators.size(iterator());
    }

    public Object[] toArray() {
      // creating an ArrayList so filtering happens once
      return Lists.newArrayList(iterator()).toArray();
    }

    public <T> T[] toArray(T[] array) {
      return Lists.newArrayList(iterator()).toArray(array);
    }

    @Override public String toString() {
      return Iterators.toString(iterator());
    }
  }

  /**
   * Returns a collection that applies {@code function} to each element of
   * {@code fromCollection}. The returned collection is a live view of {@code
   * fromCollection}; changes to one affect the other.
   *
   * <p>The returned collection's {@code add()} and {@code addAll()} methods
   * throw an {@link UnsupportedOperationException}. All other collection
   * methods are supported, as long as {@code fromCollection} supports them.
   *
   * <p>The returned collection isn't threadsafe or serializable, even if
   * {@code fromCollection} is.
   *
   * <p>When a live view is <i>not</i> needed, it may be faster to copy the
   * transformed collection and use the copy.
   */
  public static <F, T> Collection<T> transform(Collection<F> fromCollection,
      Function<? super F, T> function) {
    return new TransformedCollection<F, T>(fromCollection, function);
  }

  static class TransformedCollection<F, T> extends AbstractCollection<T> {
    final Collection<F> fromCollection;
    final Function<? super F, ? extends T> function;

    TransformedCollection(Collection<F> fromCollection,
        Function<? super F, ? extends T> function) {
      this.fromCollection = Preconditions.checkNotNull(fromCollection);
      this.function = Preconditions.checkNotNull(function);
    }

    @Override public void clear() {
      fromCollection.clear();
    }

    @Override public boolean isEmpty() {
      return fromCollection.isEmpty();
    }

    @Override public Iterator<T> iterator() {
      return Iterators.transform(fromCollection.iterator(), function);
    }

    @Override public int size() {
      return fromCollection.size();
    }
  }

  /**
   * Returns {@code true} if the collection {@code self} contains all of the
   * elements in the collection {@code c}.
   *
   * <p>This method iterates over the specified collection {@code c}, checking
   * each element returned by the iterator in turn to see if it is contained in
   * the specified collection {@code self}. If all elements are so contained,
   * {@code true} is returned, otherwise {@code false}.
   *
   * @param self a collection which might contain all elements in {@code c}
   * @param c a collection whose elements might be contained by {@code self}
   */
  static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
    Preconditions.checkNotNull(self);
    for (Object o : c) {
      if (!self.contains(o)) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * An implementation of {@link Collection#toString()}.
   */
  static String toStringImpl(final Collection<?> collection) {
    StringBuilder sb
        = newStringBuilderForCollection(collection.size()).append('[');
    STANDARD_JOINER.appendTo(
        sb, Iterables.transform(collection, new Function<Object, Object>() {
          @Override public Object apply(Object input) {
            return input == collection ? "(this Collection)" : input;
          }
        }));
    return sb.append(']').toString();
  }

  /**
   * Returns best-effort-sized StringBuilder based on the given collection size.
   */
  static StringBuilder newStringBuilderForCollection(int size) {
    Preconditions.checkArgument(size >= 0, "size must be non-negative");
    return new StringBuilder((int) Math.min(size * 8L, 1 << 30));
  }

  /**
   * Used to avoid http://bugs.sun.com/view_bug.do?bug_id=6558557
   */
  static <T> Collection<T> cast(Iterable<T> iterable) {
    return (Collection<T>) iterable;
  }

  static final Joiner STANDARD_JOINER = Joiner.on(", ");
}
