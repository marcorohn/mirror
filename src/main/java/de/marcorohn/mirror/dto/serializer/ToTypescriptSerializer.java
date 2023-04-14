package de.marcorohn.mirror.dto.serializer;

import de.marcorohn.mirror.config.SerializationConfig;
import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.type.GenericMetaType;
import de.marcorohn.mirror.meta.type.MetaType;
import de.marcorohn.mirror.util.DistinctByKey;
import de.marcorohn.mirror.util.StringUtil;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * Default serializer to convert a type to a typescript file (actually only the file contents).
 */
public class ToTypescriptSerializer implements TypeSerializer {

	private static final String TEMPLATE =
		"""
        %s
        
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        // NOTICE:
        // This file has been generated automatically by mirror,
        // any changes made here may be overwritten when restarting the executing
        // application the next time.
        // This file will always reflect the state of the corresponding file in the backend.
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        export interface %s %s {
        %s
        }
        """;

	private MetaType targetType;
	private final List<MetaField> metaFields = new ArrayList<>();
	private SerializationConfig config;

	@Override
	public TypeSerializer setTargetType(MetaType targetType) {
		this.targetType = targetType;
		return this;
	}

	@Override
	public TypeSerializer addField(MetaField metaMember) {
		this.metaFields.add(metaMember);
		return this;
	}

	@Override
	public String serialize() {
		final var importsString = new StringBuilder();
		final var extensions = new StringBuilder();
		final var strFields = new StringBuilder();

		final var importedTypes = new HashSet<MetaType>();

		if (this.targetType.getSuperType() != null && !this.targetType.getSuperType().isFallbackType()) {
			extensions.append("extends ").append(this.targetType.getSuperType().getTypeName());
			importedTypes.add(this.targetType.getSuperType());
		}

		importedTypes.addAll(
			this.metaFields.stream()
				.flatMap(f -> {
					if (f.getType() instanceof GenericMetaType gf) {
						return Stream.concat(Stream.of(f.getType()), gf.getGenericTypes().stream());
					}
					return Stream.of(f.getType());
				})
				.filter(DistinctByKey.distinctByKey(MetaType::getTypeName))
				.filter(MetaType::isImportNeeded)
				.filter(t -> t.getLocation() != null)
				.toList()
		);

		final var importList = new ArrayList<>(importedTypes);
		importList.sort(Comparator.naturalOrder());

		for (final var type : importedTypes) {
			importsString.append(
				"import { %s } from '%s';\n".formatted(
						type.getTypeName(),
						type.getLocation().replace("%filename", generateFileName(type.getTypeName()))
					)
			);
		}

		for (final var field : this.metaFields) {
			strFields.append("  %s%s: %s;\n".formatted(field.getIdentifier(), (field.isOptional() ? "?" : ""), field.getType().getTypeName()));
		}

		return TEMPLATE
			.formatted(importsString + "\n", this.targetType.getTypeName(), extensions, strFields)
			.replaceAll("\n\n}\n$", "\n}\n")
			.replaceAll("\n\n\n", "\n\n");
	}

	@Override
	public TypeSerializer setSerializationConfig(SerializationConfig config) {
		this.config = config;
		return this;
	}

	@Override
	public String getFilePath() {
		final var targetPath = FileSystems
			.getDefault()
			.getPath("")
			.toAbsolutePath()
			.resolve(Path.of(config.getBasePath() + getFileName(true)))
			.normalize();
		return targetPath.toString();
	}

	@Override
	public String getFileName(boolean withEnding) {
		return generateFileName(this.targetType.getTypeName()) + (withEnding ? ".ts" : "");
	}

	private String generateFileName(String fromTypeName) {
		return (StringUtil.camelToKebabCase(fromTypeName).replaceAll("-dto$", ".dto"));
	}
}
