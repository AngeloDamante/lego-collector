package com.angelodamante.app.view;

import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.runner.RunWith;

import com.angelodamante.app.model.LegoEntity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;

@RunWith(GUITestRunner.class)
public class LegoSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private LegoSwingView legoSwingView;

	@Override
	protected void onSetUp() throws Exception {
		GuiActionRunner.execute(() -> {
			legoSwingView = new LegoSwingView();
			return legoSwingView;
		});

		window = new FrameFixture(robot(), legoSwingView);
		window.show(); // shows the frame to test
	}

	@Test
	public void testInitialState() {
		window.label(JLabelMatcher.withText("name"));
		window.textBox("txtName").requireEnabled();
	}
	
	@Test
	public void testGetLegos() {
		LegoEntity lego = new LegoEntity("foo");
		GuiActionRunner.execute(() -> legoSwingView.showAllLegos(Arrays.asList(lego)));
		String[] legos = window.list("listLegos").contents();
		assertThat(legos).containsExactly(lego.toString());
	}

}