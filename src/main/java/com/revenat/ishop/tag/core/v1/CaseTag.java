package com.revenat.ishop.tag.core.v1;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.JspTag;

public class CaseTag extends BodyTagSupport implements SwitchChild {
	private static final long serialVersionUID = 1L;
	private Object value;
	private String content;
	
	@Override
	public int doStartTag() throws JspException {
		content = null;
		return EVAL_BODY_BUFFERED;
	}
	
	@Override
	public int doEndTag() throws JspException {
		content = getBodyContent().getString();
		JspTag parent = getParent();
		associateWithParent(parent);
		return EVAL_PAGE;
	}

	private void associateWithParent(JspTag parent) throws JspException {
		if (parent != null && parent.getClass().equals(SwitchTag.class)) {
			((SwitchTag) parent).addCaseTag(this);
		} else {
			throw new JspException("case tag should be inside switch tag");
		}
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	Object getValue() {
		return value;
	}
	
	@Override
	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return "Case: value=" + value;
	}
}
