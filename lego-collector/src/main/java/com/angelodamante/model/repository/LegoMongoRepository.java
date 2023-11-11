package com.angelodamante.model.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.angelodamante.model.entities.LegoEntity;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class LegoMongoRepository {

	private MongoCollection<Document> legoCollection;

	public LegoMongoRepository(MongoClient mongoClient, String dbName, String collectionName) {
		legoCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
	}

	private LegoEntity fromDocumentToLego(Document d) {
		return new LegoEntity(d.getInteger("id"), "" + d.get("productCode"), d.getInteger("buds"),
				d.getInteger("quantity"), d.getInteger("kitId"));
	}

	public List<LegoEntity> getAllLegos() {
		return StreamSupport.stream(legoCollection.find().spliterator(), false).map(this::fromDocumentToLego)
				.collect(Collectors.toList());
	}

}
