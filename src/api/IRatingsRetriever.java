package api;

import java.util.List;

import org.apache.spark.mllib.recommendation.Rating;

public interface IRatingsRetriever {
	public List<Rating> retrieve();
}
