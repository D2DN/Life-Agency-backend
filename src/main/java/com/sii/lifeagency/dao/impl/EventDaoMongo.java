package com.sii.lifeagency.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;
import com.sii.lifeagency.dao.EventDao;
import com.sii.lifeagency.pojo.Event;
import com.sii.lifeagency.pojo.EventType;
import com.sii.lifeagency.pojo.Image;
import com.sii.lifeagency.util.MongoUtil;

public class EventDaoMongo implements EventDao {

	@Override
	public List<Event> getAllEvents() {
		return MongoUtil.getMongoOp().findAll(Event.class);
	}

	@Override
	public Event getEvent(String id) {
		return MongoUtil.getMongoOp().findOne(new Query(Criteria.where("id").is(id)), Event.class);
	}

	@Override
	public void upsertEvent(Event event) {
		MongoUtil.getMongoOp().save(event);
	}

	@Override
	public boolean deleteEvent(String id) {
		Query query1 = new Query();
		query1.addCriteria(Criteria.where("id").is(id));
		WriteResult writeResult=MongoUtil.getMongoOp().remove(query1, Event.class);
		if (writeResult.getN() > 0) {
			return true;
		}
		return false;
	}
}
