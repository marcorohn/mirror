package de.marcorohn.mirror.language.ts.types;

import de.marcorohn.mirror.meta.type.GenericMetaType;
import de.marcorohn.mirror.meta.type.MetaType;
import java.util.List;

public class TsMapType extends TsType implements GenericMetaType {

	public static TsMapType build(MetaType leftType, MetaType rightType) {
		final var type = new TsMapType();
		type.setLeftType(leftType);
		type.setRightType(rightType);
		return type;
	}

	private MetaType leftType;

	private MetaType rightType;

	@Override
	public String getTypeName() {
		return "Map<%s, %s>".formatted(this.leftType.getTypeName(), this.rightType.getTypeName());
	}

	@Override
	public boolean isImportNeeded() {
		return this.getGenericTypes().stream().anyMatch(MetaType::isImportNeeded);
	}

	@Override
	public List<MetaType> getGenericTypes() {
		return List.of(this.leftType, this.rightType);
	}

	public void setLeftType(MetaType leftType) {
		this.leftType = leftType;
	}

	public void setRightType(MetaType rightType) {
		this.rightType = rightType;
	}
}
