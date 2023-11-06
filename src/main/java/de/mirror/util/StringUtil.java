package de.mirror.util;

public class StringUtil {

	public static String camelToKebabCase(String camelCase) {
		if (camelCase == null || camelCase.isEmpty()) {
			return camelCase;
		}

		StringBuilder kebabCase = new StringBuilder();
		kebabCase.append(Character.toLowerCase(camelCase.charAt(0)));

		for (int i = 1; i < camelCase.length(); i++) {
			char currentChar = camelCase.charAt(i);
			if (Character.isUpperCase(currentChar)) {
				kebabCase.append('-');
				kebabCase.append(Character.toLowerCase(currentChar));
			} else {
				kebabCase.append(currentChar);
			}
		}

		return kebabCase.toString();
	}

	public static String getterToField(String getterName) {
		var fieldName = getterName.replaceAll("^get", "");
		if (Character.isUpperCase(fieldName.charAt(0))) {
			fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
		}
		return fieldName;
	}
}
