package com.revenat.ishop.tag.jsp1;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class TagWithBody extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private String type;
	
	public TagWithBody() {
		setDefaults();
	}
	
	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().println("<"+type+">");
			return EVAL_BODY_INCLUDE;
		} catch (IOException e) {
			throw new JspException(e);
		}
	}
	
	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().println("</"+type+">");
			setDefaults();
			return EVAL_PAGE;
		} catch (IOException e) {
			throw new JspException(e);
		}
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
