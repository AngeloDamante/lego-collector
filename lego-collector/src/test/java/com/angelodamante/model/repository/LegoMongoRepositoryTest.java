package com.angelodamante.model.repository;

import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import com.angelodamante.model.entities.LegoEntity;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class LegoMongoRepositoryTest {

	private static final String DB_NAME = "LegoCollector";
	private static final String LEGO_COLLECTION_NAME = "Lego";
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;

	private LegoMongoRepository legoMongoRepository; // SUT
	private MongoClient mongoClient;

	private MongoCollection<Document> legoCollection;

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
		legoMongoRepository = new LegoMongoRepository(mongoClient, DB_NAME, LEGO_COLLECTION_NAME);

		// clean DB
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();

		// connection
		legoCollection = database.getCollection(LEGO_COLLECTION_NAME);
	}

	@After
	public void tearDown() throws Exception {
		mongoClient.close();
	}

	private void addTestLegoToDatabase(Integer id, String productCode, Integer buds, Integer quantity, Integer kitId) {
		legoCollection.insertOne(new Document().append("id", id).append("productCode", productCode).append("buds", buds)
				.append("quantity", quantity).append("kitId", kitId));
	}

	@Test
	public void testGetAllLegosWhenNoLegos() {
		assertThat(legoMongoRepository.getAllLegos()).isEmpty();
	}

	@Test
	public void testGetAllLegosWhenThereIsOneLego() {
		addTestLegoToDatabase(0, "6383", 8, 3, 1);
		assertThat(legoMongoRepository.getAllLegos()).containsExactly(new LegoEntity(0, "6383", 8, 3, 1));
	}

}
