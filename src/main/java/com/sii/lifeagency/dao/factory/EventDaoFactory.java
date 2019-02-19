package com.sii.lifeagency.dao.factory;

import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.EventDao;
import com.sii.lifeagency.dao.impl.EventDaoMongo;

public class EventDaoFactory {
    public static EventDao getEventDao() { 
        if (GlobalConfiguration.getInstance().getDatabaseType().equalsIgnoreCase("mongodb")) {
            return new EventDaoMongo();
        } else {
            return null;
        }
    }
}
