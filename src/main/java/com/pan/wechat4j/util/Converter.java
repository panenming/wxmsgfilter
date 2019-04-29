package com.pan.wechat4j.util;

public interface Converter<S, T> {
	public T convert(S s);

}
