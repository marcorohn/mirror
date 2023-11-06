package de.mirror.language.ts;

import de.marcorohn.mirror.language.ts.types.TsAny;
import de.marcorohn.mirror.language.ts.types.TsBoolean;
import de.marcorohn.mirror.language.ts.types.TsNumber;
import de.marcorohn.mirror.language.ts.types.TsString;
import de.marcorohn.mirror.meta.type.TypeMapper;
import de.marcorohn.mirror.meta.type.TypeMapping;

import java.util.LinkedHashSet;
import java.util.List;

public class TsTypeMapper implements TypeMapper {

	private final TypeMapping fallbackMapping = new TypeMapping(Object.class, TsAny.class);

	private final LinkedHashSet<TypeMapping> dynamicMappings = new LinkedHashSet<>();

	private final LinkedHashSet<TypeMapping> staticMappings = new LinkedHashSet<>();

	public TsTypeMapper() {
		this.staticMappings.addAll(
				List.of(
					new TypeMapping(Byte.class, TsNumber.class),
					new TypeMapping(Short.class, TsNumber.class),
					new TypeMapping(Integer.class, TsNumber.class),
					new TypeMapping(Long.class, TsNumber.class),
					new TypeMapping(Float.class, TsNumber.class),
					new TypeMapping(Double.class, TsNumber.class),
					new TypeMapping(byte.class, TsNumber.class),
					new TypeMapping(short.class, TsNumber.class),
					new TypeMapping(int.class, TsNumber.class),
					new TypeMapping(long.class, TsNumber.class),
					new TypeMapping(float.class, TsNumber.class),
					new TypeMapping(double.class, TsNumber.class),
					new TypeMapping(boolean.class, TsBoolean.class),
					new TypeMapping(Boolean.class, TsBoolean.class),
					new TypeMapping(String.class, TsString.class),
					new TypeMapping(Character.class, TsString.class),
					new TypeMapping(char.class, TsString.class),
					new TypeMapping(Character[].class, TsString.class),
					fallbackMapping
				)
			);
	}

	@Override
	public TypeMapping getFallbackMapping() {
		return fallbackMapping;
	}

	@Override
	public List<TypeMapping> getStaticTypeMappings() {
		return this.staticMappings.stream().toList();
	}

	@Override
	public List<TypeMapping> getCustomTypeMappings() {
		return this.dynamicMappings.stream().toList();
	}

	@Override
	public void addTypeMapping(TypeMapping mapping) {
		if (this.hasCustomMappingForSource(mapping.getSourceType())) {
			this.dynamicMappings.remove(this.getCustomMappingForSource(mapping.getSourceType()));
		}
		this.dynamicMappings.add(mapping);
	}
}
