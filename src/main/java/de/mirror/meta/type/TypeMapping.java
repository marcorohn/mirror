package de.mirror.meta.type;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class TypeMapping {

	private static final Map<Class<? extends MetaType>, MetaType> targetResolver = new HashMap<>();

	private final Class<?> sourceType;

	private final MetaType targetType;

	public TypeMapping(Class<?> sourceType, Class<? extends MetaType> targetType) {
		if (!targetResolver.containsKey(targetType)) {
			MetaType inst;
			try {
				inst = targetType.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
			targetResolver.put(targetType, inst);
			this.targetType = inst;
		} else {
			this.targetType = targetResolver.get(targetType);
		}
		this.sourceType = sourceType;
	}

	public TypeMapping(Class<?> sourceType, MetaType targetTypeInstance) {
		if (!targetResolver.containsKey(targetTypeInstance.getClass())) {
			targetResolver.put(targetTypeInstance.getClass(), targetTypeInstance);
		}
		this.targetType = targetTypeInstance;
		this.sourceType = sourceType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TypeMapping that = (TypeMapping) o;

		if (!Objects.equals(sourceType, that.sourceType)) return false;
		return Objects.equals(targetType, that.targetType);
	}

	@Override
	public int hashCode() {
		int result = sourceType != null ? sourceType.hashCode() : 0;
		result = 31 * result + (targetType != null ? targetType.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "(%s.class => %s)".formatted(this.sourceType.getName(), targetType.getTypeName());
	}
}
