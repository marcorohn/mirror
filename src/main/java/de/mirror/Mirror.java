package de.mirror;

import de.marcorohn.mirror.config.ParserConfig;
import de.marcorohn.mirror.config.SerializationConfig;
import de.marcorohn.mirror.dto.annotations.MirrorDto;
import de.marcorohn.mirror.dto.annotations.MirrorExclude;
import de.marcorohn.mirror.dto.annotations.MirrorToField;
import de.marcorohn.mirror.dto.serializer.TypeSerializer;
import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.member.MetaFieldFactory;
import de.marcorohn.mirror.meta.type.MetaType;
import de.marcorohn.mirror.meta.type.TargetOrigin;
import de.marcorohn.mirror.meta.type.TypeMapping;
import de.marcorohn.mirror.util.MirroringFailedException;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Mirror {

	private final SerializationConfig serializationConfig;
	private final ParserConfig parserConfig;

	public Mirror(SerializationConfig serializationConfig, ParserConfig parserConfig) {
		this.serializationConfig = serializationConfig;
		this.parserConfig = parserConfig;
	}

	public void mirror() throws MirroringFailedException {
		final var reflections = new Reflections(
			new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(parserConfig.getPackageName()))
				.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
		);

		final var mirroredDtos = reflections.getTypesAnnotatedWith(MirrorDto.class);

		// Register first so our custom dto types are available when processing them
		mirroredDtos.forEach(clazz -> {
			final var customName = clazz.getAnnotation(MirrorDto.class).name();
			parserConfig
				.getTypeMapper()
				.addTypeMapping(
					new TypeMapping(
						clazz,
						new MetaType() {
							@Override
							public String getTypeName() {
								return customName.isEmpty() ? clazz.getSimpleName() : customName;
							}

							@Override
							public String getLocation() {
								return "./%filename";
							}

							@Override
							public TargetOrigin getOrigin() {
								return TargetOrigin.INCLUDED;
							}

							@Override
							public MetaType getSuperType() {
								final var superclass = clazz.getSuperclass();
								if (superclass != null) {
									return parserConfig.getTypeMapper().getMappingForSource(superclass).getTargetType();
								}
								return null;
							}

							@Override
							public List<MetaType> getInterfaces() {
								final var interfaces = Arrays.stream(clazz.getInterfaces()).toList();
								final var mappings = interfaces.stream().map(parserConfig.getTypeMapper()::getMappingForSource).toList();
								final var targets = mappings.stream().filter(Objects::nonNull).map(TypeMapping::getTargetType).toList();
								return targets;
							}
						}
					)
				);
		});

		/*parserConfig
			.getTypeMapper()
			.getAllMappings()
			.forEach(mapping -> {
				System.out.println(mapping);
			});*/

		// now do the actual mirroring
		final var files = new ArrayList<SourceFile>(mirroredDtos.size());
		for (final var clazz : mirroredDtos) {
			final var mirrorAnnotation = clazz.getAnnotation(MirrorDto.class);
			if (mirrorAnnotation.excludeContent()) {
				continue;
			}
			files.add(mirrorDto(clazz));
		}
		for (final var file : files) {
			this.writeFile(file);
		}
	}

	private SourceFile mirrorDto(Class<?> clazz) throws MirroringFailedException {
		if (!clazz.isAnnotationPresent(MirrorDto.class)) {
			throw new IllegalArgumentException(
				"Annotation %s is not present on class %s!".formatted(MirrorDto.class.getSimpleName(), clazz.getTypeName())
			);
		}

		final var mapping = this.parserConfig.getTypeMapper().getMappingForSource(clazz);

		if (mapping == null) {
			throw new IllegalStateException("No mapping found for %s!".formatted(clazz.getName()));
		}

		final var fieldsToMirror = Arrays.stream(clazz.getDeclaredFields()).filter(f -> !f.isAnnotationPresent(MirrorExclude.class)).toList();

		try {
			final TypeSerializer serializerInstance;
			try {
				serializerInstance =
					this.parserConfig.getSerializer()
						.getDeclaredConstructor()
						.newInstance()
						.setTargetType(mapping.getTargetType())
						.setSerializationConfig(this.serializationConfig);
			} catch (
				InstantiationException
				| IllegalAccessException
				| IllegalArgumentException
				| InvocationTargetException
				| NoSuchMethodException
				| SecurityException e
			) {
				throw new RuntimeException(e);
			}

			for (final var field : fieldsToMirror) {
				MetaField metaField = MetaFieldFactory.build(field, parserConfig);
				serializerInstance.addField(metaField);
			}

			final var methodsToMirrorToField = Arrays
				.stream(clazz.getDeclaredMethods())
				.filter(m -> !m.isAnnotationPresent(MirrorExclude.class) && m.isAnnotationPresent(MirrorToField.class))
				.toList();

			for (final var field : methodsToMirrorToField) {
				MetaField metaField = MetaFieldFactory.build(field, parserConfig);
				serializerInstance.addField(metaField);
			}

			return new SourceFile(serializerInstance.getFilePath(), serializerInstance.serialize());
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			throw new MirroringFailedException(e);
		}
	}

	private void writeFile(SourceFile sourceFile) throws MirroringFailedException {
		try {
			final var path = Paths.get(sourceFile.getLocation());
			if (this.serializationConfig.isCreateIfNonExistent()) {
				Files.createDirectories(path.getParent());
				Files.writeString(path, sourceFile.getContents(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} else {
				Files.writeString(path, sourceFile.getContents());
			}
		} catch (IOException e) {
			throw new MirroringFailedException(e);
		}
	}
}
