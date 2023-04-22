package com.yida.spider4j.crawler.utils.collection;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;
import com.yida.spider4j.crawler.utils.collection.base.Objects;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * A general-purpose bimap implementation using any two backing {@code Map}
 * instances.
 *
 * <p>Note that this class contains {@code equals()} calls that keep it from
 * supporting {@code IdentityHashMap} backing maps.
 *
 * @author Kevin Bourrillion
 * @author Mike Bostock
 */
@GwtCompatible(emulated = true)
abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V>
    implements BiMap<K, V>, Serializable {

  private transient Map<K, V> delegate;
  private transient AbstractBiMap<V, K> inverse;

  /** Package-private constructor for creating a map-backed bimap. */
  AbstractBiMap(Map<K, V> forward, Map<V, K> backward) {
    setDelegates(forward, backward);
  }

  /** Private constructor for inverse bimap. */
  private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
    delegate = backward;
    inverse = forward;
  }

  @Override protected Map<K, V> delegate() {
    return delegate;
  }

  /**
   * Specifies the delegate maps going in each direction. Called by the
   * constructor and by subclasses during deserialization.
   */
  void setDelegates(Map<K, V> forward, Map<V, K> backward) {
    Preconditions.checkState(delegate == null);
    Preconditions.checkState(inverse == null);
    Preconditions.checkArgument(forward.isEmpty());
    Preconditions.checkArgument(backward.isEmpty());
    Preconditions.checkArgument(forward != backward);
    delegate = forward;
    inverse = new Inverse<V, K>(backward, this);
  }

  void setInverse(AbstractBiMap<V, K> inverse) {
    this.inverse = inverse;
  }

  // Query Operations (optimizations)

  @Override public boolean containsValue(Object value) {
    return inverse.containsKey(value);
  }

  // Modification Operations

  @Override public V put(K key, V value) {
    return putInBothMaps(key, value, false);
  }

  public V forcePut(K key, V value) {
    return putInBothMaps(key, value, true);
  }

  private V putInBothMaps(K key, V value, boolean force) {
    boolean containedKey = containsKey(key);
    if (containedKey && Objects.equal(value, get(key))) {
      return value;
    }
    if (force) {
      inverse().remove(value);
    } else {
      Preconditions.checkArgument(!containsValue(value), "value already present: %s", value);
    }
    V oldValue = delegate.put(key, value);
    updateInverseMap(key, containedKey, oldValue, value);
    return oldValue;
  }

  private void updateInverseMap(
      K key, boolean containedKey, V oldValue, V newValue) {
    if (containedKey) {
      removeFromInverseMap(oldValue);
    }
    inverse.delegate.put(newValue, key);
  }

  @Override public V remove(Object key) {
    return containsKey(key) ? removeFromBothMaps(key) : null;
  }

  private V removeFromBothMaps(Object key) {
    V oldValue = delegate.remove(key);
    removeFromInverseMap(oldValue);
    return oldValue;
  }

  private void removeFromInverseMap(V oldValue) {
    inverse.delegate.remove(oldValue);
  }

  // Bulk Operations

  @Override public void putAll(Map<? extends K, ? extends V> map) {
    for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override public void clear() {
    delegate.clear();
    inverse.delegate.clear();
  }

  // Views

  public BiMap<V, K> inverse() {
    return inverse;
  }

  private transient Set<K> keySet;

  @Override public Set<K> keySet() {
    Set<K> result = keySet;
    return (result == null) ? keySet = new KeySet() : result;
  }

  private class KeySet extends ForwardingSet<K> {
    @Override protected Set<K> delegate() {
      return delegate.keySet();
    }

    @Override public void clear() {
      AbstractBiMap.this.clear();
    }

    @Override public boolean remove(Object key) {
      if (!contains(key)) {
        return false;
      }
      removeFromBothMaps(key);
      return true;
    }

    @Override public boolean removeAll(Collection<?> keysToRemove) {
      return standardRemoveAll(keysToRemove);
    }

    @Override public boolean retainAll(Collection<?> keysToRetain) {
      return standardRetainAll(keysToRetain);
    }

    @Override public Iterator<K> iterator() {
      final Iterator<Entry<K, V>> iterator = delegate.entrySet().iterator();
      return new Iterator<K>() {
        Entry<K, V> entry;

        public boolean hasNext() {
          return iterator.hasNext();
        }
        public K next() {
          entry = iterator.next();
          return entry.getKey();
        }
        public void remove() {
          Preconditions.checkState(entry != null);
          V value = entry.getValue();
          iterator.remove();
          removeFromInverseMap(value);
        }
      };
    }
  }

  private transient Set<V> valueSet;

  @Override public Set<V> values() {
    /*
     * We can almost reuse the inverse's keySet, except we have to fix the
     * iteration order so that it is consistent with the forward map.
     */
    Set<V> result = valueSet;
    return (result == null) ? valueSet = new ValueSet() : result;
  }

  private class ValueSet extends ForwardingSet<V> {
    final Set<V> valuesDelegate = inverse.keySet();

    @Override protected Set<V> delegate() {
      return valuesDelegate;
    }

    @Override public Iterator<V> iterator() {
      final Iterator<V> iterator = delegate.values().iterator();
      return new Iterator<V>() {
        V valueToRemove;

        @Override public boolean hasNext() {
          return iterator.hasNext();
        }

        @Override public V next() {
          return valueToRemove = iterator.next();
        }

        @Override public void remove() {
          iterator.remove();
          removeFromInverseMap(valueToRemove);
        }
      };
    }

    @Override public Object[] toArray() {
      return standardToArray();
    }

    @Override public <T> T[] toArray(T[] array) {
      return standardToArray(array);
    }

    @Override public String toString() {
      return standardToString();
    }
  }

  private transient Set<Entry<K, V>> entrySet;

  @Override public Set<Entry<K, V>> entrySet() {
    Set<Entry<K, V>> result = entrySet;
    return (result == null) ? entrySet = new EntrySet() : result;
  }

  private class EntrySet extends ForwardingSet<Entry<K, V>> {
    final Set<Entry<K, V>> esDelegate = delegate.entrySet();

    @Override protected Set<Entry<K, V>> delegate() {
      return esDelegate;
    }

    @Override public void clear() {
      AbstractBiMap.this.clear();
    }

    @Override public boolean remove(Object object) {
      if (!esDelegate.remove(object)) {
        return false;
      }
      Entry<?, ?> entry = (Entry<?, ?>) object;
      inverse.delegate.remove(entry.getValue());
      return true;
    }

    @Override public Iterator<Entry<K, V>> iterator() {
      final Iterator<Entry<K, V>> iterator = esDelegate.iterator();
      return new Iterator<Entry<K, V>>() {
        Entry<K, V> entry;

        @Override public boolean hasNext() {
          return iterator.hasNext();
        }

        @Override public Entry<K, V> next() {
          entry = iterator.next();
          final Entry<K, V> finalEntry = entry;

          return new ForwardingMapEntry<K, V>() {
            @Override protected Entry<K, V> delegate() {
              return finalEntry;
            }

            @Override public V setValue(V value) {
              // Preconditions keep the map and inverse consistent.
              Preconditions.checkState(contains(this), "entry no longer in map");
              // similar to putInBothMaps, but set via entry
              if (Objects.equal(value, getValue())) {
                return value;
              }
              Preconditions.checkArgument(!containsValue(value),
                  "value already present: %s", value);
              V oldValue = finalEntry.setValue(value);
              Preconditions.checkState(Objects.equal(value, get(getKey())),
                  "entry no longer in map");
              updateInverseMap(getKey(), true, oldValue, value);
              return oldValue;
            }
          };
        }

        @Override public void remove() {
          Preconditions.checkState(entry != null);
          V value = entry.getValue();
          iterator.remove();
          removeFromInverseMap(value);
        }
      };
    }

    // See java.util.Collections.CheckedEntrySet for details on attacks.

    @Override public Object[] toArray() {
      return standardToArray();
    }
    @Override public <T> T[] toArray(T[] array) {
      return standardToArray(array);
    }
    @Override public boolean contains(Object o) {
      return Maps.containsEntryImpl(delegate(), o);
    }
    @Override public boolean containsAll(Collection<?> c) {
      return standardContainsAll(c);
    }
    @Override public boolean removeAll(Collection<?> c) {
      return standardRemoveAll(c);
    }
    @Override public boolean retainAll(Collection<?> c) {
      return standardRetainAll(c);
    }
  }

  /** The inverse of any other {@code AbstractBiMap} subclass. */
  private static class Inverse<K, V> extends AbstractBiMap<K, V> {
    private Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) {
      super(backward, forward);
    }

    /*
     * Serialization stores the forward bimap, the inverse of this inverse.
     * Deserialization calls inverse() on the forward bimap and returns that
     * inverse.
     *
     * If a bimap and its inverse are serialized together, the deserialized
     * instances have inverse() methods that return the other.
     */

    /**
     * @serialData the forward bimap
     */
    @GwtIncompatible("java.io.ObjectOuputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
      stream.defaultWriteObject();
      stream.writeObject(inverse());
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    @SuppressWarnings("unchecked") // reading data stored by writeObject
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
      stream.defaultReadObject();
      setInverse((AbstractBiMap<V, K>) stream.readObject());
    }

    @GwtIncompatible("Not needed in the emulated source.")
    Object readResolve() {
      return inverse().inverse();
    }

    @GwtIncompatible("Not needed in emulated source.")
    private static final long serialVersionUID = 0;
  }

  @GwtIncompatible("Not needed in emulated source.")
  private static final long serialVersionUID = 0;
}
