package de.mirror.dto.annotations;

import de.marcorohn.mirror.meta.member.parser.FieldParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to define a custom parser and its rules on how to handle a field.
 * This is usually needed when working with generics, as type information gets lost at compile time.
 * <br><br>
 * When working with lists or maps, you probably want to use {@link MirrorList} or {@link MirrorMap} -
 * Otherwise, you can specify your own parser like this:
 *<br><br>
 * {@code @MirrorGeneric(parser = YourParser.class, types = {GenericClass.class, Long.class, String.class})
 * private GenericClass<Long, String> items;}
 * <br>
 * Note, that it is usually required to include all used Type Parameters classes in the "types"-array as well.
 * It is recommended to write them left-to right as in the diamond operators for consistency and better readability.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MirrorGeneric {
	Class<? extends FieldParser> parser();

	Class<?>[] types() default {};
}
