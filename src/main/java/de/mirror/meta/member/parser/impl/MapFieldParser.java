package de.mirror.meta.member.parser.impl;

import de.marcorohn.mirror.dto.annotations.MirrorToField;
import de.marcorohn.mirror.language.ts.types.TsMapType;
import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.member.parser.FieldParser;
import de.marcorohn.mirror.meta.type.MetaType;
import de.marcorohn.mirror.meta.type.TypeMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;

public class MapFieldParser implements FieldParser {

	@Override
	public MetaField parse(Field field, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
		ensureArguments(types);
		return this.build(types[1], types[2], field.getName(), typeMapper, optional);
	}

	@Override
	public MetaField parse(Method method, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
		ensureArguments(types);
		if (!method.isAnnotationPresent(MirrorToField.class)) {
			throw new IllegalStateException("Annotation %s missing on method %s".formatted(MirrorToField.class.getName(), method));
		}
		return this.build(types[1], types[2], method.getAnnotation(MirrorToField.class).value(), typeMapper, optional);
	}

	protected MetaField build(Class<?> keyType, Class<?> valueType, String identifier, TypeMapper typeMapper, boolean optional) {
		final var keyMapping = typeMapper.getMappingForSource(keyType);
		final var valueMapping = typeMapper.getMappingForSource(valueType);

		final var imports = new HashSet<MetaType>();

		if (keyMapping.getTargetType().isImportNeeded()) {
			imports.add(keyMapping.getTargetType());
		}

		if (valueMapping.getTargetType().isImportNeeded()) {
			imports.add(valueMapping.getTargetType());
		}

		final var result = new MetaField();
		result.setIdentifier(identifier);
		result.setOptional(optional);
		result.setImportingTypes(imports);
		result.setType(TsMapType.build(keyMapping.getTargetType(), valueMapping.getTargetType()));
		return result;
	}

	protected void ensureArguments(Class<?>[] types) {
		if (types.length != 3) {
			throw new IllegalArgumentException(
				"Wrong number of type parameters provided to %s! Expected 3, got %d".formatted(this.getClass().getName(), types.length)
			);
		}

		if (!types[0].isAssignableFrom(Map.class)) {
			throw new IllegalArgumentException("Type parameter 0 is not a map!");
		}
	}
}
