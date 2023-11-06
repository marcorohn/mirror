package de.mirror.dto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to mirror List types with generics.
 * Shorthand for <br>
 * {@code @MirrorGeneric(parser = NestedListFieldParser.class, types = {List.class, T.class})
 * private Map<T> items;}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface MirrorList {
	Class<?>[] types();
}
