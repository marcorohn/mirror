package de.marcorohn.mirror.language.ts.types;

import de.marcorohn.mirror.meta.type.GenericMetaType;
import de.marcorohn.mirror.meta.type.MetaType;
import java.util.List;

public class TsNestedListType extends TsType implements GenericMetaType {

	public static TsNestedListType build(MetaType innerType, int depth) {
		final var type = new TsNestedListType();
		type.setType(innerType);
		type.setDepth(depth);
		return type;
	}

	private MetaType type;

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

	public void setType(MetaType type) {
		this.type = type;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
