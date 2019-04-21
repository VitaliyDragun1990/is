package com.revenat.ishop.tag.jsp2;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TagWithAttr extends SimpleTagSupport {
	private boolean condition;
	
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		if (condition) {
			out.println("Condition is true");
		} else {
			out.println("Condition is false");
		}
		condition = false;
	}
	
	public void setCondition(boolean condition) {
		this.condition = condition;
	}
}
