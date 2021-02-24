package com.sdc2ch.mongodb.service.factory;

import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class MongoDbFactory {

	private MongoDbFactory() {
	};

	private static volatile MongoClient mongoClient3; 
	static {
		try {
			mongoClient3 = new MongoClient(Arrays.asList(
					new ServerAddress("nosql1.seoulmilk.co.kr", 27017),
					new ServerAddress("nosql2.seoulmilk.co.kr", 27017)), new MongoClientOptions.Builder().build());
			mongoClient3.setReadPreference(ReadPreference.secondaryPreferred());
		} catch (Exception e) {

		}
	}

	public static MongoClient getInstance() {
		return mongoClient3;
	}
}
