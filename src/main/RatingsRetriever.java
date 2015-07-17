package main;

import java.util.List;

import org.apache.spark.mllib.recommendation.Rating;

import api.DBAdapter;
import api.IRatingsRetriever;

public class RatingsRetriever extends DBAdapter implements IRatingsRetriever{

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://10.153.7.114:3306/mydb";
	// Database Credentials
	static final String USER = "root";
	static final String PWD = "aspect2015";
	
	@Override
	public List<Rating> retrieve() {
		// TODO Auto-generated method stub
		return null;
	}
}
