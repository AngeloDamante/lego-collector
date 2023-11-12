package com.angelodamante.view;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.angelodamante.controller.LegoController;
import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.entities.LegoEntity;
import com.angelodamante.model.repository.KitMongoRepository;
import com.angelodamante.model.repository.LegoMongoRepository;
import com.angelodamante.model.repository.LegoRepository;
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
			legoMongoRepository = new LegoMongoRepository(mongoClient, DB_NAME, LEGO_COLLECTION_NAME);
			kitMongoRepository = new KitMongoRepository(mongoClient, DB_NAME, KIT_COLLECTION_NAME);
			for (LegoEntity lego : legoMongoRepository.getAllLegos()) {
				legoMongoRepository.remove(lego);
			}
			for (KitEntity kit : kitMongoRepository.getAllKits()) {
				kitMongoRepository.remove(kit);
			}
			legoSwingView = new LegoSwingView();
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
	
	@Test
	public void testShowAllKits() {
		KitEntity kit1 = new KitEntity(0, "6383", "n1");
		KitEntity kit2 = new KitEntity(1, "a5a5", "n2");
		kitMongoRepository.add("6383", "n1");
		kitMongoRepository.add("a5a5", "n2");

		GuiActionRunner.execute(() -> legoController.allKits());

		assertThat(window.list("listKits").contents()).containsExactly(kit1.toString(), kit2.toString());
	}

	@Test
	public void testAddKit() {
		window.textBox(JTextComponentMatcher.withName("txtProductCode")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtName")).enterText("n");
		window.button(JButtonMatcher.withName("btnAddKit")).click();

		String[] listContents = window.list("listKits").contents();
		assertThat(listContents).containsExactly(new KitEntity(0, "p", "n").toString());
	}

	@Test
	public void testAddLegoSuccess() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("2");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("1");

		KitEntity kit = new KitEntity(77, "6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);

		window.button(JButtonMatcher.withName("btnAddLego")).click();

		String[] listContents = window.list("listLegos").contents();
		assertThat(listContents).containsExactly(new LegoEntity(0, "p", 2, 1, kit.getId()).toString());
	}

	@Test
	public void testAddLegoErrorBuds() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("aaa");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("1");

		KitEntity kit = new KitEntity(77, "6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);

		window.button(JButtonMatcher.withName("btnAddLego")).click();

		assertThat(window.label(JLabelMatcher.withName("lblErrorLog")).text()).contains("Buds", "Integer");
	}

	@Test
	public void testAddLegoErrorQuantity() {
		window.textBox(JTextComponentMatcher.withName("txtProductCodeLego")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtBudsLego")).enterText("1");
		window.textBox(JTextComponentMatcher.withName("txtQuantityLego")).enterText("aaaa");

		KitEntity kit = new KitEntity(77, "6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);

		window.button(JButtonMatcher.withName("btnAddLego")).click();

		assertThat(window.label(JLabelMatcher.withName("lblErrorLog")).text()).contains("Quantity", "Integer");
	}

	@Test
	public void testDeleteLego() {
		LegoEntity lego = new LegoEntity(0, "6383", 8, 3, 1);
		legoMongoRepository.add("6383", 8, 3, 1);
		GuiActionRunner.execute(() -> legoSwingView.onAddedLego(lego));
		window.list("listLegos").selectItem(0);
		window.button(JButtonMatcher.withName("btnDeleteLego")).click();
		assertThat(window.list("listLegos").contents()).isEmpty();
	}
	
	@Test
	public void testDeleteKit() {
		KitEntity kit = new KitEntity(0, "6383", "n");
		kitMongoRepository.add("6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		window.button(JButtonMatcher.withName("btnDeleteKit")).click();
		assertThat(window.list("listKits").contents()).isEmpty();
	}
	
	@Test
	public void testUpdateKit() {
		KitEntity kit = new KitEntity(0, "6383", "n");
		kitMongoRepository.add("6383", "n");
		GuiActionRunner.execute(() -> legoSwingView.onAddedKit(kit));
		window.list("listKits").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtNewKitProductCode")).enterText("p");
		window.textBox(JTextComponentMatcher.withName("txtNewKitName")).enterText("n");
		
		window.button(JButtonMatcher.withName("btnUpdateKit")).click();
		
		assertThat(window.list("listKits").contents()).containsExactly(new KitEntity(0,  "6383p", "nn").toString());
	}
	
	@Test
	public void testSearchLegoByBudsSuccess() {
		LegoEntity lego = new LegoEntity(0, "6383", 8, 3, 1);
		legoMongoRepository.add("6383", 8, 3, 1);
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).enterText("8");
		window.button(JButtonMatcher.withName("btnSearchLegos")).click();
		
		assertThat(window.list("listSearchedLegos").contents()).containsExactly(lego.toString());
	}
	
	@Test
	public void testSearchLegoByBudsError() {
		LegoEntity lego = new LegoEntity(0, "6383", 8, 3, 1);
		legoMongoRepository.add("6383", 8, 3, 1);
		window.textBox(JTextComponentMatcher.withName("txtSearchBuds")).enterText("aa");
		window.button(JButtonMatcher.withName("btnSearchLegos")).click();
		
		assertThat(window.label(JLabelMatcher.withName("lblErrorLog")).text()).contains("Buds", "Integer");
	}
}
