package com.angelodamante.model.repository;

import java.util.List;

import com.angelodamante.model.entities.KitEntity;

// connect code to repository (db connector)
public interface KitRepository {
	public List<KitEntity> getAllKits();

	public KitEntity add(String productCode, String name);

	public void remove(KitEntity kit);

	public void update(KitEntity kit);
}
