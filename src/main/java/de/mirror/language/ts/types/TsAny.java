package de.mirror.language.ts.types;

public class TsAny extends TsType {

	@Override
	public String getTypeName() {
		return "any";
	}

	@Override
	public boolean isFallbackType() {
		return true;
	}
}
