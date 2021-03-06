package main;

public class GenerateRatingDataController {

	final static Long RANDOM_SEED = 9384509872359L;
	final static int NUM_USERS = 5000;
	final static int NUM_RATINGS_PER_USER = 20;
	
	public static void main(String[] args) {
		RatingGenerator rg = new RatingGenerator(RANDOM_SEED);
		
		for(int userIndex=0; userIndex<NUM_USERS; userIndex++){
			DBDispatcher.dispatch( rg.generateRatings(userIndex, NUM_RATINGS_PER_USER) );
		}
	}
}
