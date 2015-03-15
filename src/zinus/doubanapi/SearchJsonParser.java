package zinus.doubanapi;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;

public class SearchJsonParser {
	
	private String imgSize;
	public SearchJsonParser(String _imgSize) throws Exception{
		if(!_imgSize.equals("small") &&
				!_imgSize.equals("large") && !_imgSize.equals("medium"))
			throw new Exception("image size arguement error: " + _imgSize);
		else
			imgSize = _imgSize;
	}
	
	public Bundle parseJson(String _json) {
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<String> years = new ArrayList<String>();
		ArrayList<String> people = new ArrayList<String>();
		ArrayList<String> imgUrls = new ArrayList<String>();
		
		JSONTokener jsonParser = new JSONTokener(_json);
    	JSONArray jsons = null;
		try {
			jsons = ((JSONObject)jsonParser.nextValue())
					.getJSONArray("subjects");
			
        	JSONObject json = null;
        	
        	for(int i = 0; i < jsons.length(); i++) {
        		json = (JSONObject)jsons.opt(i);
        		
        		// get titles
        		String otherTitle = json.getString("original_title");
        		int otherTitleLength = otherTitle.length();
        		if(otherTitleLength == 0) {
        			titles.add(json.getString("title"));
        		}
        		else {
        			if(otherTitle.charAt(otherTitleLength - 1) == ' ')
        				otherTitle = (String) otherTitle.subSequence(0, otherTitleLength - 1);
	        		titles.add(json.getString("title") +
	        				" (" + otherTitle + ")");
        		}
        		// get years
        		years.add(json.getString("year"));
        		// get image urls
        		imgUrls.add(json.getJSONObject("images").getString(imgSize));
        		
        		// get casts
        		JSONArray peopleJsons = json.getJSONArray("casts");
        		if(peopleJsons.length() == 0) {
        			people.add("");
        			continue;
        		}
        		StringBuilder peopleString =  new StringBuilder(
        				((JSONObject) peopleJsons.opt(0)).getString("name"));
        		
        		for(int j = 1; j < peopleJsons.length(); j++)
        			peopleString.append(", " +
        					((JSONObject) peopleJsons.opt(j)).getString("name"));
        		people.add(peopleString.toString());
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Bundle data = new Bundle();
		data.putStringArrayList("title", titles);
		data.putStringArrayList("year", years);
		data.putStringArrayList("imgUrl", imgUrls);
		data.putStringArrayList("people", people);
		
		return data;
	}
}
