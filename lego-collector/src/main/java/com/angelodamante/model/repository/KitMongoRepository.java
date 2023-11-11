package com.angelodamante.model.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.angelodamante.model.entities.KitEntity;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class KitMongoRepository {

	private MongoCollection<Document> kitCollection;

	public KitMongoRepository(MongoClient mongoClient, String dbName, String collectionName) {
		kitCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
	}

	private KitEntity fromDocumentToKit(Document d) {
		return new KitEntity(d.getInteger("id"), "" + d.get("productCode"), "" + d.get("name"));
	}

	public List<KitEntity> getAllKits() {
		return StreamSupport.stream(kitCollection.find().spliterator(), false).map(this::fromDocumentToKit)
				.collect(Collectors.toList());
	}

}
