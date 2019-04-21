package com.revenat.ishop.tag.jsp2;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SimpleTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut()
				.println("Current date: " + DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()));
	}
}
