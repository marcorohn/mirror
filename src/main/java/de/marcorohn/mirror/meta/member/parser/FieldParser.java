package de.marcorohn.mirror.meta.member.parser;

import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.type.TypeMapper;
import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldParser {
	MetaField parse(Field field, Class<?>[] types, TypeMapper typeMapper, boolean optional);
}
