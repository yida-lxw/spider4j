package com.yida.spider4j.crawler.utils.collection.base;


public final class Platform {

	private Platform() {
	}

	/**
	 * Calls {@link Class#isInstance(Object)}.
	 * 
	 * <p>
	 * This method is not supported in GWT yet.
	 */
	static boolean isInstance(Class<?> clazz, Object obj) {
		return clazz.isInstance(obj);
	}

	/** Returns a thread-local 1024-char array. */
	static char[] charBufferFromThreadLocal() {
		return DEST_TL.get();
	}

	/** Calls {@link System#nanoTime()}. */
	static long systemNanoTime() {
		return System.nanoTime();
	}

	/**
	 * A thread-local destination buffer to keep us from creating new buffers.
	 * The starting size is 1024 characters. If we grow past this we don't put
	 * it back in the threadlocal, we just keep going and grow as needed.
	 */
	private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>() {
		@Override
		protected char[] initialValue() {
			return new char[1024];
		}
	};

	static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
		return matcher.precomputedInternal();
	}
}