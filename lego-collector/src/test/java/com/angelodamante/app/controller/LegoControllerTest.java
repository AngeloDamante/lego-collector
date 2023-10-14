package com.angelodamante.app.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LegoControllerTest {

	private LegoController legoController;

	@Before
	public void setUp() throws Exception {
		legoController = new LegoController();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLegosWhenNoLegoAvailable() {
		assertThat(legoController.getLegos()).isEmpty();
	}

}
