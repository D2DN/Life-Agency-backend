package com.sii.lifeagency.dao.factory;

import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.TokenMapDao;
import com.sii.lifeagency.dao.impl.TokenMapDaoMongo;

public class TokenMapDaoFactory {
    public static TokenMapDao getMapTokenDao() { 
        if (GlobalConfiguration.getInstance().getDatabaseType().equalsIgnoreCase("mongodb")) {
            return new TokenMapDaoMongo();
        } else {
            return null;
        }
    }
}
