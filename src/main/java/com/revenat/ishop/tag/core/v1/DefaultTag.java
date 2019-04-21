package com.revenat.ishop.tag.core.v1;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.JspTag;

public class DefaultTag extends BodyTagSupport implements SwitchChild {
	private static final long serialVersionUID = 1L;
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
			((SwitchTag) parent).setDefaultTag(this);
		} else {
			throw new JspException("default tag should be inside switch tag");
		}
	}
	
	@Override
	public String getContent() {
		return content;
	}
}
