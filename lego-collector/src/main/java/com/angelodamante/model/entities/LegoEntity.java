package com.angelodamante.model.entities;

import java.util.Objects;

public class LegoEntity extends Entity {
	private Integer buds;
	private Integer quantity;
	private Integer kitId;

	public LegoEntity(Integer id, String productCode, Integer buds, Integer quantity, Integer kitId) {
		super(id, productCode);
		this.buds = buds;
		this.quantity = quantity;
		this.kitId = kitId;
	}

	public Integer getBuds() {
		return buds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(buds, kitId, quantity);
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
		LegoEntity other = (LegoEntity) obj;
		return Objects.equals(buds, other.buds) && Objects.equals(kitId, other.kitId)
				&& Objects.equals(quantity, other.quantity);
	}

	public void setBuds(Integer buds) {
		this.buds = buds;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}
	
	@Override
	public String toString() {
		return "lego " + this.getId() + ": " + this.getProductCode() + " [" + buds + " buds]";
	}

}
