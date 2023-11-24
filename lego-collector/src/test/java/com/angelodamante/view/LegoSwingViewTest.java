package com.angelodamante.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.angelodamante.controller.LegoController;
import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.entities.LegoEntity;

@RunWith(GUITestRunner.class)
public class LegoSwingViewTest extends AssertJSwingJUnitTestCase {
	private AutoCloseable closeable;
	private FrameFixture window;
	private LegoSwingView legoSwingView;

	@Mock
	private LegoController legoController;

	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			legoSwingView = new LegoSwingView();
			legoSwingView.setController(legoController);
			return legoSwingView;
		});

		window = new FrameFixture(robot(), legoSwingView);
		window.show(); // shows the frame to test
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testInitialState() {
		// Kit
		window.label(JLabelMatcher.withName("lblName")).requireText("name");
		window.label(JLabelMatcher.withName("lblProductCode")).requireText("product code");
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).requireEditable();
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtName")).requireEditable();
		window.textBox(JTextComponentMatcher.withName("txtName")).requireEnabled();
		window.button(JButtonMatcher.withName("btnAddKit")).requireDisabled();
		window.button(JButtonMatcher.withName("btnDeleteKit")).requireDisabled();
		window.list("listKits");

		// Lego
		window.label(JLabelMatcher.withName("lblBudsLego")).requireText("buds");
		window.label(JLabelMatcher.withName("lblQuantityLego")).requireText("quantity");
		window.label(JLabelMatcher.withName("lblProductCodeLego")).requireText("product code");
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).requireEditable();
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).requireEditable();
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).requireEditable();
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).requireEnabled();
		window.button(JButtonMatcher.withName("btnAddLego")).requireDisabled();
		window.button(JButtonMatcher.withName("btnDeleteLego")).requireDisabled();
		window.list("listLegos");

		// Update Kit
		window.textBox(JTextComponentMatcher.withName("txtNewKitProductCode")).requireEditable();
		window.textBox(JTextComponentMatcher.withName("txtNewKitProductCode")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).requireEditable();
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).requireEnabled();
		window.button(JButtonMatcher.withName("btnUpdateKit")).requireDisabled();

		// Search Lego
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).requireEditable();
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).requireEnabled();
		window.button(JButtonMatcher.withName("btnSearchLegos")).requireDisabled();
		window.list("listSearchedLegos");
	}

	@Test
	public void testGetLegos() {
		LegoEntity lego = new LegoEntity(0, "6383", 8, 3, 1);
		GuiActionRunner.execute(() -> legoSwingView.showAllLegos(Arrays.asList(lego)));
		String[] legos = window.list("listLegos").contents();
		assertThat(legos).containsExactly(lego.toString());
	}

	@Test
	public void testGetKits() {
		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.showAllKits(Arrays.asList(kit)));
		String[] kits = window.list("listKits").contents();
		assertThat(kits).containsExactly(kit.toString());
	}

	@Test
	public void testAddKitBtnIsEnabled() {
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtName")).enterText("n");
		window.button(JButtonMatcher.withName("btnAddKit")).requireEnabled();
	}

	@Test
	public void testAddKitBtnIsDisabledWhenThereIsWhiteSpace() {
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).enterText(" ");
		window.textBox(JTextComponentMatcher.withName("txtName")).enterText("n");
		window.button(JButtonMatcher.withName("btnAddKit")).requireDisabled();
	}

	@Test
	public void testAddNewKit() {
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtName")).enterText("n");
		window.button(JButtonMatcher.withName("btnAddKit")).click();
		verify(legoController).addKit("p", "n");
	}

	@Test
	public void testOnAddedKitShouldAddTheKitToTheList() {
		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		String[] listContents = window.list("listKits").contents();
		assertThat(listContents).containsExactly(kit.toString());
	}

	@Test
	public void testWhenKitIsSelectedDeleteKitButtonIsEnabled() {
		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		assertTrue(window.button(JButtonMatcher.withName("btnDeleteKit")).isEnabled());
	}

	@Test
	public void testDeleteKit() {
		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		window.button(JButtonMatcher.withName("btnDeleteKit")).click();
		verify(legoController).removeKit(kit);
	}

	@Test
	public void testOnDeletedKitShouldDeleteFromList() {
		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		GuiActionRunner.execute(() -> legoSwingView.onDeletedKit(kit));

		String[] listContents = window.list("listKits").contents();
		assertThat(listContents).isEmpty();
	}

	@Test
	public void testAddNewLegoBtnIsEnabled() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("2");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("1");
		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		window.button(JButtonMatcher.withName("btnAddLego")).requireEnabled();
	}

	@Test
	public void testAddNewLegoBtnIsDisabledWhenNoProductCode() {
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("2");
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("1");

		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);

		window.button(JButtonMatcher.withName("btnAddLego")).requireDisabled();
	}

	@Test
	public void testAddNewLegoBtnIsDisabledWhenNoBuds() {
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("1");
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("");

		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);

		window.button(JButtonMatcher.withName("btnAddLego")).requireDisabled();
	}

	@Test
	public void testAddNewLegoBtnIsDisabledWhenNoQuantity() {
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("1");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("");
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p");

		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);

		window.button(JButtonMatcher.withName("btnAddLego")).requireDisabled();
	}

	@Test
	public void testAddNewLegoBtnIsDisabledWhenNoKitIsSelected() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("2");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("1");

		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));

		window.button(JButtonMatcher.withName("btnAddLego")).requireDisabled();
	}

	@Test
	public void testAddNewLegoCallsController() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("2");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("1");

		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);

		window.button(JButtonMatcher.withName("btnAddLego")).click();
		verify(legoController).addLego("p", "2", "1", kit);
	}

	@Test
	public void testOnAddedLegoShouldAddTheLegoToTheList() {
		LegoEntity le = new LegoEntity(0, "p", 1, 1, 1);
		GuiActionRunner.execute(() -> legoSwingView.onAddedLego(le));
		String[] listContents = window.list("listLegos").contents();
		assertThat(listContents).containsExactly(le.toString());
	}

	@Test
	public void testShowErrorDisplaysError() {
		String message = "Error";
		GuiActionRunner.execute(() -> legoSwingView.showError(message));
		window.label(JLabelMatcher.withName("lblErrorLog")).requireText(message);
	}

	@Test
	public void testWhenLegoIsSelectedDeleteButtonIsEnabled() {
		LegoEntity lego = new LegoEntity(0, "6383", 8, 3, 1);
		GuiActionRunner.execute(() -> legoSwingView.onAddedLego(lego));
		window.list("listLegos").selectItem(0);
		assertTrue(window.button(JButtonMatcher.withName("btnDeleteLego")).isEnabled());
	}

	@Test
	public void testDeleteLego() {
		LegoEntity lego = new LegoEntity(0, "6383", 8, 3, 1);
		GuiActionRunner.execute(() -> legoSwingView.onAddedLego(lego));
		window.list("listLegos").selectItem(0);
		window.button(JButtonMatcher.withName("btnDeleteLego")).click();
		verify(legoController).removeLego(lego);
	}

	@Test
	public void testOnDeletedLegoShouldDeleteFromList() {
		LegoEntity lego = new LegoEntity(0, "6383", 8, 3, 1);
		GuiActionRunner.execute(() -> legoSwingView.onAddedLego(lego));
		GuiActionRunner.execute(() -> legoSwingView.onDeletedLego(lego));
		String[] listContents = window.list("listLegos").contents();
		assertThat(listContents).isEmpty();
	}

	@Test
	public void testOnKitSelectedShouldShowLegos() {
		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		verify(legoController, atLeastOnce()).legosOfKitId(kit.getId());
	}

	@Test
	public void testSearchButtonIsEnabledWhenThereIsSomethingText() {
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).enterText("3");
		assertTrue(window.button(JButtonMatcher.withName("btnSearchLegos")).isEnabled());
	}

	@Test
	public void testSearchButtonIsDisablesWhenThereIsWhiteSpace() {
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).enterText(" ");
		assertFalse(window.button(JButtonMatcher.withName("btnSearchLegos")).isEnabled());
	}

	@Test
	public void testSearchButton() {
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).enterText("3");
		window.button(JButtonMatcher.withName("btnSearchLegos")).click();
		verify(legoController).legosByBuds("3");
	}

	@Test
	public void testShowAllSearchedLegos() {
		LegoEntity lego = new LegoEntity(0, "6383", 8, 3, 1);
		GuiActionRunner.execute(() -> legoSwingView.showAllSearchedLegos(Arrays.asList(lego)));
		String[] legos = window.list("listSearchedLegos").contents();
		assertThat(legos).containsExactly(lego.toString());
	}

	@Test
	public void testUpdateKitBtnIsDisabledWhenNoProductCode() {
		KitEntity kit = new KitEntity(0, "6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtNewKitProductCode")).setText("");
		window.textBox(JTextComponentMatcher.withName("txtNewKitProductCode")).enterText(" ");
		assertFalse(window.button(JButtonMatcher.withName("btnUpdateKit")).isEnabled());
	}

	@Test
	public void testUpdateKitBtnIsDisabledWhenNoName() {
		KitEntity kit = new KitEntity(0, "6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).setText("");
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).enterText(" ");
		assertFalse(window.button(JButtonMatcher.withName("btnUpdateKit")).isEnabled());
	}

	@Test
	public void testUpdateKitBtnIsDisabledWhenNoKitSelected() {
		KitEntity kit = new KitEntity(0, "6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).setText("p");
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).enterText("n");
		assertFalse(window.button(JButtonMatcher.withName("btnUpdateKit")).isEnabled());
	}

	@Test
	public void testUpdateKitBtnIsEnabled() {
		KitEntity kit = new KitEntity(0, "6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtNewKitProductCode")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).enterText("n");
		assertTrue(window.button(JButtonMatcher.withName("btnUpdateKit")).isEnabled());
	}

	@Test
	public void testUpdateKitBtnCallsController() {
		KitEntity kit = new KitEntity(0, "6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtNewKitProductCode")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).enterText("n");
		window.button(JButtonMatcher.withName("btnUpdateKit")).click();
		verify(legoController).updateKit("6383p", "nn", kit);
	}
}