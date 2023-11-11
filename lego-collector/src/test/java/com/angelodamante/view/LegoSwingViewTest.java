package com.angelodamante.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import javax.swing.DefaultListModel;

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
import org.assertj.swing.annotation.GUITest;
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

	@GUITest
	@Test
	public void testInitialState() {
		window.label(JLabelMatcher.withText("name"));
		window.textBox("txtName").requireEnabled();
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
	public void testAddBtnIsDisabled() {
		window.button(JButtonMatcher.withName("btnAddKit")).requireDisabled();
	}

	@Test
	public void testAddBtnIsEnabled() {
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtName")).enterText("n");
		window.button(JButtonMatcher.withName("btnAddKit")).requireEnabled();
	}

	@Test
	public void testAddNewKit() {
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtName")).enterText("n");
		window.button(JButtonMatcher.withName("btnAddKit")).click();
		verify(legoController).addKit("p", "n");

	}

	@Test
	public void testonAddedKitShouldAddTheKitToTheList() {
		KitEntity kit = new KitEntity(0, "6383", "");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		String[] listContents = window.list("listKits").contents();
		assertThat(listContents).containsExactly(kit.toString());
	}

}