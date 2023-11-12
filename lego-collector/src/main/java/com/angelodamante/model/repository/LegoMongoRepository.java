package com.angelodamante.model.repository;

import static com.mongodb.client.model.Sorts.descending;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.entities.LegoEntity;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class LegoMongoRepository implements LegoRepository {

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

	@Override
	public LegoEntity add(String productCode, Integer buds, Integer quantity, Integer kitId) {
		Integer id = 0;
		Document d = legoCollection.find().sort(descending("id")).first();
		if (d != null) {
			id = d.getInteger("id") + 1;
		}
		LegoEntity le = new LegoEntity(id, productCode, buds, quantity, kitId);
		legoCollection.insertOne(fromLegoToDocument(le));
		return le;
	}

	private Document fromLegoToDocument(LegoEntity le) {
		return new Document().append("id", le.getId()).append("productCode", le.getProductCode())
				.append("buds", le.getBuds()).append("quantity", le.getQuantity()).append("kitId", le.getKitId());
	}
	
	@Override
	public void remove(LegoEntity lego) {
		legoCollection.deleteOne(Filters.eq("id", lego.getId()));
	}

	@Override
	public List<LegoEntity> getLegosByKitId(Integer kitId) {
		return StreamSupport.stream(legoCollection.find(Filters.eq("kitId", kitId)).spliterator(), false).map(this::fromDocumentToLego)
				.collect(Collectors.toList());
	}

}
