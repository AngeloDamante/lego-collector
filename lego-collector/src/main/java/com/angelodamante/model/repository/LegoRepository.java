package com.angelodamante.model.repository;

import java.util.List;

import com.angelodamante.model.entities.LegoEntity;

// connect code to repository (db connector)
public interface LegoRepository {
	public List<LegoEntity> getAllLegos();

	public LegoEntity add(String productCode, Integer buds, Integer quantity, Integer kitId);

	public void remove(LegoEntity lego);

}
