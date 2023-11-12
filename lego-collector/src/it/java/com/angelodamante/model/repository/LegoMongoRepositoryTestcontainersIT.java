package com.angelodamante.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import com.angelodamante.model.entities.LegoEntity;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.junit.After;
import org.junit.Before;

public class LegoMongoRepositoryTestcontainersIT {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.4.3").withExposedPorts(27017);

	public static final String DB_NAME = "legoCollector";
	public static final String LEGO_COLLECTION_NAME = "lego";

	private MongoClient client;
	private LegoMongoRepository legoMongoRepository;
	private MongoCollection<Document> legoCollection;

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getContainerIpAddress(), mongo.getMappedPort(27017)));
		legoMongoRepository = new LegoMongoRepository(client, DB_NAME, LEGO_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(DB_NAME);

		// make sure we always start with a clean database
		database.drop();
		legoCollection = database.getCollection(LEGO_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	private void addTestLegoToDatabase(Integer id, String productCode, Integer buds, Integer quantity, Integer kitId) {
		legoCollection.insertOne(new Document().append("id", id).append("productCode", productCode).append("buds", buds)
				.append("quantity", quantity).append("kitId", kitId));
	}

	@Test
	public void testAllLegos() {
		addTestLegoToDatabase(0, "6383", 8, 3, 1);
		addTestLegoToDatabase(1, "0451", 3, 2, 1);
		assertThat(legoMongoRepository.getAllLegos()).containsExactly(new LegoEntity(0, "6383", 8, 3, 1),
				new LegoEntity(1, "0451", 3, 2, 1));
	}

	public List<LegoEntity> readAllLegosFromDB() {
		return StreamSupport.stream(legoCollection.find().spliterator(), false)
				.map(d -> new LegoEntity(d.getInteger("id"), "" + d.get("productCode"), d.getInteger("buds"),
						d.getInteger("quantity"), d.getInteger("kitId")))
				.collect(Collectors.toList());
	}

	@Test
	public void testAddLego() {
		legoMongoRepository.add("p1", 1, 1, 1);
		legoMongoRepository.add("p2", 2, 2, 2);

		assertThat(readAllLegosFromDB()).containsExactly(new LegoEntity(0, "p1", 1, 1, 1),
				new LegoEntity(1, "p2", 2, 2, 2));
	}

	@Test
	public void testDeleteLego() {
		addTestLegoToDatabase(0, "6383", 8, 3, 1);
		legoMongoRepository.remove(new LegoEntity(0, "6383", 8, 3, 1));
		assertThat(readAllLegosFromDB()).isEmpty();
	}

}
