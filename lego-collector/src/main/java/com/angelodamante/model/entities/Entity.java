package com.angelodamante.model.entities;

import java.util.Objects;

public abstract class Entity {
	private Integer id;
	private String productCode;

	protected Entity(Integer id, String productCode) {
		super();
		this.id = id;
		this.productCode = productCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, productCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		return Objects.equals(id, other.id) && Objects.equals(productCode, other.productCode);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
