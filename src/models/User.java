package models;

import java.util.*;

import scala.Function2;
import scala.Tuple2;
import models.Listing;

public class User {
	private List<Tuple2<Function2<Listing,Listing,Boolean>, Listing>> preferenceFactors;
	
	public User(int numPreferenceFactors){
		setPreferenceFactors(new ArrayList<Tuple2<Function2<Listing,Listing,Boolean>, Listing>>(numPreferenceFactors));
	}

	public List<Tuple2<Function2<Listing,Listing,Boolean>, Listing>> getPreferenceFactors() {
		return preferenceFactors;
	}

	public void setPreferenceFactors(List<Tuple2<Function2<Listing,Listing,Boolean>, Listing>> preferenceFactors) {
		this.preferenceFactors = preferenceFactors;
	}
	
	
}
