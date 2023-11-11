package com.angelodamante.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.angelodamante.controller.LegoController;
import com.angelodamante.model.entities.LegoEntity;
import com.angelodamante.model.repository.LegoRepository;
import com.angelodamante.view.LegoView;

public class LegoControllerTest {

	private LegoRepository legoRepository;
	private LegoView legoView;
	private LegoController legoController;

	@Before
	public void setUp() throws Exception {
		legoRepository = mock(LegoRepository.class);
		legoView = mock(LegoView.class);
		legoController = new LegoController(legoRepository, legoView);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLegosOne() {
		List<LegoEntity> legos = new ArrayList<LegoEntity>();
		legos.add(new LegoEntity("foo"));

		when(legoRepository.getLegos()).thenReturn(legos);

		legoController.allLegos();
		verify(legoView).showAllLegos(legos);
	}
}
