package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.apache.spark.mllib.recommendation.Rating;
import org.json.JSONException;

import api.IRatingGenerator;
import scala.Tuple2;
import main.DBDispatcher.FullListingQuery;
import models.Listing;
import models.SampleValueList;
import models.ValueType;
import models.address;
import models.property;
import models.propertyphoto;
import models.users;

public class RatingGenerator implements IRatingGenerator{

	final static Long DEFAULT_RANDOM_SEED = 9384509872359L;
	final static int LOWER_BOUND_FOR_NUM_USER_FACTORS = 1;
	final static int UPPER_BOUND_FOR_NUM_USER_FACTORS = 6;
	final static int NUM_PROPERTIES = 100;
	
	static List<SampleValueList> SampleValueListForAllFields;
	{
		String[] strA = new String[1];
		strA[0] = "D:\\workspace\\GenerateRatingData\\GenerateListingsConfig.json";
		SampleValueListForAllFields = SampleValuesGenerator.generate(strA);
	}
	
	static List<Function<Tuple2<Listing, Listing>, Boolean>> options; //listing to rate, listing preference by user, whether condition is met 
	{
		options = new ArrayList<Function<Tuple2<Listing, Listing>, Boolean>>();
		//tu2LP = tu2 of listing:Listing, preference:Listing
		options.add(tu2LP -> tu2LP._1.getPropertyAddress().getCity().equals(tu2LP._2.getPropertyAddress().getCity()));	//city equal
		options.add(tu2LP -> tu2LP._1.getPropertyAddress().getAddressField().equals(tu2LP._2.getPropertyAddress().getAddressField()));	//street equal
		options.add(tu2LP -> tu2LP._1.getBath() > 1);	//numBath > 1
		options.add(tu2LP -> tu2LP._1.getBed() > 3);	//numBed > 3
		options.add(tu2LP -> tu2LP._1.getPhoto_path().equals(tu2LP._2.getPhoto_path()));	//photo_path equal
		options.add(tu2LP -> tu2LP._1.getPrice() < 100000.0);	//price < 100000.0
		options.add(tu2LP -> tu2LP._1.getType().equals(tu2LP._2.getType()));	//type equal
	}
	
	Random rdm;
	protected Map<Integer, Listing> listings;
	
	public RatingGenerator(Long randomSeed){
		rdm = new Random(randomSeed);
		//listings = DBDispatcher.getListingFromRange(0, NUM_PROPERTIES-1);
	}
	
	public List<SampleValueList> createSampleValueListForAllFields() {
		
		return null;
	}

	protected int calculateRating(Listing listing, Listing preferredListing, List<Function<Tuple2<Listing, Listing>, Boolean>> factors) {
		int totalNumFactors = factors.size();
		int numMatchingFactors = 0;
		
		for(Function<Tuple2<Listing, Listing>, Boolean> factor: factors){
			if(factor.apply(new Tuple2<Listing, Listing>(listing, preferredListing))){
				numMatchingFactors++;
			}
		}
		
		return Math.round((numMatchingFactors/(float)totalNumFactors)*4)+1;
	}

	protected int selectAPropertyId() {
		return rdm.nextInt(NUM_PROPERTIES);
	}

	public Listing generateListing(int primaryKey) {
		address address = new address();
		property property = new property();
		Listing propertylisting = new Listing();
		propertyphoto	propertyphoto = new propertyphoto();
		users users = new users();

		propertylisting.setProperty(property);
		property.setPropertyAddress(address);
		propertylisting.setPropertyphoto(propertyphoto);
		propertylisting.setUsers(users);
		
		SampleValueListForAllFields.forEach( sample -> {
			
			boolean pk = false;
			switch(sample.name){
			case user_id:	users.setUser_id(primaryKey); propertylisting.setUser_id(primaryKey); pk = true; break;
			case zip_id:	address.setZip_id(primaryKey); property.setAddress_zip_id_1(primaryKey); users.setAddress_zip_id(primaryKey); pk = true; break;
			case prop_id:	property.setProp_id(primaryKey); propertylisting.setProp_id(primaryKey); pk = true; break;
			case listing_id:	propertylisting.setListing_id(primaryKey); pk = true; break;
			case photo_id:	propertyphoto.setPhoto_id(primaryKey); propertylisting.setPhoto_id(primaryKey); pk = true; break;
			default: break;
			}
			
			if(pk)
				return;
			
			ValueType valueType = sample.valueType;
			String	str0 = null;
			int var0 = 0;
			double dub0 = 0;
			
			switch(valueType){
			case STRING:	str0 = sample.sampleValues.getString(rdm.nextInt(sample.sampleValues.length())); break;
			case INT:		var0 = sample.sampleValues.getInt(rdm.nextInt(sample.sampleValues.length())); break; 
			case DOUBLE:	dub0 = sample.sampleValues.getDouble(rdm.nextInt(sample.sampleValues.length())); break;
			default: throw new JSONException("Unrecognized valueType");
			}
			
			switch(sample.name){
			case fname:		try {users.setFname(str0);} catch (Exception e) {e.printStackTrace();} break;
			case lname:		try {users.setLname(str0);} catch (Exception e) {e.printStackTrace();} break;
			case dob:		try {users.setDob(str0);} catch (Exception e) {e.printStackTrace();} break;
			case gender:		try {users.setGender(str0);} catch (Exception e) {e.printStackTrace();} break;
			case contact:		try {users.setContact(str0);} catch (Exception e) {e.printStackTrace();} break;
			case email:		try {users.setEmail(str0);} catch (Exception e) {e.printStackTrace();} break;
			case price:		property.setPrice(dub0); break;
			case size:		try {property.setSize(str0);} catch (Exception e) {e.printStackTrace();} break;
			case bed:		property.setBed(var0); break;
			case bath:		property.setBath(var0); break;
			case description:		try {property.setDescription(str0);} catch (Exception e) {e.printStackTrace();} break;
			case type:		try {property.setType(str0);} catch (Exception e) {e.printStackTrace();} break;
			case zip_code:		try {address.setZip_code(str0);} catch (Exception e) {e.printStackTrace();} break;
			case city:		try {address.setCity(str0);} catch (Exception e) {e.printStackTrace();} break;
			case state:		try {address.setState(str0);} catch (Exception e) {e.printStackTrace();} break;
			case country:		try {address.setCountry(str0);} catch (Exception e) {e.printStackTrace();} break;
			case address:		try {address.setAddressField(str0);} catch (Exception e) {e.printStackTrace();} break;
			case photo_name:	try {propertyphoto.setPhoto_name(str0);} catch (Exception e) {e.printStackTrace();} break;
			case photo_path:	try {propertyphoto.setPhoto_path(str0);} catch (Exception e) {e.printStackTrace();} break;
			default: throw new JSONException("Unrecognized field name");
			}
		});
		
		return propertylisting;
	}

	public List<Function<Tuple2<Listing, Listing>, Boolean>> selectFactors(int numFactors) {
		if(options == null)
			return null;
		
		List<Function<Tuple2<Listing, Listing>, Boolean>> result = new ArrayList<Function<Tuple2<Listing, Listing>, Boolean>>((numFactors < options.size()?numFactors:options.size()));
		if(numFactors > options.size()){
			result.addAll(options);
		}else{
			int numSelected = 0;
			while(numSelected < numFactors){
				Function<Tuple2<Listing, Listing>, Boolean> option = selectOption();
				if(!result.contains(option)){
					result.add(option);
					numSelected++;
				}
			}
		}
		return result;
	}

	public Function<Tuple2<Listing, Listing>, Boolean> selectOption(){
		if(options == null)
			return null;
		
		if(rdm == null)
			rdm = new Random(DEFAULT_RANDOM_SEED);
		
		int index = rdm.nextInt(options.size());
		return options.get(index);
	}
	
	public int generateNumFactors() {
		if(rdm == null)
			rdm = new Random(DEFAULT_RANDOM_SEED);
		return rdm.nextInt(UPPER_BOUND_FOR_NUM_USER_FACTORS - LOWER_BOUND_FOR_NUM_USER_FACTORS)+LOWER_BOUND_FOR_NUM_USER_FACTORS;
	}

	public List<Rating> generateRatings(int userIndex, int numRatings) {
		List<Rating> results = new ArrayList<Rating>(numRatings);
		
		int numFactors = generateNumFactors();
		List<Function<Tuple2<Listing, Listing>, Boolean>> factors = selectFactors(numFactors);
		
		Listing preferredListing = generateListing(userIndex);
		
		int propertyId;
		List<Integer> alreadyRatedProperties = new ArrayList<Integer>(numRatings);
		int numSelected=0;
		
		if(listings == null){
			initializeListings();
		}
		
		while( numSelected < numRatings){
			propertyId = selectAPropertyId();
			if(!alreadyRatedProperties.contains(propertyId)){
				numSelected++;
				alreadyRatedProperties.add(propertyId);
				results.add(new Rating(userIndex, propertyId, calculateRating(getListing(propertyId), preferredListing, factors)));
			}
		}
		
		return results;
	}

	protected void initializeListings() {
		listings = new HashMap<Integer, Listing>(NUM_PROPERTIES);
		
		List<Listing> results = DBDispatcher.runQuery(new FullListingQuery(), new ResultSetListingParser());
		
		for(Listing listing: results){
			listings.put(listing.getListing_id(), listing);
		}
	}

	protected Listing getListing(int propertyId) {
		return listings.get(propertyId);
	}
}
