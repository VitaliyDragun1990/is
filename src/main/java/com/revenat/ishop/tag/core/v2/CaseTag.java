package com.revenat.ishop.tag.core.v2;

import java.util.Objects;

public class CaseTag extends AbstractSwitchChildTag {
	private Object value;

	@Override
	protected boolean isMatched(SwitchTag parent) {
		return Objects.equals(value, parent.getValue());
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
