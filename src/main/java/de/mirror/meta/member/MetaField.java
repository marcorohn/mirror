package de.mirror.meta.member;

import de.marcorohn.mirror.meta.type.MetaType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MetaField implements MetaMember {

	private String identifier;
	private MetaType type;
	private Set<MetaType> importingTypes = new HashSet<>();

	private boolean optional = false;

	@Override
	public String toString() {
		return "MetaField{" + "identifier='" + identifier + "'" + "typeName='" + type + "'" + '}';
	}
}
