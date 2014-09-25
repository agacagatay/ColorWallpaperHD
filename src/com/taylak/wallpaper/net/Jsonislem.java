package com.taylak.wallpaper.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class Jsonislem {
	
	public  static ArrayList<JSONObject> JsonList(String _JsonString) throws JSONException {
		
		ArrayList<JSONObject> retValue = null;
		
		JSONArray jsonArrayObject;
				
			jsonArrayObject = new JSONArray(_JsonString);
			retValue = new ArrayList<JSONObject>(jsonArrayObject.length());
			
			 for(int i=0;i<jsonArrayObject.length();i++){
				 JSONObject jsonObject = jsonArrayObject.getJSONObject(i);
				 retValue.add(jsonObject);
			 }
		 
		
		return   retValue;
	}

}
