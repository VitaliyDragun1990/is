package com.revenat.ishop.tag.jsp2;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class UpperCaseTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		StringWriter buffer = new StringWriter();
		getJspBody().invoke(buffer);
		getJspContext().getOut().print(buffer.toString().toUpperCase());
	}
}
