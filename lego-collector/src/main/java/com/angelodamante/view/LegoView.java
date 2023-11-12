package com.angelodamante.view;

import java.util.List;

import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.entities.LegoEntity;

public interface LegoView {
	public void showAllLegos(List<LegoEntity> legos);

	public void showAllKits(List<KitEntity> kits);

	public void onAddedKit(KitEntity kitEntity);

	public void onDeletedKit(KitEntity kit);

	public void onAddedLego(LegoEntity legoEntity);

	public void showError(String string);

	public void onDeletedLego(LegoEntity lego);
}
