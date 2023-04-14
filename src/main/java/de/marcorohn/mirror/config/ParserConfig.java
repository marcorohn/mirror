package de.marcorohn.mirror.config;

import de.marcorohn.mirror.dto.serializer.TypeSerializer;
import de.marcorohn.mirror.meta.type.TypeMapper;

public class ParserConfig {

	/**
	 * The typemapper to use.
	 * You can use custom mapper here, for example if you wish to include a custom type
	 * in your DTOs, which is not a DTO by itself/generated automatically by mirror.
	 *
	 * Furthermore, if you want to transpile to a non-supported language, you need to
	 * implement you own mapper (and its mappings) as well.
	 */
	private final TypeMapper typeMapper;

	/**
	 * Specify the TypeSerializer to use to create a file from a modeled type.
	 * The TypeSerializer does the most work, as it needs to transform the types into
	 * a string which conforms to the target language.
	 */
	private final Class<? extends TypeSerializer> serializer;

	/**
	 * The name of the package where to search for DTOs.
	 */
	private final String packageName;

	public ParserConfig(TypeMapper typeMapper, Class<? extends TypeSerializer> serializer, String packageName) {
		this.typeMapper = typeMapper;
		this.serializer = serializer;
		this.packageName = packageName;
	}

	public TypeMapper getTypeMapper() {
		return typeMapper;
	}

	public Class<? extends TypeSerializer> getSerializer() {
		return serializer;
	}

	public String getPackageName() {
		return packageName;
	}
}
