package com.yida.spider4j.crawler.utils.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;

/**
 * A {@code BiMap} backed by an {@code EnumMap} instance for keys-to-values, and
 * a {@code HashMap} instance for values-to-keys. Null keys are not permitted,
 * but null values are. An {@code EnumHashBiMap} and its inverse are both
 * serializable.
 *
 * @author Mike Bostock
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible(emulated = true)
public final class EnumHashBiMap<K extends Enum<K>, V>
    extends AbstractBiMap<K, V> {
  private transient Class<K> keyType;

  /**
   * Returns a new, empty {@code EnumHashBiMap} using the specified key type.
   *
   * @param keyType the key type
   */
  public static <K extends Enum<K>, V> EnumHashBiMap<K, V>
      create(Class<K> keyType) {
    return new EnumHashBiMap<K, V>(keyType);
  }

  /**
   * Constructs a new bimap with the same mappings as the specified map. If the
   * specified map is an {@code EnumHashBiMap} or an {@link EnumBiMap}, the new
   * bimap has the same key type as the input bimap. Otherwise, the specified
   * map must contain at least one mapping, in order to determine the key type.
   *
   * @param map the map whose mappings are to be placed in this map
   * @throws IllegalArgumentException if map is not an {@code EnumBiMap} or an
   *     {@code EnumHashBiMap} instance and contains no mappings
   */
  public static <K extends Enum<K>, V> EnumHashBiMap<K, V>
      create(Map<K, ? extends V> map) {
    EnumHashBiMap<K, V> bimap = create(EnumBiMap.inferKeyType(map));
    bimap.putAll(map);
    return bimap;
  }

  private EnumHashBiMap(Class<K> keyType) {
    super(new EnumMap<K, V>(keyType), Maps.<V, K>newHashMapWithExpectedSize(
        keyType.getEnumConstants().length));
    this.keyType = keyType;
  }

  // Overriding these two methods to show that values may be null (but not keys)

  @Override public V put(K key, V value) {
    return super.put(key, value);
  }

  @Override public V forcePut(K key, V value) {
    return super.forcePut(key, value);
  }

  /** Returns the associated key type. */
  public Class<K> keyType() {
    return keyType;
  }

  /**
   * @serialData the key class, number of entries, first key, first value,
   *     second key, second value, and so on.
   */
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeObject(keyType);
    Serialization.writeMap(this, stream);
  }

  @SuppressWarnings("unchecked") // reading field populated by writeObject
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    keyType = (Class<K>) stream.readObject();
    setDelegates(new EnumMap<K, V>(keyType),
        new HashMap<V, K>(keyType.getEnumConstants().length * 3 / 2));
    Serialization.populateMap(this, stream);
  }

  @GwtIncompatible("only needed in emulated source.")
  private static final long serialVersionUID = 0;
}
