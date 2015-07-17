package main;

import java.util.*;
import java.util.function.Function;

import main.DBDispatcher.FullListingQuery;
import models.Listing;

import org.apache.spark.mllib.recommendation.Rating;

import scala.Tuple2;

/**
 * Correct functionality has been merged into RatingGenerator
 * @author skinsella
 *
 */
@Deprecated
public class RatingGeneratorV2 extends RatingGenerator {

	protected Map<Integer, Listing> listings;
	
	public RatingGeneratorV2(Long randomSeed) {
		super(randomSeed);
	}

	@Override
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
	
	@Override
	protected Listing getListing(int propertyId) {
		return listings.get(propertyId);
	}
}
