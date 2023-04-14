package de.marcorohn.mirror.meta.member.parser.impl;

import de.marcorohn.mirror.language.ts.types.TsMapType;
import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.member.parser.FieldParser;
import de.marcorohn.mirror.meta.type.MetaType;
import de.marcorohn.mirror.meta.type.TypeMapper;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;

public class MapFieldParser implements FieldParser {

	@Override
	public MetaField parse(Field field, Class<?>[] types, TypeMapper typeMapper, boolean optional) {
		if (types.length != 3) {
			throw new IllegalArgumentException(
				"Wrong number of type parameters provided to %s! Expected 3, got %d".formatted(this.getClass().getName(), types.length)
			);
		}

		if (!types[0].isAssignableFrom(Map.class)) {
			throw new IllegalArgumentException("Type parameter 0 is not a map!");
		}

		final var keyMapping = typeMapper.getMappingForSource(types[1]);
		final var valueMapping = typeMapper.getMappingForSource(types[2]);

		final var imports = new HashSet<MetaType>();

		if (keyMapping.getTargetType().isImportNeeded()) {
			imports.add(keyMapping.getTargetType());
		}

		if (valueMapping.getTargetType().isImportNeeded()) {
			imports.add(valueMapping.getTargetType());
		}

		final var result = new MetaField();
		result.setIdentifier(field.getName());
		result.setOptional(optional);
		result.setImportingTypes(imports);
		result.setType(TsMapType.build(keyMapping.getTargetType(), valueMapping.getTargetType()));
		return result;
	}
}
