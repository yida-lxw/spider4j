package com.yida.spider4j.crawler.utils.hash;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * {@link HashFunction} adapter for {@link MessageDigest} instances.
 *
 * @author Kevin Bourrillion
 * @author Dimitris Andreou
 */
@SuppressWarnings("serial")
public final class MessageDigestHashFunction extends AbstractStreamingHashFunction
    implements Serializable {
  private final MessageDigest prototype;
  private final int bytes;
  private final boolean supportsClone;
  private final String toString;

  MessageDigestHashFunction(String algorithmName, String toString) {
    this.prototype = getMessageDigest(algorithmName);
    this.bytes = prototype.getDigestLength();
    this.toString = Preconditions.checkNotNull(toString);
    this.supportsClone = supportsClone();
  }

  MessageDigestHashFunction(String algorithmName, int bytes, String toString) {
    this.toString = Preconditions.checkNotNull(toString);
    this.prototype = getMessageDigest(algorithmName);
    int maxLength = prototype.getDigestLength();
    Preconditions.checkArgument(bytes >= 4 && bytes <= maxLength,
        "bytes (%s) must be >= 4 and < %s", bytes, maxLength);
    this.bytes = bytes;
    this.supportsClone = supportsClone();
  }

  private boolean supportsClone() {
    try {
      prototype.clone();
      return true;
    } catch (CloneNotSupportedException e) {
      return false;
    }
  }

  @Override public int bits() {
    return bytes * Byte.SIZE;
  }

  @Override public String toString() {
    return toString;
  }

  private static MessageDigest getMessageDigest(String algorithmName) {
    try {
      return MessageDigest.getInstance(algorithmName);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  @Override public Hasher newHasher() {
    if (supportsClone) {
      try {
        return new MessageDigestHasher((MessageDigest) prototype.clone(), bytes);
      } catch (CloneNotSupportedException e) {
        // falls through
      }
    }
    return new MessageDigestHasher(getMessageDigest(prototype.getAlgorithm()), bytes);
  }

  private static final class SerializedForm implements Serializable {
    private final String algorithmName;
    private final int bytes;
    private final String toString;

    private SerializedForm(String algorithmName, int bytes, String toString) {
      this.algorithmName = algorithmName;
      this.bytes = bytes;
      this.toString = toString;
    }

    private Object readResolve() {
      return new MessageDigestHashFunction(algorithmName, bytes, toString);
    }

    private static final long serialVersionUID = 0;
  }

  Object writeReplace() {
    return new SerializedForm(prototype.getAlgorithm(), bytes, toString);
  }

  /**
   * Hasher that updates a message digest.
   */
  private static final class MessageDigestHasher extends AbstractByteHasher {

    private final MessageDigest digest;
    private final int bytes;
    private boolean done;

    private MessageDigestHasher(MessageDigest digest, int bytes) {
      this.digest = digest;
      this.bytes = bytes;
    }

    @Override
    protected void update(byte b) {
      checkNotDone();
      digest.update(b);
    }

    @Override
    protected void update(byte[] b) {
      checkNotDone();
      digest.update(b);
    }

    @Override
    protected void update(byte[] b, int off, int len) {
      checkNotDone();
      digest.update(b, off, len);
    }

    private void checkNotDone() {
			Preconditions.checkState(!done, "Cannot use Hasher after calling #hash() on it");
    }

    @Override
    public HashCode hash() {
      done = true;
      return (bytes == digest.getDigestLength())
          ? HashCode.fromBytesNoCopy(digest.digest())
          : HashCode.fromBytesNoCopy(Arrays.copyOf(digest.digest(), bytes));
    }
  }
}

