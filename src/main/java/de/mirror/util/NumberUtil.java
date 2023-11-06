package de.mirror.util;

public class NumberUtil {

	public static Double toDouble(Object o) {
		if (o instanceof Integer i) {
			return Double.valueOf(i);
		} else if (o instanceof String s) {
			return Double.parseDouble(s);
		} else if (o instanceof Long l) {
			return Double.valueOf(l);
		} else if (o instanceof Short s) {
			return Double.valueOf(s);
		} else if (o instanceof Byte b) {
			return Double.valueOf(b);
		} else if (o instanceof Float f) {
			return Double.valueOf(f);
		}
		return (Double) o;
	}
}
