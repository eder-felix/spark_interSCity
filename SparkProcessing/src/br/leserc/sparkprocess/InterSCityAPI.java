package br.leserc.sparkprocess;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public abstract class InterSCityAPI {
	@SuppressWarnings("unchecked")
	public static String getDataByUUIDAndCapability(String uuid, String capability) {
		org.springframework.web.client.RestTemplate rest = new org.springframework.web.client.RestTemplate();
		
		//Building the body of message
		JSONArray capabilities = new JSONArray();
		capabilities.add(capability);
		JSONObject body = new JSONObject();
		body.put("capabilities",capabilities);
		
		//Building message request
		//Set the content media type
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		
		//Set the request body
		HttpEntity<String> request = new HttpEntity<String>(body.toString(), header);
		
		//Make request
		String response = rest.postForObject("http://cidadesinteligentes.lsdi.ufma.br/eq2/collector/resources/" + uuid + "/data", request, String.class);
		JSONParser parser = new JSONParser();
		JSONArray array = null;
		JSONObject obj = null;
		try {
			obj = (JSONObject)parser.parse(response);
			array = (JSONArray)obj.get("resources");
			obj = (JSONObject)array.get(0);
			obj = (JSONObject)obj.get("capabilities");
			array = (JSONArray)obj.get("parking-status");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array.toString();
	}
}
