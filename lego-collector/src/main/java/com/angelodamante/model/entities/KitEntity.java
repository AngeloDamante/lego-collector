package com.angelodamante.model.entities;

import java.util.Objects;

public class KitEntity extends Entity {
	private String name;

	public KitEntity(Integer id, String productCode, String name) {
		super(id, productCode);
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
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		KitEntity other = (KitEntity) obj;
		return Objects.equals(name, other.name);
	}

}
