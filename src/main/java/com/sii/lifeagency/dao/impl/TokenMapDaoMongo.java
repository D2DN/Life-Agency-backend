package com.sii.lifeagency.dao.impl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;
import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.TokenMapDao;
import com.sii.lifeagency.pojo.TokenMapDevices;
import com.sii.lifeagency.util.MongoUtil;

public class TokenMapDaoMongo implements TokenMapDao {

	@Override
	public TokenMapDevices getTokenMap() {
		return MongoUtil.getMongoOp().findOne(new Query(Criteria.where("id").is(GlobalConfiguration.KEYTOKENMAP)),
				TokenMapDevices.class);
	}

	@Override
	public void upsertTokenMap(TokenMapDevices tokenMap) {
		MongoUtil.getMongoOp().save(tokenMap);
	}

	@Override
	public boolean deleteTokenMap(String id) {
		Query query1 = new Query();
		query1.addCriteria(Criteria.where("id").is(GlobalConfiguration.KEYTOKENMAP));
		WriteResult writeResult = MongoUtil.getMongoOp().remove(query1, TokenMapDevices.class);
		if (writeResult.getN() > 0) {
			return true;
		}
		return false;
	}

}
