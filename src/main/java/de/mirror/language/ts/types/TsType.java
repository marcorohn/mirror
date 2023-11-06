package de.mirror.language.ts.types;

import de.marcorohn.mirror.meta.type.MetaType;
import de.marcorohn.mirror.meta.type.TargetOrigin;

import java.util.Objects;

public abstract class TsType implements MetaType {

	@Override
	public String getLocation() {
		return null;
	}

	@Override
	public TargetOrigin getOrigin() {
		return TargetOrigin.BUILTIN;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getTypeName());
	}

	@Override
	public boolean equals(Object that) {
		if (!(that instanceof MetaType)) {
			return false;
		}
		return Objects.equals(this.getTypeName(), ((MetaType) that).getTypeName());
	}
}
