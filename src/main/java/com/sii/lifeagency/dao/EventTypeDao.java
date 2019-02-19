package com.sii.lifeagency.dao;

import java.util.List;

import com.sii.lifeagency.pojo.EventType;

public interface EventTypeDao {
	public List<EventType> getAllEventType();
	public EventType getEventType(String id);
	public void upsertEventType(EventType eventType);
	public boolean deleteEventType(String eventType);
}
