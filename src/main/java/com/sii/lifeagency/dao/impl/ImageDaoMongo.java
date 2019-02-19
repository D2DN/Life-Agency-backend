package com.sii.lifeagency.dao.impl;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;
import com.sii.lifeagency.dao.ImageDao;
import com.sii.lifeagency.pojo.Image;
import com.sii.lifeagency.util.MongoUtil;

public class ImageDaoMongo implements ImageDao {

	public List<Image> getAllImages() {
		return MongoUtil.getMongoOp().findAll(Image.class);
	}

	public Image getImage(String id) {
		return MongoUtil.getMongoOp().findOne(new Query(Criteria.where("id").is(id)), Image.class);
	}

	public void upsertImage(Image img) {
		MongoUtil.getMongoOp().save(img);
	}

	public boolean deleteImage(String id) {
		Query query1 = new Query();
		query1.addCriteria(Criteria.where("id").is(id));
		WriteResult writeResult=MongoUtil.getMongoOp().remove(query1, Image.class);
		if (writeResult.getN() > 0) {
			return true;
		}
		return false;
	}
	
}
