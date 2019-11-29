package br.leserc.sparkprocess;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface JSONHandler {
	byte[] convertJsonArrayToByteStream(JSONArray array);
	byte[] convertJsonObjectToByteStream(JSONObject json);
	
}
