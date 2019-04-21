package com.revenat.ishop.tag.core.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class SwitchTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private Object value;
	private List<CaseTag> caseTags;
	private DefaultTag defaultTag;

	@Override
	public int doStartTag() throws JspException {
		caseTags = new ArrayList<>();
		defaultTag = null;
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		processSwitchBody();
		return EVAL_PAGE;
	}

	public void processSwitchBody() throws JspException {
		try {
			SwitchChild matchClause = findMatchClause();
			if (matchClause != null) {
				printContent(matchClause.getContent());
			}
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	private SwitchChild findMatchClause() {
		for (CaseTag tag : caseTags) {
			if (Objects.equals(value, tag.getValue())) {
				return tag;
			}
		}
		return defaultTag;
	}
	
	private void printContent(String content) throws IOException {
		pageContext.getOut().print(content);
	}

	public void setValue(Object value) {
		this.value = value;
	}

	void addCaseTag(CaseTag tag) {
		caseTags.add(tag);
	}

	void setDefaultTag(DefaultTag tag) {
		defaultTag = tag;
	}
}
