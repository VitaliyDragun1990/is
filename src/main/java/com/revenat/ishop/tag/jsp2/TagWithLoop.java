package com.revenat.ishop.tag.jsp2;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TagWithLoop extends SimpleTagSupport {
	private int count;
	
	@Override
	public void doTag() throws JspException, IOException {
		while(count-- > 0) {
			getJspBody().invoke(null);
		}
	}
	
	public void setCount(int count) {
		this.count = count;
	}
}
