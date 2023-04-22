package com.yida.spider4j.crawler.utils.collection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;
import com.yida.spider4j.crawler.utils.collection.anno.VisibleForTesting;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Synchronized collection views. The returned synchronized collection views are
 * serializable if the backing collection and the mutex are serializable.
 *
 * <p>If a {@code null} is passed as the {@code mutex} parameter to any of this
 * class's top-level methods or inner class constructors, the created object
 * uses itself as the synchronization mutex.
 *
 * <p>This class should be used by other collection classes only.
 *
 * @author Mike Bostock
 * @author Jared Levy
 */
@GwtCompatible(emulated = true)
final class Synchronized {
  private Synchronized() {}

  static class SynchronizedObject implements Serializable {
    final Object delegate;
    final Object mutex;

    SynchronizedObject(Object delegate, Object mutex) {
      this.delegate = Preconditions.checkNotNull(delegate);
      this.mutex = (mutex == null) ? this : mutex;
    }

    Object delegate() {
      return delegate;
    }

    // No equals and hashCode; see ForwardingObject for details.

    @Override public String toString() {
      synchronized (mutex) {
        return delegate.toString();
      }
    }

    // Serialization invokes writeObject only when it's private.
    // The SynchronizedObject subclasses don't need a writeObject method since
    // they don't contain any non-transient member variables, while the
    // following writeObject() handles the SynchronizedObject members.

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
      synchronized (mutex) {
        stream.defaultWriteObject();
      }
    }

    @GwtIncompatible("not needed in emulated source")
    private static final long serialVersionUID = 0;
  }

  private static <E> Collection<E> collection(
      Collection<E> collection, Object mutex) {
    return new SynchronizedCollection<E>(collection, mutex);
  }

  @VisibleForTesting static class SynchronizedCollection<E>
      extends SynchronizedObject implements Collection<E> {
    private SynchronizedCollection(
        Collection<E> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @SuppressWarnings("unchecked")
    @Override Collection<E> delegate() {
      return (Collection<E>) super.delegate();
    }

    public boolean add(E e) {
      synchronized (mutex) {
        return delegate().add(e);
      }
    }

    public boolean addAll(Collection<? extends E> c) {
      synchronized (mutex) {
        return delegate().addAll(c);
      }
    }

    public void clear() {
      synchronized (mutex) {
        delegate().clear();
      }
    }

    public boolean contains(Object o) {
      synchronized (mutex) {
        return delegate().contains(o);
      }
    }

    public boolean containsAll(Collection<?> c) {
      synchronized (mutex) {
        return delegate().containsAll(c);
      }
    }

    public boolean isEmpty() {
      synchronized (mutex) {
        return delegate().isEmpty();
      }
    }

    public Iterator<E> iterator() {
      return delegate().iterator(); // manually synchronized
    }

    public boolean remove(Object o) {
      synchronized (mutex) {
        return delegate().remove(o);
      }
    }

    public boolean removeAll(Collection<?> c) {
      synchronized (mutex) {
        return delegate().removeAll(c);
      }
    }

    public boolean retainAll(Collection<?> c) {
      synchronized (mutex) {
        return delegate().retainAll(c);
      }
    }

    public int size() {
      synchronized (mutex) {
        return delegate().size();
      }
    }

    public Object[] toArray() {
      synchronized (mutex) {
        return delegate().toArray();
      }
    }

    public <T> T[] toArray(T[] a) {
      synchronized (mutex) {
        return delegate().toArray(a);
      }
    }

    private static final long serialVersionUID = 0;
  }

  @VisibleForTesting 
  static <E> Set<E> set(Set<E> set, Object mutex) {
    return new SynchronizedSet<E>(set, mutex);
  }

  static class SynchronizedSet<E>
      extends SynchronizedCollection<E> implements Set<E> {
    
    SynchronizedSet(Set<E> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @Override Set<E> delegate() {
      return (Set<E>) super.delegate();
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }

    @Override public int hashCode() {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }

    private static final long serialVersionUID = 0;
  }

  private static <E> SortedSet<E> sortedSet(
      SortedSet<E> set, Object mutex) {
    return new SynchronizedSortedSet<E>(set, mutex);
  }

  static class SynchronizedSortedSet<E> extends SynchronizedSet<E>
      implements SortedSet<E> {
    SynchronizedSortedSet(SortedSet<E> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @Override SortedSet<E> delegate() {
      return (SortedSet<E>) super.delegate();
    }

    public Comparator<? super E> comparator() {
      synchronized (mutex) {
        return delegate().comparator();
      }
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
      synchronized (mutex) {
        return sortedSet(delegate().subSet(fromElement, toElement), mutex);
      }
    }

    public SortedSet<E> headSet(E toElement) {
      synchronized (mutex) {
        return sortedSet(delegate().headSet(toElement), mutex);
      }
    }

    public SortedSet<E> tailSet(E fromElement) {
      synchronized (mutex) {
        return sortedSet(delegate().tailSet(fromElement), mutex);
      }
    }

    public E first() {
      synchronized (mutex) {
        return delegate().first();
      }
    }

    public E last() {
      synchronized (mutex) {
        return delegate().last();
      }
    }

    private static final long serialVersionUID = 0;
  }

  private static <E> List<E> list(List<E> list, Object mutex) {
    return (list instanceof RandomAccess)
        ? new SynchronizedRandomAccessList<E>(list, mutex)
        : new SynchronizedList<E>(list, mutex);
  }

  private static class SynchronizedList<E> extends SynchronizedCollection<E>
      implements List<E> {
    SynchronizedList(List<E> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @Override List<E> delegate() {
      return (List<E>) super.delegate();
    }

    public void add(int index, E element) {
      synchronized (mutex) {
        delegate().add(index, element);
      }
    }

    public boolean addAll(int index, Collection<? extends E> c) {
      synchronized (mutex) {
        return delegate().addAll(index, c);
      }
    }

    public E get(int index) {
      synchronized (mutex) {
        return delegate().get(index);
      }
    }

    public int indexOf(Object o) {
      synchronized (mutex) {
        return delegate().indexOf(o);
      }
    }

    public int lastIndexOf(Object o) {
      synchronized (mutex) {
        return delegate().lastIndexOf(o);
      }
    }

    public ListIterator<E> listIterator() {
      return delegate().listIterator(); // manually synchronized
    }

    public ListIterator<E> listIterator(int index) {
      return delegate().listIterator(index); // manually synchronized
    }

    public E remove(int index) {
      synchronized (mutex) {
        return delegate().remove(index);
      }
    }

    public E set(int index, E element) {
      synchronized (mutex) {
        return delegate().set(index, element);
      }
    }

    public List<E> subList(int fromIndex, int toIndex) {
      synchronized (mutex) {
        return list(delegate().subList(fromIndex, toIndex), mutex);
      }
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }

    @Override public int hashCode() {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }

    private static final long serialVersionUID = 0;
  }

  private static class SynchronizedRandomAccessList<E>
      extends SynchronizedList<E> implements RandomAccess {
    SynchronizedRandomAccessList(List<E> list, Object mutex) {
      super(list, mutex);
    }
    private static final long serialVersionUID = 0;
  }

  static <E> Multiset<E> multiset(
      Multiset<E> multiset, Object mutex) {
    return new SynchronizedMultiset<E>(multiset, mutex);
  }

  private static class SynchronizedMultiset<E> extends SynchronizedCollection<E>
      implements Multiset<E> {
    transient Set<E> elementSet;
    transient Set<Entry<E>> entrySet;

    SynchronizedMultiset(Multiset<E> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @Override Multiset<E> delegate() {
      return (Multiset<E>) super.delegate();
    }

    public int count(Object o) {
      synchronized (mutex) {
        return delegate().count(o);
      }
    }

    public int add(E e, int n) {
      synchronized (mutex) {
        return delegate().add(e, n);
      }
    }

    public int remove(Object o, int n) {
      synchronized (mutex) {
        return delegate().remove(o, n);
      }
    }

    public int setCount(E element, int count) {
      synchronized (mutex) {
        return delegate().setCount(element, count);
      }
    }

    public boolean setCount(E element, int oldCount, int newCount) {
      synchronized (mutex) {
        return delegate().setCount(element, oldCount, newCount);
      }
    }

    public Set<E> elementSet() {
      synchronized (mutex) {
        if (elementSet == null) {
          elementSet = typePreservingSet(delegate().elementSet(), mutex);
        }
        return elementSet;
      }
    }

    public Set<Entry<E>> entrySet() {
      synchronized (mutex) {
        if (entrySet == null) {
          entrySet = typePreservingSet(delegate().entrySet(), mutex);
        }
        return entrySet;
      }
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }

    @Override public int hashCode() {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }

    private static final long serialVersionUID = 0;
  }

  static <K, V> Multimap<K, V> multimap(
      Multimap<K, V> multimap, Object mutex) {
    return new SynchronizedMultimap<K, V>(multimap, mutex);
  }

  private static class SynchronizedMultimap<K, V> extends SynchronizedObject
      implements Multimap<K, V> {
    transient Set<K> keySet;
    transient Collection<V> valuesCollection;
    transient Collection<Map.Entry<K, V>> entries;
    transient Map<K, Collection<V>> asMap;
    transient Multiset<K> keys;

    @SuppressWarnings("unchecked")
    @Override Multimap<K, V> delegate() {
      return (Multimap<K, V>) super.delegate();
    }

    SynchronizedMultimap(Multimap<K, V> delegate, Object mutex) {
      super(delegate, mutex);
    }

    public int size() {
      synchronized (mutex) {
        return delegate().size();
      }
    }

    public boolean isEmpty() {
      synchronized (mutex) {
        return delegate().isEmpty();
      }
    }

    public boolean containsKey(Object key) {
      synchronized (mutex) {
        return delegate().containsKey(key);
      }
    }

    public boolean containsValue(Object value) {
      synchronized (mutex) {
        return delegate().containsValue(value);
      }
    }

    public boolean containsEntry(Object key, Object value) {
      synchronized (mutex) {
        return delegate().containsEntry(key, value);
      }
    }

    public Collection<V> get(K key) {
      synchronized (mutex) {
        return typePreservingCollection(delegate().get(key), mutex);
      }
    }

    public boolean put(K key, V value) {
      synchronized (mutex) {
        return delegate().put(key, value);
      }
    }

    public boolean putAll(K key, Iterable<? extends V> values) {
      synchronized (mutex) {
        return delegate().putAll(key, values);
      }
    }

    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
      synchronized (mutex) {
        return delegate().putAll(multimap);
      }
    }

    public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
      synchronized (mutex) {
        return delegate().replaceValues(key, values); // copy not synchronized
      }
    }

    public boolean remove(Object key, Object value) {
      synchronized (mutex) {
        return delegate().remove(key, value);
      }
    }

    public Collection<V> removeAll(Object key) {
      synchronized (mutex) {
        return delegate().removeAll(key); // copy not synchronized
      }
    }

    public void clear() {
      synchronized (mutex) {
        delegate().clear();
      }
    }

    public Set<K> keySet() {
      synchronized (mutex) {
        if (keySet == null) {
          keySet = typePreservingSet(delegate().keySet(), mutex);
        }
        return keySet;
      }
    }

    public Collection<V> values() {
      synchronized (mutex) {
        if (valuesCollection == null) {
          valuesCollection = collection(delegate().values(), mutex);
        }
        return valuesCollection;
      }
    }

    public Collection<Map.Entry<K, V>> entries() {
      synchronized (mutex) {
        if (entries == null) {
          entries = typePreservingCollection(delegate().entries(), mutex);
        }
        return entries;
      }
    }

    public Map<K, Collection<V>> asMap() {
      synchronized (mutex) {
        if (asMap == null) {
          asMap = new SynchronizedAsMap<K, V>(delegate().asMap(), mutex);
        }
        return asMap;
      }
    }

    public Multiset<K> keys() {
      synchronized (mutex) {
        if (keys == null) {
          keys = multiset(delegate().keys(), mutex);
        }
        return keys;
      }
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }

    @Override public int hashCode() {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }

    private static final long serialVersionUID = 0;
  }

  static <K, V> ListMultimap<K, V> listMultimap(
      ListMultimap<K, V> multimap, Object mutex) {
    return new SynchronizedListMultimap<K, V>(multimap, mutex);
  }

  private static class SynchronizedListMultimap<K, V>
      extends SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
    SynchronizedListMultimap(
        ListMultimap<K, V> delegate, Object mutex) {
      super(delegate, mutex);
    }
    @Override ListMultimap<K, V> delegate() {
      return (ListMultimap<K, V>) super.delegate();
    }
    @Override public List<V> get(K key) {
      synchronized (mutex) {
        return list(delegate().get(key), mutex);
      }
    }
    @Override public List<V> removeAll(Object key) {
      synchronized (mutex) {
        return delegate().removeAll(key); // copy not synchronized
      }
    }
    @Override public List<V> replaceValues(
        K key, Iterable<? extends V> values) {
      synchronized (mutex) {
        return delegate().replaceValues(key, values); // copy not synchronized
      }
    }
    private static final long serialVersionUID = 0;
  }

  static <K, V> SetMultimap<K, V> setMultimap(
      SetMultimap<K, V> multimap, Object mutex) {
    return new SynchronizedSetMultimap<K, V>(multimap, mutex);
  }

  private static class SynchronizedSetMultimap<K, V>
      extends SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
    transient Set<Map.Entry<K, V>> entrySet;

    SynchronizedSetMultimap(
        SetMultimap<K, V> delegate, Object mutex) {
      super(delegate, mutex);
    }
    @Override SetMultimap<K, V> delegate() {
      return (SetMultimap<K, V>) super.delegate();
    }
    @Override public Set<V> get(K key) {
      synchronized (mutex) {
        return set(delegate().get(key), mutex);
      }
    }
    @Override public Set<V> removeAll(Object key) {
      synchronized (mutex) {
        return delegate().removeAll(key); // copy not synchronized
      }
    }
    @Override public Set<V> replaceValues(
        K key, Iterable<? extends V> values) {
      synchronized (mutex) {
        return delegate().replaceValues(key, values); // copy not synchronized
      }
    }
    @Override public Set<Map.Entry<K, V>> entries() {
      synchronized (mutex) {
        if (entrySet == null) {
          entrySet = set(delegate().entries(), mutex);
        }
        return entrySet;
      }
    }
    private static final long serialVersionUID = 0;
  }

  static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(
      SortedSetMultimap<K, V> multimap, Object mutex) {
    return new SynchronizedSortedSetMultimap<K, V>(multimap, mutex);
  }

  private static class SynchronizedSortedSetMultimap<K, V>
      extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
    SynchronizedSortedSetMultimap(
        SortedSetMultimap<K, V> delegate, Object mutex) {
      super(delegate, mutex);
    }
    @Override SortedSetMultimap<K, V> delegate() {
      return (SortedSetMultimap<K, V>) super.delegate();
    }
    @Override public SortedSet<V> get(K key) {
      synchronized (mutex) {
        return sortedSet(delegate().get(key), mutex);
      }
    }
    @Override public SortedSet<V> removeAll(Object key) {
      synchronized (mutex) {
        return delegate().removeAll(key); // copy not synchronized
      }
    }
    @Override public SortedSet<V> replaceValues(
        K key, Iterable<? extends V> values) {
      synchronized (mutex) {
        return delegate().replaceValues(key, values); // copy not synchronized
      }
    }
    public Comparator<? super V> valueComparator() {
      synchronized (mutex) {
        return delegate().valueComparator();
      }
    }
    private static final long serialVersionUID = 0;
  }

  private static <E> Collection<E> typePreservingCollection(
      Collection<E> collection, Object mutex) {
    if (collection instanceof SortedSet) {
      return sortedSet((SortedSet<E>) collection, mutex);
    }
    if (collection instanceof Set) {
      return set((Set<E>) collection, mutex);
    }
    if (collection instanceof List) {
      return list((List<E>) collection, mutex);
    }
    return collection(collection, mutex);
  }

  private static <E> Set<E> typePreservingSet(
      Set<E> set, Object mutex) {
    if (set instanceof SortedSet) {
      return sortedSet((SortedSet<E>) set, mutex);
    } else {
      return set(set, mutex);
    }
  }

  private static class SynchronizedAsMapEntries<K, V>
      extends SynchronizedSet<Map.Entry<K, Collection<V>>> {
    SynchronizedAsMapEntries(
        Set<Map.Entry<K, Collection<V>>> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @Override public Iterator<Map.Entry<K, Collection<V>>> iterator() {
      // Must be manually synchronized.
      final Iterator<Map.Entry<K, Collection<V>>> iterator = super.iterator();
      return new ForwardingIterator<Map.Entry<K, Collection<V>>>() {
        @Override protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
          return iterator;
        }

        @Override public Map.Entry<K, Collection<V>> next() {
          final Map.Entry<K, Collection<V>> entry = iterator.next();
          return new ForwardingMapEntry<K, Collection<V>>() {
            @Override protected Map.Entry<K, Collection<V>> delegate() {
              return entry;
            }
            @Override public Collection<V> getValue() {
              return typePreservingCollection(entry.getValue(), mutex);
            }
          };
        }
      };
    }

    // See Collections.CheckedMap.CheckedEntrySet for details on attacks.

    @Override public Object[] toArray() {
      synchronized (mutex) {
        return ObjectArrays.toArrayImpl(delegate());
      }
    }
    @Override public <T> T[] toArray(T[] array) {
      synchronized (mutex) {
        return ObjectArrays.toArrayImpl(delegate(), array);
      }
    }
    @Override public boolean contains(Object o) {
      synchronized (mutex) {
        return Maps.containsEntryImpl(delegate(), o);
      }
    }
    @Override public boolean containsAll(Collection<?> c) {
      synchronized (mutex) {
        return Collections2.containsAllImpl(delegate(), c);
      }
    }
    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return Sets.equalsImpl(delegate(), o);
      }
    }
    @Override public boolean remove(Object o) {
      synchronized (mutex) {
        return Maps.removeEntryImpl(delegate(), o);
      }
    }
    @Override public boolean removeAll(Collection<?> c) {
      synchronized (mutex) {
        return Iterators.removeAll(delegate().iterator(), c);
      }
    }
    @Override public boolean retainAll(Collection<?> c) {
      synchronized (mutex) {
        return Iterators.retainAll(delegate().iterator(), c);
      }
    }

    private static final long serialVersionUID = 0;
  }

  @VisibleForTesting
  static <K, V> Map<K, V> map(Map<K, V> map, Object mutex) {
    return new SynchronizedMap<K, V>(map, mutex);
  }

  private static class SynchronizedMap<K, V> extends SynchronizedObject
      implements Map<K, V> {
    transient Set<K> keySet;
    transient Collection<V> values;
    transient Set<Map.Entry<K, V>> entrySet;

    SynchronizedMap(Map<K, V> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @SuppressWarnings("unchecked")
    @Override Map<K, V> delegate() {
      return (Map<K, V>) super.delegate();
    }

    public void clear() {
      synchronized (mutex) {
        delegate().clear();
      }
    }

    public boolean containsKey(Object key) {
      synchronized (mutex) {
        return delegate().containsKey(key);
      }
    }

    public boolean containsValue(Object value) {
      synchronized (mutex) {
        return delegate().containsValue(value);
      }
    }

    public Set<Map.Entry<K, V>> entrySet() {
      synchronized (mutex) {
        if (entrySet == null) {
          entrySet = set(delegate().entrySet(), mutex);
        }
        return entrySet;
      }
    }

    public V get(Object key) {
      synchronized (mutex) {
        return delegate().get(key);
      }
    }

    public boolean isEmpty() {
      synchronized (mutex) {
        return delegate().isEmpty();
      }
    }

    public Set<K> keySet() {
      synchronized (mutex) {
        if (keySet == null) {
          keySet = set(delegate().keySet(), mutex);
        }
        return keySet;
      }
    }

    public V put(K key, V value) {
      synchronized (mutex) {
        return delegate().put(key, value);
      }
    }

    public void putAll(Map<? extends K, ? extends V> map) {
      synchronized (mutex) {
        delegate().putAll(map);
      }
    }

    public V remove(Object key) {
      synchronized (mutex) {
        return delegate().remove(key);
      }
    }

    public int size() {
      synchronized (mutex) {
        return delegate().size();
      }
    }

    public Collection<V> values() {
      synchronized (mutex) {
        if (values == null) {
          values = collection(delegate().values(), mutex);
        }
        return values;
      }
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }

    @Override public int hashCode() {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }

    private static final long serialVersionUID = 0;
  }
  
  static <K, V> SortedMap<K, V> sortedMap(
      SortedMap<K, V> sortedMap, Object mutex) {
    return new SynchronizedSortedMap<K, V>(sortedMap, mutex);
  }
  
  static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V>
      implements SortedMap<K, V> {

    SynchronizedSortedMap(SortedMap<K, V> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @Override SortedMap<K, V> delegate() {
      return (SortedMap<K, V>) super.delegate();
    }

    @Override public Comparator<? super K> comparator() {
      synchronized (mutex) {
        return delegate().comparator();
      }
    }

    @Override public K firstKey() {
      synchronized (mutex) {
        return delegate().firstKey();
      }
    }

    @Override public SortedMap<K, V> headMap(K toKey) {
      synchronized (mutex) {
        return sortedMap(delegate().headMap(toKey), mutex);
      }
    }

    @Override public K lastKey() {
      synchronized (mutex) {
        return delegate().lastKey();
      }
    }

    @Override public SortedMap<K, V> subMap(K fromKey, K toKey) {
      synchronized (mutex) {
        return sortedMap(delegate().subMap(fromKey, toKey), mutex);
      }
    }

    @Override public SortedMap<K, V> tailMap(K fromKey) {
      synchronized (mutex) {
        return sortedMap(delegate().tailMap(fromKey), mutex);
      }
    }

    private static final long serialVersionUID = 0;
  }

  static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, Object mutex) {
    return new SynchronizedBiMap<K, V>(bimap, mutex, null);
  }

  @VisibleForTesting static class SynchronizedBiMap<K, V>
      extends SynchronizedMap<K, V> implements BiMap<K, V>, Serializable {
    private transient Set<V> valueSet;
    private transient BiMap<V, K> inverse;

    private SynchronizedBiMap(BiMap<K, V> delegate, Object mutex,
        BiMap<V, K> inverse) {
      super(delegate, mutex);
      this.inverse = inverse;
    }

    @Override BiMap<K, V> delegate() {
      return (BiMap<K, V>) super.delegate();
    }

    @Override public Set<V> values() {
      synchronized (mutex) {
        if (valueSet == null) {
          valueSet = set(delegate().values(), mutex);
        }
        return valueSet;
      }
    }

    public V forcePut(K key, V value) {
      synchronized (mutex) {
        return delegate().forcePut(key, value);
      }
    }

    public BiMap<V, K> inverse() {
      synchronized (mutex) {
        if (inverse == null) {
          inverse
              = new SynchronizedBiMap<V, K>(delegate().inverse(), mutex, this);
        }
        return inverse;
      }
    }

    private static final long serialVersionUID = 0;
  }

  private static class SynchronizedAsMap<K, V>
      extends SynchronizedMap<K, Collection<V>> {
    transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
    transient Collection<Collection<V>> asMapValues;

    SynchronizedAsMap(Map<K, Collection<V>> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @Override public Collection<V> get(Object key) {
      synchronized (mutex) {
        Collection<V> collection = super.get(key);
        return (collection == null) ? null
            : typePreservingCollection(collection, mutex);
      }
    }

    @Override public Set<Map.Entry<K, Collection<V>>> entrySet() {
      synchronized (mutex) {
        if (asMapEntrySet == null) {
          asMapEntrySet = new SynchronizedAsMapEntries<K, V>(
              delegate().entrySet(), mutex);
        }
        return asMapEntrySet;
      }
    }

    @Override public Collection<Collection<V>> values() {
      synchronized (mutex) {
        if (asMapValues == null) {
          asMapValues
              = new SynchronizedAsMapValues<V>(delegate().values(), mutex);
        }
        return asMapValues;
      }
    }

    @Override public boolean containsValue(Object o) {
      // values() and its contains() method are both synchronized.
      return values().contains(o);
    }

    private static final long serialVersionUID = 0;
  }

  private static class SynchronizedAsMapValues<V>
      extends SynchronizedCollection<Collection<V>> {
    SynchronizedAsMapValues(
        Collection<Collection<V>> delegate, Object mutex) {
      super(delegate, mutex);
    }

    @Override public Iterator<Collection<V>> iterator() {
      // Must be manually synchronized.
      final Iterator<Collection<V>> iterator = super.iterator();
      return new ForwardingIterator<Collection<V>>() {
        @Override protected Iterator<Collection<V>> delegate() {
          return iterator;
        }
        @Override public Collection<V> next() {
          return typePreservingCollection(iterator.next(), mutex);
        }
      };
    }

    private static final long serialVersionUID = 0;
  }
}
