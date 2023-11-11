package com.angelodamante.controller;

import java.util.List;

import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.entities.LegoEntity;
import com.angelodamante.model.repository.LegoRepository;
import com.angelodamante.model.repository.KitRepository;
import com.angelodamante.view.LegoView;

public class LegoController {
	private LegoRepository legoRepository;
	private KitRepository kitRepository;
	private LegoView legoView;
	
	public LegoController(LegoRepository legoRepository, KitRepository kitRepository, LegoView legoView) {
		this.legoRepository = legoRepository;
		this.kitRepository = kitRepository;
		this.legoView = legoView;
	}

	public void allLegos() {
		List<LegoEntity> legos = legoRepository.getAllLegos();
		legoView.showAllLegos(legos);
	}
	
	public void allKits() {
		List<KitEntity> kits = kitRepository.getAllKits();
		legoView.showAllKits(kits);
	}

	public void addKit(String productCode, String name) {
		KitEntity kitEntity = kitRepository.add(productCode, name);
		legoView.onAddedKit(kitEntity);
	}

	public void removeKit(KitEntity kit) {
		kitRepository.remove(kit);
		legoView.onDeletedKit(kit);
	}
}
