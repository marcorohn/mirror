package de.marcorohn.mirror.meta.member.parser.impl;

import de.marcorohn.mirror.language.ts.types.TsNestedListType;
import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.member.parser.FieldParser;
import de.marcorohn.mirror.meta.type.TypeMapper;
import java.lang.reflect.Field;
import java.util.List;

public class NestedListFieldParser implements FieldParser {

	@Override
	public MetaField parse(Field field, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
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
		final var mapping = typeMapper.getMappingForSource(types[types.length - 1]);

		final var result = new MetaField();
		result.setIdentifier(field.getName());
		result.setOptional(optional);

		if (mapping.getTargetType().isImportNeeded()) {
			result.getImportingTypes().add(mapping.getTargetType());
		}

		result.setType(TsNestedListType.build(mapping.getTargetType(), types.length - 1));

		return result;
	}
}
