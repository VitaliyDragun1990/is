package com.revenat.ishop.domain.entity;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity<T> implements Serializable {
	private static final long serialVersionUID = 3911762542288653980L;

	private T id;

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity<?> other = (AbstractEntity<?>) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return String.format("%s [id=%s]", getClass().getSimpleName(), id);
	}
}
