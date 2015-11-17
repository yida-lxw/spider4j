package com.yida.spider4j.crawler.utils.hash;

import java.nio.charset.Charset;

/**
 * An abstract hasher, implementing {@link #putBoolean(boolean)}, {@link #putDouble(double)},
 * {@link #putFloat(float)}, {@link #putUnencodedChars(CharSequence)}, and
 * {@link #putString(CharSequence, Charset)} as prescribed by {@link Hasher}.
 *
 * @author Dimitris Andreou
 */
public abstract class AbstractHasher implements Hasher {
  @Override public final Hasher putBoolean(boolean b) {
    return putByte(b ? (byte) 1 : (byte) 0);
  }

  @Override public final Hasher putDouble(double d) {
    return putLong(Double.doubleToRawLongBits(d));
  }

  @Override public final Hasher putFloat(float f) {
    return putInt(Float.floatToRawIntBits(f));
  }

  /**
   * @deprecated Use {@link AbstractHasher#putUnencodedChars} instead.
   */
  @Deprecated
  @Override public Hasher putString(CharSequence charSequence) {
    return putUnencodedChars(charSequence);
  }

  @Override public Hasher putUnencodedChars(CharSequence charSequence) {
    for (int i = 0, len = charSequence.length(); i < len; i++) {
      putChar(charSequence.charAt(i));
    }
    return this;
  }

  @Override public Hasher putString(CharSequence charSequence, Charset charset) {
    return putBytes(charSequence.toString().getBytes(charset));
  }
}

