package com.sii.lifeagency.dao;

import java.util.List;

import com.sii.lifeagency.pojo.Event;

public interface EventDao {
	public List<Event> getAllEvents();
	public Event getEvent(String id);
	public void upsertEvent(Event event);
	public boolean deleteEvent(String id);
}
