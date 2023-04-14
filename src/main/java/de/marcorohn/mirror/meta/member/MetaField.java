package de.marcorohn.mirror.meta.member;

import de.marcorohn.mirror.meta.type.MetaType;
import java.util.HashSet;
import java.util.Set;

public class MetaField implements MetaMember {

	public MetaField() {}

	private String identifier;
	private MetaType type;
	private Set<MetaType> importingTypes = new HashSet<>();

	private boolean optional = false;

	@Override
	public String toString() {
		return "MetaField{" + "identifier='" + identifier + "'" + "typeName='" + type + "'" + '}';
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public MetaType getType() {
		return type;
	}

	@Override
	public void setType(MetaType type) {
		this.type = type;
	}

	@Override
	public Set<MetaType> getImportingTypes() {
		return importingTypes;
	}

	@Override
	public void setImportingTypes(Set<MetaType> importingTypes) {
		this.importingTypes = importingTypes;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}
}
