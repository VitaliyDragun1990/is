package com.revenat.ishop.tag.jsp2;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TagWithBody extends SimpleTagSupport {
	private String type;
	
	public TagWithBody() {
		setDefaults();
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.print("<" + type + ">");
		getJspBody().invoke(null);
		out.println("</" + type + ">");
	}
	
	public void setType(String type) {
		if (type != null) {
			if (Arrays.asList("h1", "h2", "h3", "h4", "h5", "h6").contains(type.toLowerCase())) {
				this.type = type;
			}
		}
	}

	private void setDefaults() {
		type = "h6";
	}
}
