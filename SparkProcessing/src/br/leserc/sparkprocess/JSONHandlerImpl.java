package br.leserc.sparkprocess;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONHandlerImpl implements JSONHandler {

	@Override
	public byte[] convertJsonArrayToByteStream(JSONArray array) {
		
		return array.toJSONString().getBytes();
	}

	@Override
	public byte[] convertJsonObjectToByteStream(JSONObject json) {
		// TODO Auto-generated method stub
		
		return json.toJSONString().getBytes();
	}

	
}