package com.cwiztech.sectiona.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cwiztech.log.apiRequestLog;
import com.cwiztech.sectiona.model.TransportRoute;
import com.cwiztech.sectiona.repository.transportRouteRepository;
import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/transportroute")
public class transportRouteController {
	
	@Autowired 
	private transportRouteRepository transportrouterepository;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	private ResponseEntity get(@RequestHeader("Authorization") String headToken) throws JSONException, JsonProcessingException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("GET", "/transportroute", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);
		
		List<TransportRoute> transportroutes = transportrouterepository.findActive();
		
		return new ResponseEntity(getAPIResponse(transportroutes, null, null, null, null, apiRequest, true), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	private ResponseEntity getAll(@RequestHeader("Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("GET", "/transportroute", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);
		
		List<TransportRoute> transportroutes = transportrouterepository.findAll();
		
		return new ResponseEntity(getAPIResponse(transportroutes, null, null, null, null, apiRequest, true), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	private ResponseEntity getOne(@RequestHeader("Authorization") String headToken, @PathVariable Long id) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("GET", "/transportroute", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);
		
		TransportRoute transportroute = transportrouterepository.findOne(id);
		
		return new ResponseEntity(getAPIResponse(null, transportroute, null, null, null, apiRequest, true), HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST)
	private ResponseEntity insert(@RequestHeader("Authorization") String headToken, @RequestBody String data) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("GET", "/transportroute", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		TransportRoute transportroute = new TransportRoute();
		JSONObject jsonTransportRoute = new JSONObject(data);
		
		if (!jsonTransportRoute.has("transportroute_CODE") || jsonTransportRoute.isNull("transportroute_CODE"))
			return new ResponseEntity(getAPIResponse(null, null, null, null, "transportroute_CODE is missing!", apiRequest, true), HttpStatus.OK);

		if (!jsonTransportRoute.has("transportroute_DESC") || jsonTransportRoute.isNull("transportroute_DESC"))
			return new ResponseEntity(getAPIResponse(null, null, null, null, "transportroute_DESC is missing!", apiRequest, true), HttpStatus.OK);

		if (!jsonTransportRoute.has("routetype_ID") || jsonTransportRoute.isNull("routetype_ID"))
			return new ResponseEntity(getAPIResponse(null, null, null, null, "routetype_ID is missing!", apiRequest, true), HttpStatus.OK);

		if (jsonTransportRoute.has("transportroute_CODE") && !jsonTransportRoute.isNull("transportroute_CODE"))
			transportroute.setTRANSPORTROUTE_CODE(jsonTransportRoute.getString("transportroute_CODE"));
		
		if (jsonTransportRoute.has("transportroute_DESC") && !jsonTransportRoute.isNull("transportroute_DESC"))
			transportroute.setTRANSPORTROUTE_DESC(jsonTransportRoute.getString("transportroute_DESC"));
		
		if (jsonTransportRoute.has("routetype_ID") && !jsonTransportRoute.isNull("routetype_ID"))
			transportroute.setROUTETYPE_ID(jsonTransportRoute.getLong("routetype_ID"));
		
		if (jsonTransportRoute.has("color_ID") && !jsonTransportRoute.isNull("color_ID"))
			transportroute.setCOLOUR_ID(jsonTransportRoute.getLong("color_ID"));
		
		return new ResponseEntity(getAPIResponse(null, transportroute, null, null, null, apiRequest, true), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	private ResponseEntity update(@RequestHeader("Authorization") String headToken, @PathVariable Long id, @RequestBody TransportRoute data) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("GET", "/transportroute", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		TransportRoute transportroute = transportrouterepository.saveAndFlush(data);
		
		return new ResponseEntity(getAPIResponse(null, transportroute, null, null, null, apiRequest, true), HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.PUT)
	private ResponseEntity insertupdate(@RequestHeader("Authorization") String headToken, @RequestBody List<TransportRoute> data) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("GET", "/transportroute", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		List<TransportRoute> transportroutes = new ArrayList<TransportRoute>();
		
		for (int i = 0; i <data.size(); i++) {
			transportroutes.add(transportrouterepository.saveAndFlush(data.get(i)));
		}
		return new ResponseEntity(getAPIResponse(transportroutes, null, null, null, null, apiRequest, true), HttpStatus.OK);
	}
	
	private String getAPIResponse(
			List<TransportRoute> transportroutes, 
			TransportRoute transportroute, 
			JSONArray jsontransportroutes, 
			JSONObject jsontransportroute,
			String message, 
			JSONObject apiRequest, boolean isWithDetail) throws JSONException, JsonProcessingException, ParseException, InterruptedException, ExecutionException {
		ObjectMapper mapper = new ObjectMapper();
		String rtnAPIResponse="Invalid Resonse";
	
	    if (message != null) {
	        rtnAPIResponse = apiRequestLog.apiRequestErrorLog(apiRequest, "TransportRoute", message).toString();
	    } else {
	    	if (isWithDetail == true && (transportroutes != null || transportroute != null)) {
	    		if (transportroute != null) {
	    			rtnAPIResponse = mapper.writeValueAsString(transportroute);
	    		} else {
	    			rtnAPIResponse = mapper.writeValueAsString(transportroutes);
	    		}
	    	}
	    }
		return rtnAPIResponse;
	}
}
