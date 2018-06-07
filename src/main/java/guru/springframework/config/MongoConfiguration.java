package guru.springframework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration{


	@Override
	public MongoClient mongoClient() {
		
		return new MongoClient("localhost", 27017);
	}

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return "test";
	}


}
