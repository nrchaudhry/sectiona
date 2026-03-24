package com.cwiztech.sectiona.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
import com.cwiztech.services.ServiceCall;
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
		JSONObject apiRequest = AccessToken.checkToken("GET", "/transportroute/all", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		List<TransportRoute> transportroutes = transportrouterepository.findAll();

		return new ResponseEntity(getAPIResponse(transportroutes, null, null, null, null, apiRequest, true), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	private ResponseEntity getOne(@RequestHeader("Authorization") String headToken, @PathVariable Long id) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("GET", "/transportroute/"+id, null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		TransportRoute transportroute = transportrouterepository.findOne(id);

		return new ResponseEntity(getAPIResponse(null, transportroute, null, null, null, apiRequest, true), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST)
	private ResponseEntity insert(@RequestHeader("Authorization") String headToken, @RequestBody String data) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("POST", "/transportroute", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		JSONObject jsonTransportRoute = new JSONObject(data);

		return insertupdateAll(null, jsonTransportRoute, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	private ResponseEntity update(@RequestHeader("Authorization") String headToken, @PathVariable Long id, @RequestBody String data) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("PUT", "/transportroute/"+id, null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		JSONObject jsonTransportRoute = new JSONObject(data);
		jsonTransportRoute.put("transportroute_ID", id);

		return insertupdateAll(null, jsonTransportRoute, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.PUT)
	private ResponseEntity insertupdate(@RequestHeader("Authorization") String headToken, @RequestBody String data) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("PUT", "/transportroute", null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		JSONArray jsonTransportRoutes = new JSONArray(data);

		return insertupdateAll(jsonTransportRoutes, null, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ResponseEntity insertupdateAll(JSONArray jsonTransportRoutes, JSONObject jsonTransportRoute, JSONObject apiRequest) throws JSONException, JsonProcessingException, ParseException, InterruptedException, ExecutionException {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		List<TransportRoute> transportroutes = new ArrayList<TransportRoute>();

		if (jsonTransportRoute != null) {
			jsonTransportRoutes = new JSONArray();
			jsonTransportRoutes.put(jsonTransportRoute);
		}

		for (int i = 0; i <jsonTransportRoutes.length(); i++) {
			JSONObject jsonObj = jsonTransportRoutes.getJSONObject(i);
			TransportRoute transportroute = new TransportRoute();
			long id = 0;

			if (jsonObj.has("transportroute_ID")) {
				id = jsonObj.getLong("transportroute_ID");
				if (id != 0) {
					transportroute = transportrouterepository.findOne(id);

					if (transportroute == null)
						return new ResponseEntity(getAPIResponse(null, null, null, null, "Invalid Transport Route Data!", apiRequest, true), HttpStatus.OK);
				}
			}

			if (id == 0) {
				if (!jsonTransportRoute.has("transportroute_CODE") || jsonTransportRoute.isNull("transportroute_CODE"))
					return new ResponseEntity(getAPIResponse(null, null, null, null, "transportroute_CODE is missing!", apiRequest, true), HttpStatus.OK);

				if (!jsonTransportRoute.has("transportroute_DESC") || jsonTransportRoute.isNull("transportroute_DESC"))
					return new ResponseEntity(getAPIResponse(null, null, null, null, "transportroute_DESC is missing!", apiRequest, true), HttpStatus.OK);

				if (!jsonTransportRoute.has("routetype_ID") || jsonTransportRoute.isNull("routetype_ID"))
					return new ResponseEntity(getAPIResponse(null, null, null, null, "routetype_ID is missing!", apiRequest, true), HttpStatus.OK);
			}

			if (jsonTransportRoute.has("transportroute_CODE") && !jsonTransportRoute.isNull("transportroute_CODE"))
				transportroute.setTRANSPORTROUTE_CODE(jsonTransportRoute.getString("transportroute_CODE"));

			if (jsonTransportRoute.has("transportroute_DESC") && !jsonTransportRoute.isNull("transportroute_DESC"))
				transportroute.setTRANSPORTROUTE_DESC(jsonTransportRoute.getString("transportroute_DESC"));

			if (jsonTransportRoute.has("routetype_ID") && !jsonTransportRoute.isNull("routetype_ID"))
				transportroute.setROUTETYPE_ID(jsonTransportRoute.getLong("routetype_ID"));

			if (jsonTransportRoute.has("colour_ID") && !jsonTransportRoute.isNull("colour_ID"))
				transportroute.setCOLOUR_ID(jsonTransportRoute.getLong("colour_ID"));

			if (id == 0) 
				transportroute.setISACTIVE("Y");
			else if (jsonTransportRoute.has("isactive") && !jsonTransportRoute.isNull("isactive"))
				transportroute.setISACTIVE(jsonTransportRoute.getString("isactive"));

			transportroute.setMODIFIED_BY(apiRequest.getLong("request_ID"));
			transportroute.setMODIFIED_WORKSTATION(apiRequest.getString("log_WORKSTATION"));
			transportroute.setMODIFIED_WHEN(dateFormat1.format(date));

			transportroutes.add(transportroute);
		}

		for (int i=0; i<transportroutes.size(); i++) {
			TransportRoute transportroute = transportroutes.get(i);
			transportroute = transportrouterepository.saveAndFlush(transportroute);
			transportroutes.get(i).setTRANSPORTROUTE_ID(transportroute.getTRANSPORTROUTE_ID());
		}

		ResponseEntity responseentity;

		if (jsonTransportRoute != null)
			responseentity = new ResponseEntity(getAPIResponse(null, transportroutes.get(0), null, null, null, apiRequest, true), HttpStatus.OK);
		else 
			responseentity = new ResponseEntity(getAPIResponse(transportroutes, null, null, null, null, apiRequest, true), HttpStatus.OK);

		return responseentity;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	private ResponseEntity delete(@RequestHeader("Authorization") String headToken, @PathVariable Long id) throws JsonProcessingException, JSONException, ParseException, InterruptedException, ExecutionException {
		JSONObject apiRequest = AccessToken.checkToken("DELETE", "/transportroute/"+id, null, null, headToken);
		if (apiRequest.has("error")) return new ResponseEntity(apiRequest.toString(), HttpStatus.OK);

		TransportRoute transportroute = transportrouterepository.findOne(id);
		transportrouterepository.delete(transportroute);

		return new ResponseEntity(getAPIResponse(null, transportroute, null, null, null, apiRequest, true), HttpStatus.OK);
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
					transportroutes = new ArrayList<TransportRoute>();
					transportroutes.add(transportroute);
				}

				if (transportroutes.size()>0) {
					List<Integer> lookupList = new ArrayList<Integer>();

					for (int i=0; i<transportroutes.size(); i++) {
						if (transportroutes.get(i).getROUTETYPE_ID() != null) {
							lookupList.add(Integer.parseInt(transportroutes.get(i).getROUTETYPE_ID().toString()));				
						}
						if (transportroutes.get(i).getCOLOUR_ID() != null) {
							lookupList.add(Integer.parseInt(transportroutes.get(i).getROUTETYPE_ID().toString()));				
						}
					}

					CompletableFuture<JSONArray> lookupFuture = CompletableFuture.supplyAsync(() -> {
						if (lookupList.size() <= 0) return new JSONArray();

						try {
							return new JSONArray(ServiceCall.POST("lookup/ids", "{lookups: "+lookupList+"}", apiRequest.getString("access_TOKEN"), true));
						} catch (JSONException | JsonProcessingException | ParseException e) {
							e.printStackTrace();
							return new JSONArray();
						}
					});

					// Wait until all futures complete
					CompletableFuture<Void> allDone =
							CompletableFuture.allOf(lookupFuture);

					// Block until all are done
					allDone.join();

					JSONArray lookups = lookupFuture.get();

					for (int i=0; i<transportroutes.size(); i++) {
						for (int j=0; j<lookups.length(); j++) {
							if (transportroutes.get(i).getROUTETYPE_ID() != null && transportroutes.get(i).getROUTETYPE_ID() == lookups.getJSONObject(j).getLong("id")) {
								transportroutes.get(i).setROUTETYPE_DETAIL(lookups.getJSONObject(j).toString());				
							}
							if (transportroutes.get(i).getCOLOUR_ID() != null && transportroutes.get(i).getCOLOUR_ID() == lookups.getJSONObject(j).getLong("id")) {
								transportroutes.get(i).setCOLOUR_DETAIL(lookups.getJSONObject(j).toString());				
							}
						}
					}
				}
				if (transportroute != null) {
					rtnAPIResponse = mapper.writeValueAsString(transportroute);
				} else {
					rtnAPIResponse = mapper.writeValueAsString(transportroutes);
				}
			} else if (transportroute != null) {
				rtnAPIResponse = mapper.writeValueAsString(transportroute);

			} else if (transportroutes != null) {
				rtnAPIResponse = mapper.writeValueAsString(transportroutes);

			} else if (jsontransportroute != null) {
				rtnAPIResponse = jsontransportroute.toString();

			} else if (jsontransportroutes != null) {
				rtnAPIResponse = jsontransportroutes.toString();

			}

			apiRequestLog.apiRequestSaveLog(apiRequest, rtnAPIResponse, "Success");
		}

		return rtnAPIResponse;
	}
}
