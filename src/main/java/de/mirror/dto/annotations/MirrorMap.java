package de.mirror.dto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to mirror map types with generics.
 * Shorthand for <br>
 * {@code @MirrorGeneric(parser = MapTypeParser.class, types = {Map.class, K.class, V.class})
 * private Map<K, V> items;}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MirrorMap {
	Class<?>[] types();
}
