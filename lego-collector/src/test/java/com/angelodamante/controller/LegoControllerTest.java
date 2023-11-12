package com.angelodamante.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
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

		when(legoRepository.getAllLegos()).thenReturn(legos);

		legoController.allLegos();
		verify(legoView).showAllLegos(legos);
	}

	@Test
	public void testGetKitsOne() {
		List<KitEntity> kits = new ArrayList<KitEntity>();
		kits.add(new KitEntity(0, "6383", ""));

		when(kitRepository.getAllKits()).thenReturn(kits);

		legoController.allKits();
		verify(legoView).showAllKits(kits);
	}

	@Test
	public void testAddKit() {
		KitEntity k = new KitEntity(0, "n", "p");
		when(kitRepository.add("n", "p")).thenReturn(k);

		legoController.addKit("n", "p");
		verify(legoView).onAddedKit(k);
	}

	@Test
	public void testDeleteKit() {
		KitEntity k = new KitEntity(0, "n", "p");
		legoController.removeKit(k);
		verify(legoView).onDeletedKit(k);
		verify(kitRepository).remove(k);
	}

	@Test
	public void testAddLegoWhenAllFieldsAreOk() {
		LegoEntity le = new LegoEntity(0, "p", 1, 2, 1);
		when(legoRepository.add("p", 1, 2, 1)).thenReturn(le);
		legoController.addLego("p", "1", "2", new KitEntity(1, "p", "m"));
		verify(legoView).onAddedLego(le);
	}

	@Test
	public void testAddLegoWhenQuantityIsNotInteger() {
		LegoEntity le = new LegoEntity(0, "p", 1, 2, 1);
		legoController.addLego("p", "1", "aaa", new KitEntity(1, "p", "m"));
		verify(legoView).showError("Quantity Should Be Integer");
		verify(legoView, never()).onAddedLego(le);
	}

	@Test
	public void testAddLegoWhenBudsIsNotInteger() {
		LegoEntity le = new LegoEntity(0, "p", 1, 2, 1);
		legoController.addLego("p", "aaa", "2", new KitEntity(1, "p", "m"));
		verify(legoView).showError("Buds Should Be Integer");
		verify(legoView, never()).onAddedLego(le);
	}

	@Test
	public void testAddLegoWhenKitIsNull() {
		LegoEntity le = new LegoEntity(0, "p", 1, 2, 1);
		legoController.addLego("p", "1", "2", null);
		verify(legoView).showError("No kit Selected");
		verify(legoView, never()).onAddedLego(le);
	}
	
	@Test
	public void testDeleteLego() {
		LegoEntity lego = new LegoEntity(0, "p", 1, 2, 1);
		legoController.removeLego(lego);
		verify(legoView).onDeletedLego(lego);
		verify(legoRepository).remove(lego);
	}
	
	@Test
	public void testlegosOfKitId() {
		LegoEntity lego = new LegoEntity(0, "p", 1, 2, 1);
		KitEntity k = new KitEntity(1, "n", "p");

		when(legoRepository.getLegosByKitId(1)).thenReturn(Arrays.asList(lego));
		legoController.legosOfKitId(k.getId());
		verify(legoView).showAllLegos(Arrays.asList(lego));
	}
	
	@Test
	public void testLegosByBuds() {
		LegoEntity lego = new LegoEntity(0, "p", 1, 1, 1);
		when(legoRepository.getLegosByBuds(1)).thenReturn(Arrays.asList(lego));
		legoController.legosByBuds(lego.getBuds().toString());
		verify(legoView).showAllSearchedLegos(Arrays.asList(lego));
	}
	@Test
	public void testLegosByBudsWhenNotInteger() {
		legoController.legosByBuds("snhfbvfhjsdfaf");
		verify(legoView).showError("Buds Should Be Integer");
	}
	
}
