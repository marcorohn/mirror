package de.mirror.language.ts.types;

import de.marcorohn.mirror.meta.type.GenericMetaType;
import de.marcorohn.mirror.meta.type.MetaType;
import lombok.Setter;

import java.util.List;

public class TsNestedListType extends TsType implements GenericMetaType {

	public static TsNestedListType build(MetaType innerType, int depth) {
		final var type = new TsNestedListType();
		type.setType(innerType);
		type.setDepth(depth);
		return type;
	}

	@Setter
	private MetaType type;

	@Setter
	private int depth;

	@Override
	public String getTypeName() {
		final var brackets = new StringBuilder();
		brackets.append("[]".repeat(depth));
		return "%s%s".formatted(type.getTypeName(), brackets);
	}

	@Override
	public boolean isImportNeeded() {
		return this.getGenericTypes().stream().anyMatch(MetaType::isImportNeeded);
	}

	@Override
	public List<MetaType> getGenericTypes() {
		return List.of(type);
	}
}
