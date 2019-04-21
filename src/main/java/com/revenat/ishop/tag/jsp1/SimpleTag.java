package com.revenat.ishop.tag.jsp1;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class SimpleTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.println("Current date: " + DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()));
			return SKIP_BODY;
		} catch (IOException e) {
			throw new JspException(e);
		}
	}
}
