package de.marcorohn.mirror.meta.member.parser.impl;

import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.member.parser.FieldParser;
import de.marcorohn.mirror.meta.type.TypeMapper;
import java.lang.reflect.Field;

public class DefaultFieldParser implements FieldParser {

	@Override
	public MetaField parse(Field field, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
		final var mapping = typeMapper.getMappingForSource(types.length > 0 ? types[0] : field.getType());
		final var result = new MetaField();

		result.setIdentifier(field.getName());
		result.setOptional(optional);
		result.setType(mapping.getTargetType());

		if (mapping.getTargetType().isImportNeeded()) {
			result.getImportingTypes().add(mapping.getTargetType());
		}

		return result;
	}
}
