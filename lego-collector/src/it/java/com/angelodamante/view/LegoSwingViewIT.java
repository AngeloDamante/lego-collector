package com.angelodamante.view;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.angelodamante.controller.LegoController;
import com.angelodamante.model.entities.LegoEntity;
import com.angelodamante.model.repository.KitMongoRepository;
import com.angelodamante.model.repository.LegoMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@RunWith(GUITestRunner.class)
public class LegoSwingViewIT extends AssertJSwingJUnitTestCase {
	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient mongoClient;

	private FrameFixture window;
	private LegoSwingView legoSwingView;
	private LegoController legoController;
	private LegoMongoRepository legoMongoRepository;
	private KitMongoRepository kitMongoRepository;

	public static final String DB_NAME = "legoCollector";
	public static final String KIT_COLLECTION_NAME = "kit";
	public static final String LEGO_COLLECTION_NAME = "lego";

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		// bind on a random local port
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Override
	protected void onSetUp() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		GuiActionRunner.execute(() -> {
			legoSwingView = new LegoSwingView();
			legoMongoRepository = new LegoMongoRepository(mongoClient, DB_NAME, LEGO_COLLECTION_NAME);
			kitMongoRepository = new KitMongoRepository(mongoClient, DB_NAME, KIT_COLLECTION_NAME);
			legoController = new LegoController(legoMongoRepository, kitMongoRepository, legoSwingView);
			legoSwingView.setController(legoController);
			return legoSwingView;
		});
		window = new FrameFixture(robot(), legoSwingView);
		window.show(); // shows the frame to test
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test
	public void testShowAllLegos() {
		LegoEntity lego1 = new LegoEntity(0, "p0", 1, 2, 1);
		LegoEntity lego2 = new LegoEntity(1, "p1", 2, 3, 4);
		legoMongoRepository.add("p0", 1, 2, 1);
		legoMongoRepository.add("p1", 2, 3, 4);

		GuiActionRunner.execute(() -> legoController.allLegos());

		assertThat(window.list("listLegos").contents()).containsExactly(lego1.toString(), lego2.toString());
	}
}
