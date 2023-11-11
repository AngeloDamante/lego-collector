package com.angelodamante.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.angelodamante.model.entities.LegoEntity;
import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.repository.KitRepository;
import com.angelodamante.model.repository.LegoRepository;
import com.angelodamante.view.LegoView;

public class LegoControllerTest {

	private LegoRepository legoRepository;
	private KitRepository kitRepository;
	private LegoView legoView;
	private LegoController legoController;

	@Before
	public void setUp() throws Exception {
		legoRepository = mock(LegoRepository.class);
		kitRepository = mock(KitRepository.class);
		legoView = mock(LegoView.class);
		legoController = new LegoController(legoRepository, kitRepository, legoView);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetLegosOne() {
		List<LegoEntity> legos = new ArrayList<LegoEntity>();
		legos.add(new LegoEntity(0, "6383", 8, 3, 1));
		
		when(legoRepository.getLegos()).thenReturn(legos);
		
		legoController.allLegos();
		verify(legoView).showAllLegos(legos);
	}

	@Test
	public void testGetKitsOne() {
		List<KitEntity> kits = new ArrayList<KitEntity>();
		kits.add(new KitEntity(0, "6383", ""));

		when(kitRepository.getKits()).thenReturn(kits);

		legoController.allKits();
		verify(legoView).showAllKits(kits);
	}
}
