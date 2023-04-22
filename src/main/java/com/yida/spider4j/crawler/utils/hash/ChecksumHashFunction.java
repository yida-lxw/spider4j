package com.yida.spider4j.crawler.utils.hash;

import java.io.Serializable;
import java.util.zip.Checksum;

import com.yida.spider4j.crawler.utils.collection.base.Preconditions;
import com.yida.spider4j.crawler.utils.collection.base.Supplier;

public final class ChecksumHashFunction extends AbstractStreamingHashFunction implements Serializable {
	private final Supplier<? extends Checksum> checksumSupplier;
	  private final int bits;
	  private final String toString;

	  ChecksumHashFunction(Supplier<? extends Checksum> checksumSupplier, int bits, String toString) {
	    this.checksumSupplier = Preconditions.checkNotNull(checksumSupplier);
		Preconditions.checkArgument(bits == 32 || bits == 64, "bits (%s) must be either 32 or 64", bits);
	    this.bits = bits;
	    this.toString = Preconditions.checkNotNull(toString);
	  }

	  @Override
	  public int bits() {
	    return bits;
	  }

	  @Override
	  public Hasher newHasher() {
	    return new ChecksumHasher(checksumSupplier.get());
	  }

	  @Override
	  public String toString() {
	    return toString;
	  }

	  /**
	   * Hasher that updates a checksum.
	   */
	  private final class ChecksumHasher extends AbstractByteHasher {

	    private final Checksum checksum;

	    private ChecksumHasher(Checksum checksum) {
	      this.checksum = Preconditions.checkNotNull(checksum);
	    }

	    @Override
	    protected void update(byte b) {
	      checksum.update(b);
	    }

	    @Override
	    protected void update(byte[] bytes, int off, int len) {
	      checksum.update(bytes, off, len);
	    }

	    @Override
	    public HashCode hash() {
	      long value = checksum.getValue();
	      if (bits == 32) {
	        /*
	         * The long returned from a 32-bit Checksum will have all 0s for its second word, so the
	         * cast won't lose any information and is necessary to return a HashCode of the correct
	         * size.
	         */
	        return HashCode.fromInt((int) value);
	      } else {
	        return HashCode.fromLong(value);
	      }
	    }
	  }

	  private static final long serialVersionUID = 0L;
}
