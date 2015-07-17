package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Utils;
import models.SampleValueList;

public class SampleValuesGenerator {
	public static List<SampleValueList> generate(String[] args){
		JSONObject	config = null;
		List<SampleValueList>	sampleList = new ArrayList<SampleValueList>();
		
		if(args.length < 1){
			System.out.println("Please provide the path to the config file");
			System.exit(0);
		}
		
		//retrieve config object
		try {
			config = Utils.readJSONObject(args[0]);
		} catch (JSONException e1) {
			e1.printStackTrace();
			System.out.println("ERROR: error parsing JSON file");
			System.exit(-1);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("ERROR: IO exception occurred");
			System.exit(-1);
		}
		
		if(!config.has("sampleValueFiles")){
			System.out.println("no field found: sampleValueFiles");
			System.exit(-1);
		}
		
		JSONArray valueFileDetails = config.getJSONArray("sampleValueFiles");
		
		//retrieve all of the sample values
		for(int i=0; i < valueFileDetails.length(); i++){
			
			try {
				sampleList.add(SampleValueList.parseFromJSON(valueFileDetails.getJSONObject(i).toString()));
			} catch (JSONException e) {
				e.printStackTrace();
				System.out.println("WARN: JSON exception, could not parse sampleValueFiles["+i+"]");
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("WARN: IO exception attempting to parse sampleValueFiles["+i+"]");
				continue;
			}
		}
		
		return sampleList;
	}
}
