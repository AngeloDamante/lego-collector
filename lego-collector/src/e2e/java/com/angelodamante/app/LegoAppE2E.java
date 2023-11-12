package com.angelodamante.app;

import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.awt.Window;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import static org.assertj.core.api.Assertions.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.testcontainers.containers.MongoDBContainer;

import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.entities.LegoEntity;
import com.mongodb.MongoClient;

@RunWith(GUITestRunner.class)
public class LegoAppE2E extends AssertJSwingJUnitTestCase {
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private static final String DB_NAME = "test-db";

	private static final String KIT_COLLECTION_NAME = "test-kits";
	private static final String LEGO_COLLECTION_NAME = "test-legos";

	private MongoClient mongoClient;

	private FrameFixture window;

	private static final Integer KIT_FIXTURE_1_ID = 1;
	private static final String KIT_FIXTURE_1_PRODUCTCODE = "651352";
	private static final String KIT_FIXTURE_1_NAME = "star wars";

	private static final Integer KIT_FIXTURE_2_ID = 2;
	private static final String KIT_FIXTURE_2_PRODUCTCODE = "052616";
	private static final String KIT_FIXTURE_2_NAME = "harry potter";

	private static final Integer LEGO_FIXTURE_1_OF_KIT_1_ID = 1;
	private static final String LEGO_FIXTURE_1_OF_KIT_1_PRODUCTCODE = "51613";
	private static final Integer LEGO_FIXTURE_1_OF_KIT_1_BUDS = 8;
	private static final Integer LEGO_FIXTURE_1_OF_KIT_1_QUANTITY = 2;
	private static final Integer LEGO_FIXTURE_1_OF_KIT_1_KIT_ID = KIT_FIXTURE_1_ID;

	@Override
	protected void onSetUp() {
		String containerIpAddress = mongo.getContainerIpAddress();
		Integer mappedPort = mongo.getFirstMappedPort();
		mongoClient = new MongoClient(containerIpAddress, mappedPort);
		// always start with an empty database
		mongoClient.getDatabase(DB_NAME).drop();
		// add some students to the database
		addTestKitToDatabase(KIT_FIXTURE_1_ID, KIT_FIXTURE_1_PRODUCTCODE, KIT_FIXTURE_1_NAME);
		addTestKitToDatabase(KIT_FIXTURE_2_ID, KIT_FIXTURE_2_PRODUCTCODE, KIT_FIXTURE_2_NAME);
		addTestLegoToDatabase(LEGO_FIXTURE_1_OF_KIT_1_ID, LEGO_FIXTURE_1_OF_KIT_1_PRODUCTCODE,
				LEGO_FIXTURE_1_OF_KIT_1_BUDS, LEGO_FIXTURE_1_OF_KIT_1_QUANTITY, LEGO_FIXTURE_1_OF_KIT_1_KIT_ID);
		// start the Swing application
		application("com.angelodamante.app.LegoApp")
				.withArgs("--mongo-host=" + containerIpAddress, "--mongo-port=" + mappedPort.toString(),
						"--db-name=" + DB_NAME, "--db-collection-kits-name=" + KIT_COLLECTION_NAME,
						"--db-collection-legos-name=" + LEGO_COLLECTION_NAME)
				.start();
		// get a reference of its JFrame
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Lego Collector".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	private void addTestLegoToDatabase(Integer id, String productCode, Integer buds, Integer quantity, Integer kitId) {
		mongoClient.getDatabase(DB_NAME).getCollection(LEGO_COLLECTION_NAME)
				.insertOne(new Document().append("id", id).append("productCode", productCode).append("buds", buds)
						.append("quantity", quantity).append("kitId", kitId));
	}

	private void addTestKitToDatabase(Integer id, String productCode, String name) {
		mongoClient.getDatabase(DB_NAME).getCollection(KIT_COLLECTION_NAME)
				.insertOne(new Document().append("id", id).append("productCode", productCode).append("name", name));
	}

	@Test
	@GUITest
	public void testOnStartAllKitsInDatabaseElementsAreShown() {
		assertThat(window.list("listKits").contents())
				.anySatisfy(e -> assertThat(e).contains(
						new KitEntity(KIT_FIXTURE_1_ID, KIT_FIXTURE_1_PRODUCTCODE, KIT_FIXTURE_1_NAME).toString()))
				.anySatisfy(e -> assertThat(e).contains(
						new KitEntity(KIT_FIXTURE_2_ID, KIT_FIXTURE_2_PRODUCTCODE, KIT_FIXTURE_2_NAME).toString()));
	}

	@Test
	@GUITest
	public void testWhenClickOnAKitAllLegosOFTheKitAreShown() {
		window.list("listKits").selectItem(Pattern.compile(".*" + KIT_FIXTURE_1_PRODUCTCODE + ".*"));
		assertThat(window.list("listLegos").contents()).anySatisfy(e -> assertThat(e)
				.contains(new LegoEntity(LEGO_FIXTURE_1_OF_KIT_1_ID, LEGO_FIXTURE_1_OF_KIT_1_PRODUCTCODE,
						LEGO_FIXTURE_1_OF_KIT_1_BUDS, LEGO_FIXTURE_1_OF_KIT_1_QUANTITY, LEGO_FIXTURE_1_OF_KIT_1_KIT_ID)
						.toString()));
	}

	@Test
	@GUITest
	public void testDeleteKit() {
		window.list("listKits").selectItem(Pattern.compile(".*" + KIT_FIXTURE_1_PRODUCTCODE + ".*"));
		window.button(JButtonMatcher.withName("btnDeleteKit")).click();
		assertThat(window.list("listKits").contents()).noneMatch(e -> e.contains(KIT_FIXTURE_1_PRODUCTCODE));
	}

	@Test
	@GUITest
	public void testDeleteLego() {
		window.list("listKits").selectItem(Pattern.compile(".*" + KIT_FIXTURE_1_PRODUCTCODE + ".*"));
		window.list("listLegos").selectItem(Pattern.compile(".*" + LEGO_FIXTURE_1_OF_KIT_1_PRODUCTCODE + ".*"));
		window.button(JButtonMatcher.withName("btnDeleteLego")).click();
		assertThat(window.list("listLegos").contents()).noneMatch(e -> e.contains(LEGO_FIXTURE_1_OF_KIT_1_PRODUCTCODE));
	}

	@Test
	@GUITest
	public void testUpdateKit() {
		window.list("listKits").selectItem(Pattern.compile(".*" + KIT_FIXTURE_1_PRODUCTCODE + ".*"));
		window.textBox(JTextComponentMatcher.withName("txtNewKitProductCode")).enterText("p2");
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).enterText("n2");
		window.button(JButtonMatcher.withName("btnUpdateKit")).click();
		assertThat(window.list("listKits").contents()).anySatisfy(e -> assertThat(e)
				.contains(new KitEntity(KIT_FIXTURE_1_ID, KIT_FIXTURE_1_PRODUCTCODE + "p2", KIT_FIXTURE_1_NAME + "n2")
						.toString()));
	}

	@Test
	@GUITest
	public void testAddKit() {
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).enterText("a3a3a3a3");
		window.textBox(JTextComponentMatcher.withName("txtName")).enterText("n2");
		window.button(JButtonMatcher.withName("btnAddKit")).click();
		assertThat(window.list("listKits").contents()).anySatisfy(e -> assertThat(e).contains("a3a3a3a3"));
	}

	@Test
	@GUITest
	public void testAddLegoSuccess() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p2p2");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("8");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("1");
		window.list("listKits").selectItem(0);
		window.button(JButtonMatcher.withName("btnAddLego")).click();
		assertThat(window.list("listLegos").contents()).anySatisfy(e -> assertThat(e).contains("p2p2"));
	}
	
	@Test
	@GUITest
	public void testAddLegoErrorQuantity() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p2p2");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("8");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("aaa");
		window.list("listKits").selectItem(0);
		window.button(JButtonMatcher.withName("btnAddLego")).click();
		assertThat(window.label(JLabelMatcher.withName("lblErrorLog")).text()).contains("Quantity", "Integer");
	}
	
	@Test
	@GUITest
	public void testAddLegoErrorBuds() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p2p2");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("aaaa");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("1");
		window.list("listKits").selectItem(0);
		window.button(JButtonMatcher.withName("btnAddLego")).click();
		assertThat(window.label(JLabelMatcher.withName("lblErrorLog")).text()).contains("Buds", "Integer");
	}

	@Test
	@GUITest
	public void testSearchLegosByBudsSuccess() {
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).enterText(LEGO_FIXTURE_1_OF_KIT_1_BUDS.toString());
		window.button(JButtonMatcher.withName("btnSearchLegos")).click();
		assertThat(window.list("listSearchedLegos").contents()).anySatisfy(e -> assertThat(e).contains(LEGO_FIXTURE_1_OF_KIT_1_PRODUCTCODE));
	}
	
	@Test
	@GUITest
	public void testSearchLegosByBudsErrorNotABud() {
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).enterText("bbb");
		window.button(JButtonMatcher.withName("btnSearchLegos")).click();
		assertThat(window.label(JLabelMatcher.withName("lblErrorLog")).text()).contains("Buds", "Integer");
	}
}
