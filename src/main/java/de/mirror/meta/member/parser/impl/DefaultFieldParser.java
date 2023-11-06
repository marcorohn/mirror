package de.mirror.meta.member.parser.impl;

import de.marcorohn.mirror.dto.annotations.MirrorToField;
import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.member.parser.FieldParser;
import de.marcorohn.mirror.meta.type.TypeMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefaultFieldParser implements FieldParser {

	@Override
	public MetaField parse(Field field, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
		return this.build(types.length > 0 ? types[0] : field.getType(), field.getName(), typeMapper, optional);
	}

	@Override
	public MetaField parse(Method method, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
		if (!method.isAnnotationPresent(MirrorToField.class)) {
			throw new IllegalStateException("Annotation %s missing on method %s".formatted(MirrorToField.class.getName(), method));
		}
		final var name = method.getAnnotation(MirrorToField.class).value();
		return this.build(types.length > 0 ? types[0] : method.getReturnType(), name, typeMapper, optional);
	}

	protected MetaField build(Class<?> type, String identifier, TypeMapper typeMapper, boolean optional) {
		final var mapping = typeMapper.getMappingForSource(type);
		final var result = new MetaField();

		result.setIdentifier(identifier);
		result.setOptional(optional);
		result.setType(mapping.getTargetType());

		if (mapping.getTargetType().isImportNeeded()) {
			result.getImportingTypes().add(mapping.getTargetType());
		}

		return result;
	}
}
