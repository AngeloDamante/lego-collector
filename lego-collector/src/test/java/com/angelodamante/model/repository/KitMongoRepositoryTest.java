package com.angelodamante.model.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import com.angelodamante.model.entities.KitEntity;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class KitMongoRepositoryTest {

	private static final String DB_NAME = "LegoCollector";
	private static final String KIT_COLLECTION_NAME = "Kit";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;

	private KitMongoRepository kitMongoRepository; // SUT
	private MongoClient mongoClient;

	private MongoCollection<Document> kitCollection;

	@BeforeClass
	public static void setupServer() throws Exception {
		mongoServer = new MongoServer(new MemoryBackend());
		serverAddress = mongoServer.bind();
	}

	@AfterClass
	public static void shutdownServer() throws Exception {
		mongoServer.shutdown();
	}

	@Before
	public void setUp() throws Exception {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		kitMongoRepository = new KitMongoRepository(mongoClient, DB_NAME, KIT_COLLECTION_NAME);

		// clean DB
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();

		// connection
		kitCollection = database.getCollection(KIT_COLLECTION_NAME);
	}

	@After
	public void tearDown() throws Exception {
		mongoClient.close();
	}

	private void addTestKitToDatabase(Integer id, String productCode, String name) {
		kitCollection
				.insertOne(new Document().append("id", id).append("productCode", productCode).append("name", name));
	}

	@Test
	public void testGetAllKitsWhenNoKits() {
		assertThat(kitMongoRepository.getAllKits()).isEmpty();
	}

	@Test
	public void testGetAllKitsWhenThereIsOnekit() {
		addTestKitToDatabase(0, "40383", "brick");
		assertThat(kitMongoRepository.getAllKits()).containsExactly(new KitEntity(0, "40383", "brick"));
	}

	private List<KitEntity> readAllKits() {
		return StreamSupport.stream(kitCollection.find().spliterator(), false)
				.map(d -> new KitEntity(d.getInteger("id"), "" + d.get("productCode"), "" + d.get("name")))
				.collect(Collectors.toList());
	}
	
	@Test
	public void testAddKitWhenNoKits() {
		KitEntity k = kitMongoRepository.add("p", "n");
		assertEquals(new KitEntity(0, "p", "n"), k);
		assertThat(readAllKits()).containsExactly(new KitEntity(0, "p", "n"));
	}

	@Test
	public void testAddKitWhenThereIsOnekit() {
		addTestKitToDatabase(0, "p0", "n0");

		KitEntity k = kitMongoRepository.add("p", "n");
		assertEquals(new KitEntity(1, "p", "n"), k);
		assertThat(readAllKits()).containsExactly(new KitEntity(0, "p0", "n0"), new KitEntity(1, "p", "n"));
	}

	@Test
	public void testAddKitWhenThereAreUnorderedKits() {
		addTestKitToDatabase(1, "p1", "n1");
		addTestKitToDatabase(0, "p0", "n0");
		
		KitEntity k = kitMongoRepository.add("p", "n");
		assertEquals(new KitEntity(2, "p", "n"), k);
		assertThat(readAllKits()).containsExactly(new KitEntity(1, "p1", "n1"), new KitEntity(0, "p0", "n0"), new KitEntity(2, "p", "n"));
	}
	
	@Test
	public void testDeleteKit() {
		addTestKitToDatabase(1, "p1", "n1");
		kitMongoRepository.remove(new KitEntity(1, "p1", "n1"));
		assertThat(readAllKits()).isEmpty();
	}
	
	@Test
	public void testUpdateKit() {
		addTestKitToDatabase(1, "p1", "n1");
		KitEntity k = new KitEntity(1, "pNew", "nNew");
		kitMongoRepository.update(k);
		assertThat(readAllKits()).containsExactly(k);
	}
	
	@Test
	public void testUpdateKitWhenNotExist() {
		addTestKitToDatabase(1, "p1", "n1");
		kitMongoRepository.update(new KitEntity(0, "pNew", "nNew"));
		assertThat(readAllKits()).containsExactly(new KitEntity(1, "p1", "n1"));
	}
}
