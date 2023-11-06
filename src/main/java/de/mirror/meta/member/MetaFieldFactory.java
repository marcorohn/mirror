package de.mirror.meta.member;

import de.marcorohn.mirror.config.ParserConfig;
import de.marcorohn.mirror.dto.annotations.MirrorGeneric;
import de.marcorohn.mirror.dto.annotations.MirrorList;
import de.marcorohn.mirror.dto.annotations.MirrorMap;
import de.marcorohn.mirror.dto.annotations.MirrorProperties;
import de.marcorohn.mirror.meta.member.parser.FieldParser;
import de.marcorohn.mirror.meta.member.parser.impl.DefaultFieldParser;
import de.marcorohn.mirror.meta.member.parser.impl.MapFieldParser;
import de.marcorohn.mirror.meta.member.parser.impl.NestedListFieldParser;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class MetaFieldFactory {

	public static <T extends AccessibleObject & Member> MetaField build(T member, ParserConfig parserConfig)
		throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		boolean optionalField = false;
		if (member.isAnnotationPresent(MirrorProperties.class)) {
			optionalField = member.getAnnotation(MirrorProperties.class).optional();
		}

		FieldParser parser;
		MetaField metaField = null;

		if (member.isAnnotationPresent(MirrorGeneric.class)) {
			final var annotation = member.getAnnotation(MirrorGeneric.class);
			parser = annotation.parser().getDeclaredConstructor().newInstance();
			if (member instanceof Field field) {
				metaField = parser.parse(field, annotation.types(), parserConfig.getTypeMapper(), optionalField);
			} else if (member instanceof Method method) {
				metaField = parser.parse(method, annotation.types(), parserConfig.getTypeMapper(), optionalField);
			}
		} else if (member.isAnnotationPresent(MirrorList.class)) {
			final var annotation = member.getAnnotation(MirrorList.class);
			parser = new NestedListFieldParser();
			if (member instanceof Field field) {
				metaField = parser.parse(field, annotation.types(), parserConfig.getTypeMapper(), optionalField);
			} else if (member instanceof Method method) {
				metaField = parser.parse(method, annotation.types(), parserConfig.getTypeMapper(), optionalField);
			}
		} else if (member.isAnnotationPresent(MirrorMap.class)) {
			final var annotation = member.getAnnotation(MirrorMap.class);
			parser = new MapFieldParser();
			if (member instanceof Field field) {
				metaField = parser.parse(field, annotation.types(), parserConfig.getTypeMapper(), optionalField);
			} else if (member instanceof Method method) {
				metaField = parser.parse(method, annotation.types(), parserConfig.getTypeMapper(), optionalField);
			}
		} else {
			parser = new DefaultFieldParser();
			if (member instanceof Field field) {
				metaField = parser.parse(field, new Class<?>[] {}, parserConfig.getTypeMapper(), optionalField); // TODO this is not optimal
			} else if (member instanceof Method method) {
				metaField = parser.parse(method, new Class<?>[] {}, parserConfig.getTypeMapper(), optionalField); // TODO this is not optimal
			}
		}

		if (metaField == null) {
			throw new IllegalStateException("member appears to be neither field or method!");
		}

		return metaField;
	}
}
