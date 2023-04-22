package com.yida.spider4j.crawler.utils.collection;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;
import com.yida.spider4j.crawler.utils.collection.base.Function;

/**
 * A class exactly like {@link MapMaker}, except restricted in the types of maps
 * it can build. This type is returned by {@link MapMaker#evictionListener} to
 * prevent the user from trying to build a map that's incompatible with the key
 * and value types of the listener.
 *
 * @param <K0> the base type for all key types of maps built by this map maker
 * @param <V0> the base type for all value types of maps built by this map maker
 * @author Kevin Bourrillion
 * @since 7
 */
@Beta
@GwtCompatible(emulated = true)
public abstract class GenericMapMaker<K0, V0> {
  // Set by MapMaker, but sits in this class to preserve the type relationship
  @GwtIncompatible("To be supported")
  MapEvictionListener<K0, V0> evictionListener;

  // No subclasses but our own
  GenericMapMaker() {}

  /**
   * See {@link MapMaker#initialCapacity}.
   */
  public abstract GenericMapMaker<K0, V0> initialCapacity(int initialCapacity);

  /**
   * See {@link MapMaker#maximumSize}.
   *
   * @since 8
   */
  @Beta
  @GwtIncompatible("To be supported")
  public abstract GenericMapMaker<K0, V0> maximumSize(int maximumSize);

  /**
   * See {@link MapMaker#concurrencyLevel}.
   */
  @GwtIncompatible("java.util.concurrent.ConcurrentHashMap concurrencyLevel")
  public abstract GenericMapMaker<K0, V0> concurrencyLevel(
      int concurrencyLevel);

  /**
   * See {@link MapMaker#weakKeys}.
   */
  @GwtIncompatible("java.lang.ref.WeakReference")
  public abstract GenericMapMaker<K0, V0> weakKeys();

  /**
   * See {@link MapMaker#softKeys}.
   */
  @GwtIncompatible("java.lang.ref.SoftReference")
  public abstract GenericMapMaker<K0, V0> softKeys();

  /**
   * See {@link MapMaker#weakValues}.
   */
  @GwtIncompatible("java.lang.ref.WeakReference")
  public abstract GenericMapMaker<K0, V0> weakValues();

  /**
   * See {@link MapMaker#softValues}.
   */
  @GwtIncompatible("java.lang.ref.SoftReference")
  public abstract GenericMapMaker<K0, V0> softValues();

  /**
   * See {@link MapMaker#expiration}.
   */
  // TODO(user): deprecate
  public abstract GenericMapMaker<K0, V0> expiration(
      long duration, TimeUnit unit);

  /**
   * See {@link MapMaker#expireAfterWrite}.
   *
   * @since 8
   */
  @Beta
  public abstract GenericMapMaker<K0, V0> expireAfterWrite(
      long duration, TimeUnit unit);

  /**
   * See {@link MapMaker#expireAfterAccess}.
   *
   * @since 8
   */
  @Beta
  @GwtIncompatible("To be supported")
  public abstract GenericMapMaker<K0, V0> expireAfterAccess(
      long duration, TimeUnit unit);

  /*
   * Note that MapMaker's evictionListener() is not here, because once you're
   * interacting with a GenericMapMaker you've already called that, and
   * shouldn't be calling it again.
   */

  /**
   * See {@link MapMaker#makeMap}.
   */
  public abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeMap();

  /**
   * See {@link MapMaker#makeComputingMap}.
   */
  public abstract <K extends K0, V extends V0> ConcurrentMap<K, V>
      makeComputingMap(Function<? super K, ? extends V> computingFunction);
}
