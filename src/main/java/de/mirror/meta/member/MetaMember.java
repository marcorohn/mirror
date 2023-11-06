package de.mirror.meta.member;

import de.marcorohn.mirror.meta.type.MetaType;

import java.util.Set;

public interface MetaMember {
	String getIdentifier();
	void setIdentifier(String identifier);

	MetaType getType();
	void setType(MetaType type);

	Set<MetaType> getImportingTypes();
	void setImportingTypes(Set<MetaType> types);
}
