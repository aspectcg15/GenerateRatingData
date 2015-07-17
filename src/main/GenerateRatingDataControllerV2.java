package main;

public class GenerateRatingDataControllerV2 {

	final static Long RANDOM_SEED = 9384509872359L;
	final static int NUM_USERS = 10;
	final static int NUM_RATINGS_PER_USER = 20;
	
	/**
	 * Correct functionality has been merged into GenerateRatingDataController
	 * @author skinsella
	 *
	 */
	@Deprecated
	public static void main(String[] args) {
		RatingGenerator rg = new RatingGeneratorV2(RANDOM_SEED);
		
		for(int userIndex=0; userIndex<NUM_USERS; userIndex++){
			DBDispatcher.dispatch( rg.generateRatings(userIndex, NUM_RATINGS_PER_USER) );
		}
	}
}