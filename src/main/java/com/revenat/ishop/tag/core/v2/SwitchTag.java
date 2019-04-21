package com.revenat.ishop.tag.core.v2;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SwitchTag extends SimpleTagSupport {
	private Object value;
	private boolean isProcessed;
	
	@Override
	public void doTag() throws JspException, IOException {
		isProcessed = false;
		evaluateBody();
	}

	private void evaluateBody() throws JspException, IOException {
		if (getJspBody() != null) {
			getJspBody().invoke(null);
		}
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public boolean isProcessed() {
		return isProcessed;
	}
	
	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
}
