package com.angelodamante.model.repository;

import java.util.List;

import com.angelodamante.model.entities.LegoEntity;

// connect code to repository (db connector)
public interface KitRepository {
	public List<LegoEntity> getKits();
}
