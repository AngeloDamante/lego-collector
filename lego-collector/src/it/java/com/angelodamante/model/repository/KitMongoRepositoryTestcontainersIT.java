package com.angelodamante.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import com.angelodamante.model.entities.KitEntity;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.junit.After;
import org.junit.Before;

public class KitMongoRepositoryTestcontainersIT {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.4.3").withExposedPorts(27017);

	public static final String DB_NAME = "legoCollector";
	public static final String KIT_COLLECTION_NAME = "kit";

	private MongoClient client;
	private KitMongoRepository kitMongoRepository;
	private MongoCollection<Document> kitCollection;

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getContainerIpAddress(), mongo.getMappedPort(27017)));
		kitMongoRepository = new KitMongoRepository(client, DB_NAME, KIT_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(DB_NAME);

		// make sure we always start with a clean database
		database.drop();
		kitCollection = database.getCollection(KIT_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	private void addTestKitToDatabase(Integer id, String productCode, String name) {
		kitCollection
				.insertOne(new Document().append("id", id).append("productCode", productCode).append("name", name));
	}

	@Test
	public void testAllKits() {
		addTestKitToDatabase(0, "p1", "n");
		addTestKitToDatabase(1, "p2", "n");
		assertThat(kitMongoRepository.getAllKits()).containsExactly(new KitEntity(0, "p1", "n"),
				new KitEntity(1, "p2", "n"));
	}

	private List<KitEntity> readAllKitsFromDatabase() {
		return StreamSupport.stream(kitCollection.find().spliterator(), false)
				.map(d -> new KitEntity(d.getInteger("id"), "" + d.get("productCode"), "" + d.get("name")))
				.collect(Collectors.toList());
	}

	@Test
	public void testAddKit() {
		kitMongoRepository.add("p1", "n");
		kitMongoRepository.add("p2", "n");

		assertThat(readAllKitsFromDatabase()).containsExactly(new KitEntity(0, "p1", "n"), new KitEntity(1, "p2", "n"));
	}

	@Test
	public void testDeleteKit() {
		addTestKitToDatabase(1, "p1", "n1");
		kitMongoRepository.remove(new KitEntity(1, "p1", "n1"));
		assertThat(readAllKitsFromDatabase()).isEmpty();
	}
	
	@Test
	public void testUpdateKit() {
		addTestKitToDatabase(1, "p1", "n1");
		KitEntity k = new KitEntity(1, "pNew", "nNew");
		kitMongoRepository.update(k);
		assertThat(readAllKitsFromDatabase()).containsExactly(k);
	}
}
