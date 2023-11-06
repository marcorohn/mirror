package de.mirror.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class DistinctByKey {

	public static <T, K> Predicate<T> distinctByKey(Function<? super T, K> keyExtractor) {
		final Set<K> seenKeys = ConcurrentHashMap.newKeySet();
		return t -> seenKeys.add(keyExtractor.apply(t));
	}
}
