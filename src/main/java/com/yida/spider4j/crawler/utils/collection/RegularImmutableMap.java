package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.ImmutableSet.ArrayImmutableSet;
import com.yida.spider4j.crawler.utils.collection.ImmutableSet.TransformedImmutableSet;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.Immutable;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Implementation of {@link ImmutableMap} with two or more entries.
 *
 * @author Jesse Wilson
 * @author Kevin Bourrillion
 * @author Gregory Kick
 */
@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {

  // entries in insertion order
  private final transient LinkedEntry<K, V>[] entries;
  // array of linked lists of entries
  private final transient LinkedEntry<K, V>[] table;
  // 'and' with an int to get a table index
  private final transient int mask;
  private final transient int keySetHashCode;

  // TODO(gak): investigate avoiding the creation of ImmutableEntries since we
  // re-copy them anyway.
  RegularImmutableMap(Entry<?, ?>... immutableEntries) {
    int size = immutableEntries.length;
    entries = createEntryArray(size);

    int tableSize = chooseTableSize(size);
    table = createEntryArray(tableSize);
    mask = tableSize - 1;

    int keySetHashCodeMutable = 0;
    for (int entryIndex = 0; entryIndex < size; entryIndex++) {
      // each of our 6 callers carefully put only Entry<K, V>s into the array!
      @SuppressWarnings("unchecked")
      Entry<K, V> entry = (Entry<K, V>) immutableEntries[entryIndex];
      K key = entry.getKey();
      int keyHashCode = key.hashCode();
      keySetHashCodeMutable += keyHashCode;
      int tableIndex = Hashing.smear(keyHashCode) & mask;
      LinkedEntry<K, V> existing = table[tableIndex];
      // prepend, not append, so the entries can be immutable
      LinkedEntry<K, V> linkedEntry =
          newLinkedEntry(key, entry.getValue(), existing);
      table[tableIndex] = linkedEntry;
      entries[entryIndex] = linkedEntry;
      while (existing != null) {
        Preconditions.checkArgument(!key.equals(existing.getKey()), "duplicate key: %s", key);
        existing = existing.next();
      }
    }
    keySetHashCode = keySetHashCodeMutable;
  }

  private static int chooseTableSize(int size) {
    // least power of 2 greater than size
    int tableSize = Integer.highestOneBit(size) << 1;
    Preconditions.checkArgument(tableSize > 0, "table too large: %s", size);
    return tableSize;
  }

  /**
   * Creates a {@link LinkedEntry} array to hold parameterized entries. The
   * result must never be upcast back to LinkedEntry[] (or Object[], etc.), or
   * allowed to escape the class.
   */
  @SuppressWarnings("unchecked") // Safe as long as the javadocs are followed
  private LinkedEntry<K, V>[] createEntryArray(int size) {
    return new LinkedEntry[size];
  }

  private static <K, V> LinkedEntry<K, V> newLinkedEntry(K key, V value,
      LinkedEntry<K, V> next) {
    return (next == null)
        ? new TerminalEntry<K, V>(key, value)
        : new NonTerminalEntry<K, V>(key, value, next);
  }

  private interface LinkedEntry<K, V> extends Entry<K, V> {
    /** Returns the next entry in the list or {@code null} if none exists. */
    LinkedEntry<K, V> next();
  }

  /** {@link LinkedEntry} implementation that has a next value. */
  @Immutable
  @SuppressWarnings("serial") // this class is never serialized
  private static final class NonTerminalEntry<K, V>
      extends ImmutableEntry<K, V> implements LinkedEntry<K, V> {
    final LinkedEntry<K, V> next;

    NonTerminalEntry(K key, V value, LinkedEntry<K, V> next) {
      super(key, value);
      this.next = next;
    }

    @Override public LinkedEntry<K, V> next() {
      return next;
    }
  }

  /**
   * {@link LinkedEntry} implementation that serves as the last entry in the
   * list.  I.e. no next entry
   */
  @Immutable
  @SuppressWarnings("serial") // this class is never serialized
  private static final class TerminalEntry<K, V> extends ImmutableEntry<K, V>
      implements LinkedEntry<K, V> {
    TerminalEntry(K key, V value) {
      super(key, value);
    }

    @Override public LinkedEntry<K, V> next() {
      return null;
    }
  }

  @Override public V get(Object key) {
    if (key == null) {
      return null;
    }
    int index = Hashing.smear(key.hashCode()) & mask;
    for (LinkedEntry<K, V> entry = table[index];
        entry != null;
        entry = entry.next()) {
      K candidateKey = entry.getKey();

      /*
       * Assume that equals uses the == optimization when appropriate, and that
       * it would check hash codes as an optimization when appropriate. If we
       * did these things, it would just make things worse for the most
       * performance-conscious users.
       */
      if (key.equals(candidateKey)) {
        return entry.getValue();
      }
    }
    return null;
  }

  public int size() {
    return entries.length;
  }

  @Override public boolean isEmpty() {
    return false;
  }

  @Override public boolean containsValue(Object value) {
    if (value == null) {
      return false;
    }
    for (Entry<K, V> entry : entries) {
      if (entry.getValue().equals(value)) {
        return true;
      }
    }
    return false;
  }

  @Override boolean isPartialView() {
    return false;
  }

  private transient ImmutableSet<Entry<K, V>> entrySet;

  @Override public ImmutableSet<Entry<K, V>> entrySet() {
    ImmutableSet<Entry<K, V>> es = entrySet;
    return (es == null) ? (entrySet = new EntrySet<K, V>(this)) : es;
  }

  @SuppressWarnings("serial") // uses writeReplace(), not default serialization
  private static class EntrySet<K, V> extends ArrayImmutableSet<Entry<K, V>> {
    final transient RegularImmutableMap<K, V> map;

    EntrySet(RegularImmutableMap<K, V> map) {
      super(map.entries);
      this.map = map;
    }

    @Override public boolean contains(Object target) {
      if (target instanceof Entry) {
        Entry<?, ?> entry = (Entry<?, ?>) target;
        V mappedValue = map.get(entry.getKey());
        return mappedValue != null && mappedValue.equals(entry.getValue());
      }
      return false;
    }
  }

  private transient ImmutableSet<K> keySet;

  @Override public ImmutableSet<K> keySet() {
    ImmutableSet<K> ks = keySet;
    return (ks == null) ? (keySet = new KeySet<K, V>(this)) : ks;
  }

  @SuppressWarnings("serial") // uses writeReplace(), not default serialization
  private static class KeySet<K, V>
      extends TransformedImmutableSet<Entry<K, V>, K> {
    final RegularImmutableMap<K, V> map;

    KeySet(RegularImmutableMap<K, V> map) {
      super(map.entries, map.keySetHashCode);
      this.map = map;
    }

    @Override K transform(Entry<K, V> element) {
      return element.getKey();
    }

    @Override public boolean contains(Object target) {
      return map.containsKey(target);
    }

    @Override boolean isPartialView() {
      return true;
    }
  }

  private transient ImmutableCollection<V> values;

  @Override public ImmutableCollection<V> values() {
    ImmutableCollection<V> v = values;
    return (v == null) ? (values = new Values<V>(this)) : v;
  }

  @SuppressWarnings("serial") // uses writeReplace(), not default serialization
  private static class Values<V> extends ImmutableCollection<V> {
    final RegularImmutableMap<?, V> map;

    Values(RegularImmutableMap<?, V> map) {
      this.map = map;
    }

    public int size() {
      return map.entries.length;
    }

    @Override public UnmodifiableIterator<V> iterator() {
      return new AbstractIndexedListIterator<V>(map.entries.length) {
        @Override protected V get(int index) {
          return map.entries[index].getValue();
        }
      };
    }

    @Override public boolean contains(Object target) {
      return map.containsValue(target);
    }

    @Override boolean isPartialView() {
      return true;
    }
  }

  @Override public String toString() {
    StringBuilder result
        = Collections2.newStringBuilderForCollection(size()).append('{');
    Collections2.STANDARD_JOINER.appendTo(result, entries);
    return result.append('}').toString();
  }

  // This class is never actually serialized directly, but we have to make the
  // warning go away (and suppressing would suppress for all nested classes too)
  private static final long serialVersionUID = 0;
}
