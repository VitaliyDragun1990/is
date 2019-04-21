package com.revenat.ishop.tag.jsp1;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class UpperCaseTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	@Override
	public int doEndTag() throws JspException {
		BodyContent tagBody = getBodyContent();
		try {
			pageContext.getOut().print(tagBody.getString().toUpperCase());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}
}
