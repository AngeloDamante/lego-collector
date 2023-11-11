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
		List<LegoEntity> legos = legoRepository.getLegos();
		legoView.showAllLegos(legos);
	}
	
	public void allKits() {
		List<KitEntity> kits = kitRepository.getKits();
		legoView.showAllKits(kits);
	}
}
