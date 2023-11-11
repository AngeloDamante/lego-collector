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
	
	private void addTestLegoToDatabase(String name) {
		legoCollection.insertOne(new Document().append("name", name));
	}

	@Test
	public void testGetAllLegosWhenNoLegos() {
		assertThat(legoMongoRepository.getAllLegos()).isEmpty();
	}
	
	@Test
	public void testGetAllLegosWhenThereIsOneLego() {
		addTestLegoToDatabase("ciao");
		assertThat(legoMongoRepository.getAllLegos()).containsExactly(new LegoEntity("ciao"));
	}
	
	

}
