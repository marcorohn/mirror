package de.marcorohn.mirror.meta.type;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public interface MetaType extends Comparable<MetaType> {
	String getTypeName();
	String getLocation();
	TargetOrigin getOrigin();

	default MetaType getSuperType() {
		return null;
	}

	default List<MetaType> getInterfaces() { // TODO support interfaces in java DTOs to translate to ts
		return List.of();
	}

	default boolean isFallbackType() {
		return false;
	}

	default boolean isImportNeeded() {
		return !this.getOrigin().equals(TargetOrigin.BUILTIN);
	}

	@Override
	default int compareTo(MetaType other) {
		return Objects.compare(
			this.getTypeName() + " " + this.getLocation(),
			other.getTypeName() + " " + other.getLocation(),
			Comparator.naturalOrder()
		);
	}
}
