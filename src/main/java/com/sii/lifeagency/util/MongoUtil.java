package com.sii.lifeagency.util;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.sii.lifeagency.configuration.GlobalConfiguration;

public class MongoUtil {
	
	private static MongoTemplate mongoTemplate;

	public static MongoOperations getMongoOp() {

		if (mongoTemplate == null) {
			GlobalConfiguration gc = GlobalConfiguration.getInstance();
			mongoTemplate = new MongoTemplate(new MongoClient(gc.getDatabaseServer()), gc.getDatabaseName());
		}
		return mongoTemplate;
	}

}
