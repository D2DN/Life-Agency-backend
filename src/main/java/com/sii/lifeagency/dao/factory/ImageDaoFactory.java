package com.sii.lifeagency.dao.factory;

import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.ImageDao;
import com.sii.lifeagency.dao.impl.ImageDaoMongo;

public class ImageDaoFactory {
    public static ImageDao getImageDao() { 
        if (GlobalConfiguration.getInstance().getDatabaseType().equalsIgnoreCase("mongodb")) {
            return new ImageDaoMongo();
        } else {
            return null;
        }
    }
}
