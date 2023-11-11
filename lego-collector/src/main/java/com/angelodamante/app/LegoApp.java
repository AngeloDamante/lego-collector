package com.angelodamante.app;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.angelodamante.controller.LegoController;
import com.angelodamante.model.repository.KitMongoRepository;
import com.angelodamante.model.repository.KitRepository;
import com.angelodamante.model.repository.LegoMongoRepository;
import com.angelodamante.model.repository.LegoRepository;
import com.angelodamante.view.LegoSwingView;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class LegoApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String dbName = "legoCollector";

	@Option(names = { "--db-collection-kits-name" }, description = "Kits collection name")
	private String collectionKitsName = "kits";

	@Option(names = { "--db-collection-legos-name" }, description = "Legos collection name")
	private String collectionLegosName = "legos";

	public static void main(String[] args) {
		new CommandLine(new LegoApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));
				LegoSwingView legoView = new LegoSwingView();
				LegoRepository legoRepository = new LegoMongoRepository(mongoClient, dbName, collectionLegosName);
				KitRepository kitRepository = new KitMongoRepository(mongoClient, dbName, collectionKitsName);
				LegoController legoController = new LegoController(legoRepository, kitRepository, legoView);
				legoView.setController(legoController);
				legoView.setVisible(true);
			} catch (Exception e) {
				Logger.getLogger(LegoApp.class.getName()).log(Level.SEVERE, "Exception", e);
			}

		});
		return null;
	}
}
