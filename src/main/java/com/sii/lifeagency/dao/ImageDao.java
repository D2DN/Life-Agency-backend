package com.sii.lifeagency.dao;

import java.util.List;

import com.sii.lifeagency.pojo.Image;

public interface ImageDao {
	public List<Image> getAllImages();
	public Image getImage(String id);
	public void upsertImage(Image img);
	public boolean deleteImage(String img);
}
