package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import models.Listing;
import models.address;
import models.property;
import models.propertyphoto;
import models.users;
import models.longtext.longtextCastException;
import models.varchar45.varchar45CastException;

import org.apache.spark.mllib.recommendation.Rating;

public class DBDispatcher {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://10.153.7.114:3306/mydb";
	// Database Credentials
	static final String USER = "root";
	static final String PWD = "aspect2015";
	
	public static void dispatch(List<Rating> generatedRatings) {
		if(generatedRatings == null) return;
		
		try {
			// Register JDBC Driver
			Class.forName(JDBC_DRIVER);
		
			System.out.println("Connecting to Database");
			// Open a Connection
			try(Connection conn = DriverManager.getConnection(DB_URL, USER, PWD);){
				PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO user_ratings (user_id, prod_id, rating)"
						+ " VALUES (?,?,?)"
						+ " ON DUPLICATE KEY UPDATE rating=?;");
				
				for(Rating r: generatedRatings){
					if(r == null) continue;
	
					prepStatement.setInt(1, r.user());
					prepStatement.setInt(2, r.product());
					prepStatement.setDouble(3, r.rating());
					prepStatement.setDouble(4, r.rating());
					
					prepStatement.executeUpdate();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String queryForInsertRating(Rating r){
		return "INSERT INTO user_ratings (user_id, prod_id, rating)"
				+ " VALUES ("+r.user()+","+r.product()+","+r.rating()+");";
	}
	
	public static String queryForUpdateRating(Rating r){
		return "UPDATE user_ratings "
				+ "SET rating="+r.rating()
				+ " WHERE user_id="+r.user()
				+ " AND prod_id="+r.product()
				+ ";";
	}
	
	public static String queryForOnDuplicateUpdate(Rating r){
		return "INSERT INTO user_ratings (user_id, prod_id, rating)"
				+ " VALUES ("+r.user()+","+r.product()+","+r.rating()+")"
				+ " ON DUPLICATE KEY UPDATE rating="+r.rating()+";";
	}
	
	public static List<Listing> getListingFromRange(int i, int j) {
		FullListingQuery query = new FullListingQuery("listing_id BETWEEN "+i+" AND "+j);
		
		List<Listing> result = runQuery(query.toString(), (a) -> { return parseListing(a);});
		
		return result;
	}
	
	public static Listing getListing(int i){
		
		FullListingQuery query = new FullListingQuery("listing_id="+i);
		
		List<Listing> result = runQuery(query.toString(), (a) -> { return parseListing(a);});
		
		if(result.size() != 1){
			return null;
		}else{
			return result.get(0);
		}
	}
	
	/**
	 * Note: resultSet needs to be still open for this method to work
	 * I suggest using as a lambda: (a) -> {return parseListing(a);}
	 * @param resultSet
	 * @return
	 */
	public static List<Listing> parseListing(ResultSet resultSet) {
		if(resultSet == null){
			return null;
		}
		
		List<Listing>	result = new ArrayList<Listing>();
		
		try {
			while(resultSet.next()){
				
				Listing tempListing = new Listing();
				
				property tempProperty = new property();
				propertyphoto tempPropertyphoto = new propertyphoto();
				users tempUsers = new users();

				address tempPropertyAddress = new address();
				address tempUsersAddress = new address();

				tempProperty.setPropertyAddress(tempPropertyAddress);
				tempUsers.setUsersAddress(tempUsersAddress);
				
				tempListing.setProperty(tempProperty);
				tempListing.setPropertyphoto(tempPropertyphoto);
				tempListing.setUsers(tempUsers);
				
				//address
				try{
					tempPropertyAddress.setZip_id(resultSet.getInt("zip_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempPropertyAddress.setZip_code(resultSet.getString("zip_code"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempPropertyAddress.setCity(resultSet.getString("city"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempPropertyAddress.setState(resultSet.getString("state"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempPropertyAddress.setCountry(resultSet.getString("country"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempPropertyAddress.setAddressField(resultSet.getString("address"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				//end address
				//property
				try{
					tempProperty.setProp_id(resultSet.getInt("prop_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempProperty.setPrice(resultSet.getDouble("price"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempProperty.setSize(resultSet.getString("size"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempProperty.setBed(resultSet.getInt("bed"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempProperty.setBath(resultSet.getInt("bath"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempProperty.setDescription(resultSet.getString("description"));
				}catch(SQLException | longtextCastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempProperty.setType(resultSet.getString("type"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempProperty.setAddress_zip_id_1(resultSet.getInt("address_zip_id1"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				//end property
				//propertylisting
				try{
					tempListing.setListing_id(resultSet.getInt("listing_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempPropertyphoto.setPhoto_id(resultSet.getInt("propertyphoto_photo_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempProperty.setProp_id(resultSet.getInt("property_prop_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempUsers.setUser_id(resultSet.getInt("users_user_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				//end propertylisting
				//propertyphoto
				try{
					tempPropertyphoto.setPhoto_id(resultSet.getInt("photo_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempPropertyphoto.setPhoto_name(resultSet.getString("photo_name"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempPropertyphoto.setPhoto_path(resultSet.getString("photo_path"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				//end propertyphoto
				//users
				try{
					tempUsers.setUser_id(resultSet.getInt("user_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				try{
					tempUsers.setFname(resultSet.getString("fname"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempUsers.setLname(resultSet.getString("lname"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempUsers.setDob(resultSet.getString("dob"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempUsers.setGender(resultSet.getString("gender"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempUsers.setContact(resultSet.getString("contact"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempUsers.setEmail(resultSet.getString("email"));
				}catch(SQLException | varchar45CastException e){
					System.out.println(e.getMessage());
				}
				try{
					tempUsers.setAddress_zip_id(resultSet.getInt("address_zip_id"));
				}catch(SQLException e){
					System.out.println(e.getMessage());
				}
				//end users
				
				result.add(tempListing);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return result;
	}

	public static <T> T runQuery(FullListingQuery query, Function<ResultSet, T> parser) {
		return runQuery(query.toString(), parser);
	}
	
	public static <T> T runQuery(String query, Function<ResultSet, T> parser) {
		T result = null;
		try {
			// Register JDBC Driver
			Class.forName(JDBC_DRIVER);
		
			System.out.println("Connecting to Database");
			// Open a Connection
			try(Connection conn = DriverManager.getConnection(DB_URL, USER, PWD);){
				PreparedStatement preparedStatement = conn.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery();
				if(parser != null)
					result = parser.apply(resultSet);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	public static class FullListingQuery {
		private String query = "SELECT a.*, b.*, c.*, d.*, e.*, f.* FROM propertylisting a, property b, propertyphoto c, address d, users e, address f WHERE a.property_prop_id=b.prop_id AND a.propertyphoto_photo_id=c.photo_id AND b.address_zip_id1=d.zip_id AND a.users_user_id=e.user_id AND e.address_zip_id=f.zip_id";
		
		@SuppressWarnings("unused")
		private FullListingQuery(){
			query += ";";
		}
		
		public FullListingQuery(String where){
			query += " AND "+ where+ ";";
		}
		
		public String toString(){
			return query;
		}
	}
}
