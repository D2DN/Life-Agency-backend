package com.sii.lifeagency.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;
import com.sii.lifeagency.dao.EventTypeDao;
import com.sii.lifeagency.pojo.EventType;
import com.sii.lifeagency.util.MongoUtil;

public class EventTypeDaoMongo implements EventTypeDao {

	@Override
	public List<EventType> getAllEventType() {
		return MongoUtil.getMongoOp().findAll(EventType.class);
	}

	@Override
	public EventType getEventType(String id) {
		return MongoUtil.getMongoOp().findOne(new Query(Criteria.where("id").is(id)), EventType.class);
	}

	@Override
	public void upsertEventType(EventType event) {	
		MongoUtil.getMongoOp().save(event);
	}

	@Override
	public boolean deleteEventType(String id) {
		Query query1 = new Query();
		query1.addCriteria(Criteria.where("id").is(id));
		WriteResult writeResult=MongoUtil.getMongoOp().remove(query1, EventType.class);
		if (writeResult.getN() > 0) {
			return true;
		}
		return false;
	}

}
