package com.yida.spider4j.crawler.utils.collection;

import java.util.HashMap;
import java.util.Map;

import com.yida.spider4j.crawler.utils.collection.MapConstraints.ConstrainedMap;
import com.yida.spider4j.crawler.utils.collection.primitives.Primitives;

/**
 * A mutable class-to-instance map backed by an arbitrary user-provided map. See
 * also {@link ImmutableClassToInstanceMap}.
 * 
 * @author Kevin Bourrillion
 * @since 2 (imported from Google Collections Library)
 */
@SuppressWarnings("unused")
public final class MutableClassToInstanceMap<B> extends
		ConstrainedMap<Class<? extends B>, B> implements ClassToInstanceMap<B> {

	/**
	 * Returns a new {@code MutableClassToInstanceMap} instance backed by a
	 * {@link HashMap} using the default initial capacity and load factor.
	 */
	public static <B> MutableClassToInstanceMap<B> create() {
		return new MutableClassToInstanceMap<B>(
				new HashMap<Class<? extends B>, B>());
	}

	/**
	 * Returns a new {@code MutableClassToInstanceMap} instance backed by a
	 * given empty {@code backingMap}. The caller surrenders control of the
	 * backing map, and thus should not allow any direct references to it to
	 * remain accessible.
	 */
	public static <B> MutableClassToInstanceMap<B> create(
			Map<Class<? extends B>, B> backingMap) {
		return new MutableClassToInstanceMap<B>(backingMap);
	}

	private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
		super(delegate, VALUE_CAN_BE_CAST_TO_KEY);
	}

	private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint<Class<?>, Object>() {
		public void checkKeyValue(Class<?> key, Object value) {
			cast(key, value);
		}
	};

	public <T extends B> T putInstance(Class<T> type, T value) {
		return cast(type, put(type, value));
	}

	public <T extends B> T getInstance(Class<T> type) {
		return cast(type, get(type));
	}

	private static <B, T extends B> T cast(Class<T> type, B value) {
		return Primitives.wrap(type).cast(value);
	}

	private static final long serialVersionUID = 0;
}
