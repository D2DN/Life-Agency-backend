package com.sii.lifeagency.dao.factory;

import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.EventTypeDao;
import com.sii.lifeagency.dao.impl.EventTypeDaoMongo;

public class EventTypeDaoFactory {
	public static EventTypeDao getEventTypeDao() {
        if (GlobalConfiguration.getInstance().getDatabaseType().equalsIgnoreCase("mongodb")) {
			return new EventTypeDaoMongo();
		} else {
			return null;
		}
	}
}
