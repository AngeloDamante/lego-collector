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

	public void addLego(String productCode, String buds, String quantity, KitEntity kit) {
		Integer budsParsed = tryIntegerParsing(buds);
		if (budsParsed == null) {
			legoView.showError("Buds Should Be Integer");
			return;
		}
		Integer quantityParsed = tryIntegerParsing(quantity);
		if (quantityParsed == null) {
			legoView.showError("Quantity Should Be Integer");
			return;
		}
		if (kit == null) {
			legoView.showError("No kit Selected");
			return;
		}
		LegoEntity le = legoRepository.add(productCode, budsParsed, quantityParsed, kit.getId());
		legoView.onAddedLego(le);
	}
	
	private Integer tryIntegerParsing(String text) {
		try {
			return Integer.parseInt(text);
		} catch(NumberFormatException e) {
			return null;
		}
	}

	public void removeLego(LegoEntity lego) {
		legoRepository.remove(lego);
		legoView.onDeletedLego(lego);
	}

	public void legosOfKitId(Integer id) {
		List<LegoEntity> legos = legoRepository.getLegosByKitId(id);
		legoView.showAllLegos(legos);
	}

	public void legosByBuds(String buds) {
		Integer budsParsed = tryIntegerParsing(buds);
		if (budsParsed == null) {
			legoView.showError("Buds Should Be Integer");
			return;
		}
		List<LegoEntity> legos = legoRepository.getLegosByBuds(budsParsed);
		legoView.showAllSearchedLegos(legos);
	}

	public void updateKit(String newProductCode, String newName, KitEntity kit) {
		kit.setProductCode(newProductCode);
		kit.setName(newName);
		kitRepository.update(kit);
		allKits();
	}
}