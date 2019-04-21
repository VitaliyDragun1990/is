package com.revenat.ishop.tag.core.v2;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

abstract class AbstractSwitchChildTag extends SimpleTagSupport {
	
	@Override
	public void doTag() throws JspException, IOException {
		SwitchTag parent = getSwitchParent();
		if (!parent.isProcessed() && isMatched(parent)) {
			parent.setProcessed(true);
			printBlockContent();
		}
	}

	/**
	 * Prints the body content of this tag.
	 * @throws JspException
	 * @throws IOException
	 */
	private void printBlockContent() throws JspException, IOException {
		getJspBody().invoke(null);
	}
	
	/**
	 * Determines whether this block guard value matches with parent switch value.
	 * @param parent
	 * @return
	 */
	protected abstract boolean isMatched(SwitchTag parent);
	
	protected final SwitchTag getSwitchParent() throws JspException {
		JspTag parent = getParent();
		if (parent instanceof SwitchTag) {
			return (SwitchTag) parent;
		} else {
			throw new JspException("case/default tag should be inside switch tag!"
					+ " Current parent is: " + (parent != null ? parent.getClass() : null));
		}
	}

}
