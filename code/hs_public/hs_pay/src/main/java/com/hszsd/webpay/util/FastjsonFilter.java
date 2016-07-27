package com.hszsd.webpay.util;

import com.alibaba.fastjson.serializer.PropertyFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * 主要用于过滤不需要序列化的属性，或者包含需要序列化的属性
 * @author bin
 *
 */
public class FastjsonFilter implements PropertyFilter {

	private final Set<String> includes = new HashSet<String>();
	private final Set<String> excludes = new HashSet<String>();

	public Set<String> getIncludes() {
		return includes;
	}

	public Set<String> getExcludes() {
		return excludes;
	}

	@Override
	public boolean apply(Object source, String name, Object value) {
		if (excludes.contains(name)) {
			return false;
		}
		if (excludes.contains(source.getClass().getSimpleName() + "." + name)) {
			return false;
		}
		if (includes.size() == 0 || includes.contains(name)) {
			return true;
		}
		if (includes.contains(source.getClass().getSimpleName() + "." + name)) {
			return true;
		}
		return false;
	}

}
