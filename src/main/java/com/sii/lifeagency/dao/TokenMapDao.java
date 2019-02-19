package com.sii.lifeagency.dao;

import com.sii.lifeagency.pojo.TokenMapDevices;

public interface TokenMapDao {
	public TokenMapDevices getTokenMap();
	public void upsertTokenMap(TokenMapDevices tokenMap);
	public boolean deleteTokenMap(String id);
}
