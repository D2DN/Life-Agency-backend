package com.sii.lifeagency.pojo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;

import com.sii.lifeagency.configuration.GlobalConfiguration;

public class TokenMapDevices {
	@Id
	public String id;
	private Map<String, TokenDevice> mapTokens;
	
	public TokenMapDevices() {
		this.id = GlobalConfiguration.KEYTOKENMAP;
		mapTokens = new HashMap<String, TokenDevice>();
	}
	
	public void addToken(String token) {
		TokenDevice pushToken = new TokenDevice(token);
		mapTokens.put(token, pushToken);
	}
	
	public void removeToken(String token) {
		mapTokens.remove(token);
	}
	
	public TokenDevice getToken(String token) {
		return mapTokens.get(token);
	}
	
	public Map<String, TokenDevice> getMapTokens() {
		return mapTokens;
	}
	public void setMapTokens(Map<String, TokenDevice> mapTokens) {
		this.mapTokens = mapTokens;
	}
	
	public String getId() {
		return id;
	}
	
}
