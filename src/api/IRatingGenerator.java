package api;

import java.util.List;

import org.apache.spark.mllib.recommendation.Rating;

public interface IRatingGenerator {
	public List<Rating> generateRatings(int userIndex, int numRatings);
}
