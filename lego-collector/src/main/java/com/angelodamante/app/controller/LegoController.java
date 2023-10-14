package com.angelodamante.app.controller;

import java.util.List;

import com.angelodamante.app.model.LegoEntity;
import com.angelodamante.app.repository.LegoRepository;
import com.angelodamante.app.view.LegoView;

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
