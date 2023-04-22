package com.yida.spider4j.crawler.utils.hash;

import java.io.Serializable;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;


/**
 * Static factories for creating {@link HashCode} instances; most users should never have to use
 * this. All returned instances are {@link Serializable}.
 *
 * @author Dimitris Andreou
 * @since 12.0
 * @deprecated Use the duplicated methods in {@link HashCode} instead. This class is scheduled
 *     to be removed in Guava 16.0.
 */
@Beta
@Deprecated
public final class HashCodes {
  private HashCodes() {}

  /**
   * Creates a 32-bit {@code HashCode}, of which the bytes will form the passed int, interpreted
   * in little endian order.
   *
   * @deprecated Use {@link HashCode#fromInt} instead. This method is scheduled to be removed in
   *     Guava 16.0.
   */
  @Deprecated
  public static HashCode fromInt(int hash) {
    return HashCode.fromInt(hash);
  }

  /**
   * Creates a 64-bit {@code HashCode}, of which the bytes will form the passed long, interpreted
   * in little endian order.
   *
   * @deprecated Use {@link HashCode#fromLong} instead. This method is scheduled to be removed in
   *     Guava 16.0.
   */
  @Deprecated
  public static HashCode fromLong(long hash) {
    return HashCode.fromLong(hash);
  }

  /**
   * Creates a {@code HashCode} from a byte array. The array is defensively copied to preserve
   * the immutability contract of {@code HashCode}. The array cannot be empty.
   *
   * @deprecated Use {@link HashCode#fromBytes} instead. This method is scheduled to be removed in
   *     Guava 16.0.
   */
  @Deprecated
  public static HashCode fromBytes(byte[] bytes) {
    return HashCode.fromBytes(bytes);
  }
}

