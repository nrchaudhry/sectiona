package com.cwiztech.sectiona.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cwiztech.sectiona.model.TransportRoute;
import com.cwiztech.sectiona.repository.transportRouteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/transportroute")
public class transportRouteController {
	
	@Autowired 
	private transportRouteRepository transportrouterepository;
	
	@RequestMapping(method = RequestMethod.GET)
	private List<TransportRoute> get() {
		List<TransportRoute> transportroutes = transportrouterepository.findActive();
		return transportroutes;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	private List<TransportRoute> getAll() {
		List<TransportRoute> transportroutes = transportrouterepository.findAll();
		return transportroutes;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	private TransportRoute getOne(@PathVariable Long id) {
		TransportRoute transportroute = transportrouterepository.findOne(id);
		return transportroute;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	private String insert(@RequestBody String data) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String rtnAPIResponse = "Invalid Response";
		JSONObject jsonTransportRoute = new JSONObject(data);
		
		if (!jsonTransportRoute.has("transportroute_CODE") || jsonTransportRoute.isNull("transportroute_CODE"))
		{
			return "transportroute_CODE is missing!";
		}

		if (!jsonTransportRoute.has("transportroute_DESC") || jsonTransportRoute.isNull("transportroute_DESC"))
		{
			return "transportroute_DESC is missing!";
		}

		if (!jsonTransportRoute.has("routetype_ID") || jsonTransportRoute.isNull("routetype_ID"))
		{
			return "routetype_ID is missing!";
		}

		TransportRoute transportroute = new TransportRoute();
		transportroute.setTRANSPORTROUTE_CODE(jsonTransportRoute.getString("transportroute_CODE"));
		transportroute.setTRANSPORTROUTE_DESC(jsonTransportRoute.getString("transportroute_DESC"));
		transportroute.setROUTETYPE_ID(jsonTransportRoute.getLong("routetype_ID"));
		
		if (jsonTransportRoute.has("color_ID") && !jsonTransportRoute.isNull("color_ID"))
			transportroute.setCOLOUR_ID(jsonTransportRoute.getLong("color_ID"));
		
		transportroute = transportrouterepository.saveAndFlush(transportroute);
		rtnAPIResponse = mapper.writeValueAsString(transportroute);
		return rtnAPIResponse;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	private TransportRoute update(@PathVariable Long id, @RequestBody TransportRoute data) {
		TransportRoute transportroute = transportrouterepository.saveAndFlush(data);
		return transportroute;
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	private List<TransportRoute> insertupdate(@RequestBody List<TransportRoute> data) {
		List<TransportRoute> transportroutes = new ArrayList<TransportRoute>();
		
		for (int i = 0; i <data.size(); i++) {
			transportroutes.add(transportrouterepository.saveAndFlush(data.get(i)));
		}
		return transportroutes;
	}
	
}
