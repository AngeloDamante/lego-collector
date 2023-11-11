package com.angelodamante.model.entities;

import java.util.Objects;

public class LegoEntity {
	private String name;

	public LegoEntity(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LegoEntity other = (LegoEntity) obj;
		return Objects.equals(name, other.name);
	}
	
}
