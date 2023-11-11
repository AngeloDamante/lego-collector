package com.angelodamante.model.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import static com.mongodb.client.model.Sorts.descending;

import com.angelodamante.model.entities.KitEntity;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class KitMongoRepository implements KitRepository {

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

	@Override
	public KitEntity add(String productCode, String name) {
		Integer id = 0;
		Document d = kitCollection.find().sort(descending("id")).first();
		if (d != null) {
			id = d.getInteger("id") + 1;
		}
		KitEntity k = new KitEntity(id, productCode, name);
		kitCollection.insertOne(fromKitToDocument(k));
		return k;
	}

	private Document fromKitToDocument(KitEntity k) {
		return new Document().append("id", k.getId()).append("productCode", k.getProductCode()).append("name", k.getName()); 
	}

	@Override
	public void remove(KitEntity kit) {
		kitCollection.deleteOne(Filters.eq("id", kit.getId()));
	}

}
