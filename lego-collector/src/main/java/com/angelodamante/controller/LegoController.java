package com.angelodamante.controller;

import java.util.List;

import com.angelodamante.model.entities.LegoEntity;
import com.angelodamante.model.repository.LegoRepository;
import com.angelodamante.view.LegoView;

public class LegoController {
	private LegoRepository legoRepository;
	private LegoView legoView;
	
	public LegoController(LegoRepository legoRepository, LegoView legoView) {
		this.legoRepository = legoRepository;
		this.legoView = legoView;
	}

	public void allLegos() {
		List<LegoEntity> legos = legoRepository.getLegos();
		legoView.showAllLegos(legos);
	}
}
