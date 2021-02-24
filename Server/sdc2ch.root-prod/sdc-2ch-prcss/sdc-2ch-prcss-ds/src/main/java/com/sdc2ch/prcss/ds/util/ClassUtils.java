package com.sdc2ch.prcss.ds.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtils {
	public static ParameterizedType getParameterizedType(Class<?> target) {
		Type[] types = getGenericType(target);
		if (types.length > 0 && types[0] instanceof ParameterizedType) {
			return (ParameterizedType) types[0];
		}
		return null;
	}

	public static Type[] getParameterizedTypes(Class<?> target) {
		Type[] types = getGenericType(target);
		if (types.length > 0 && types[0] instanceof ParameterizedType) {
			return ((ParameterizedType) types[0]).getActualTypeArguments();
		}
		return null;
	}

	public static Type[] getGenericType(Class<?> target) {
		if (target == null)
			return new Type[0];
		Type[] types = target.getGenericInterfaces();
		if (types.length > 0) {
			return types;
		}
		Type type = target.getGenericSuperclass();
		if (type != null) {
			if (type instanceof ParameterizedType) {
				return new Type[] { type };
			}
		}
		return new Type[0];
	}

}
