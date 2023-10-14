package com.angelodamante.app.repository;

import java.util.List;

import com.angelodamante.app.model.LegoEntity;

// connect code to repository (db connector)
public interface LegoRepository {
	public List<LegoEntity> getLegos();
}
