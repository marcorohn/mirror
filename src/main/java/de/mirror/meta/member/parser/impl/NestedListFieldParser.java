package de.mirror.meta.member.parser.impl;

import de.marcorohn.mirror.dto.annotations.MirrorToField;
import de.marcorohn.mirror.language.ts.types.TsNestedListType;
import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.member.parser.FieldParser;
import de.marcorohn.mirror.meta.type.TypeMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class NestedListFieldParser implements FieldParser {

	@Override
	public MetaField parse(Field field, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
		ensureArguments(types);
		return this.build(types[types.length - 1], field.getName(), typeMapper, optional, types.length - 1);
	}

	@Override
	public MetaField parse(Method method, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
		if (!method.isAnnotationPresent(MirrorToField.class)) {
			throw new IllegalStateException("Annotation %s missing on method %s".formatted(MirrorToField.class.getName(), method));
		}
		ensureArguments(types);
		return this.build(types[types.length - 1], method.getAnnotation(MirrorToField.class).value(), typeMapper, optional, types.length - 1);
	}

	protected void ensureArguments(Class<?>[] types) {
		if (types.length < 2) {
			throw new IllegalArgumentException(
				"Wrong number of type parameters provided to %s! Expected 2 or more, got %d".formatted(this.getClass().getName(), types.length)
			);
		}

		for (int i = 0; i < types.length - 1; i++) {
			if (!types[i].isAssignableFrom(List.class)) {
				throw new IllegalArgumentException("Type parameter %d is not a list!".formatted(i));
			}
		}
	}

	protected MetaField build(Class<?> type, String identifier, TypeMapper typeMapper, boolean optional, int depth) {
		final var mapping = typeMapper.getMappingForSource(type);

		final var result = new MetaField();
		result.setIdentifier(identifier);
		result.setOptional(optional);

		if (mapping.getTargetType().isImportNeeded()) {
			result.getImportingTypes().add(mapping.getTargetType());
		}

		result.setType(TsNestedListType.build(mapping.getTargetType(), depth));

		return result;
	}
}
