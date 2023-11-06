package de.mirror.meta.type;

import java.util.ArrayList;
import java.util.List;

public interface TypeMapper {
	List<TypeMapping> getStaticTypeMappings();
	List<TypeMapping> getCustomTypeMappings();
	void addTypeMapping(TypeMapping mapping);

	TypeMapping getFallbackMapping();

	default List<TypeMapping> getAllMappings() {
		final var list = new ArrayList<TypeMapping>();
		list.addAll(this.getStaticTypeMappings());
		list.addAll(this.getCustomTypeMappings());
		return list;
	}

	default boolean hasCustomMappingForSource(Class<?> source) {
		return getCustomTypeMappings().stream().anyMatch(e -> e.getSourceType().equals(source));
	}

	default TypeMapping getCustomMappingForSource(Class<?> source) {
		return getCustomTypeMappings().stream().filter(e -> e.getSourceType().equals(source)).findFirst().orElse(null);
	}

	default boolean hasMappingForSource(Class<?> source) {
		return getAllMappings().stream().anyMatch(e -> e.getSourceType().equals(source));
	}

	default TypeMapping getMappingForSource(Class<?> source) {
		return getAllMappings().stream().filter(e -> e.getSourceType().equals(source)).findFirst().orElse(this.getFallbackMapping());
	}
}
