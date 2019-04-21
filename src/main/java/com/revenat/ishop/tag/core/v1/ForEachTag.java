package com.revenat.ishop.tag.core.v1;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ForEachTag extends SimpleTagSupport {
	private String var;
	private Object items;

	@Override
	public void doTag() throws JspException, IOException {
		Iterator<?> it = iterator();
		while (it.hasNext()) {
			setLoopVar(it.next());
			printLoopBody();
		}
	}
	
	private void setLoopVar(Object value) {
		getJspContext().setAttribute(var, value);
	}
	
	private void printLoopBody() throws JspException, IOException {
		getJspBody().invoke(null);
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	public void setItems(Object items) {
		this.items = items;
	}

	private Iterator<?> iterator() throws JspException {
		if (items == null) {
			return Collections.emptyIterator();
		}
		
		return buildIteratorFrom(items);
	}

	private Iterator<?> buildIteratorFrom(Object items) throws JspException {
		Class<? extends Object> clazz = items.getClass();
		
		if (clazz.isArray()) {
			return buildIteratorFromArray(items);
		} else if (Iterable.class.isAssignableFrom(clazz)) {
			Iterable<?> iter = (Iterable<?>) items;
			return iter.iterator();
		} else if (Enumeration.class.isAssignableFrom(clazz)) {
			Enumeration<?> en = (Enumeration<?>) items;
			return Collections.list(en).iterator();
		} else if (Map.class.isAssignableFrom(clazz)) {
			Map<?,?> map = (Map<?,?>) items;
			return map.entrySet().iterator();
		} else if (Iterator.class.isAssignableFrom(clazz)) {
			return (Iterator<?>) items;
		} else if (String.class.isAssignableFrom(clazz)) {
			return Arrays.asList(items.toString().split(",")).iterator();
		}
		
		throw new JspException("Can not iterate over items: " + items.getClass());
	}

	private Iterator<?> buildIteratorFromArray(final Object array) {
		return new Iterator<Object>() {
			private int currentIndex = 0;
			private int length = Array.getLength(array);
			
			@Override
			public boolean hasNext() {
				return currentIndex < length;
			}

			@Override
			public Object next() {
				if (hasNext()) {
					return Array.get(array, currentIndex++);					
				}
				throw new NoSuchElementException();
			}
		};
	}
}
