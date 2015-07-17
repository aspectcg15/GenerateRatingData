package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

import models.Listing;
import models.address;
import models.property;
import models.propertyphoto;
import models.users;
import models.longtext.longtextCastException;
import models.varchar45.varchar45CastException;

public class ResultSetListingParser implements Function<ResultSet, List<Listing>> {

	@Override
	public List<Listing> apply(ResultSet resultSet) {
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

}
