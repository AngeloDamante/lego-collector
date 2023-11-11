package com.angelodamante.app;

import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import static org.assertj.core.api.Assertions.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
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
	private static final Integer LEGO_FIXTURE_1_OF_KIT_1_KIT_ID = 1;

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
}
