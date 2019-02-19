package com.sii.lifeagency.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.ImageDao;
import com.sii.lifeagency.dao.factory.ImageDaoFactory;
import com.sii.lifeagency.pojo.Image;
import com.sii.lifeagency.rest.ResponseRest;
import com.sii.lifeagency.rest.RestError;

public class ImageOperation {

	public static File getBinaryFile(String id) {
		File directory = new File(GlobalConfiguration.getInstance().getPathImage());
		if (!directory.exists()) {
			directory.mkdir();
		}
		File image = new File(directory.getPath() + File.separator + id);

		return image;
	}

	public static Image saveImage(Image img, HttpServletRequest request) throws IllegalArgumentException, Exception {

		ImageDao imgDao = ImageDaoFactory.getImageDao();
		if (img.getId() == null) {
			img.setId(UUID.randomUUID().toString());
		}
		if (imgDao.getImage(img.getId()) != null) {
			throw new IllegalArgumentException("Register already exists");
		}
		img.setUrl(request.getContextPath() + "/image/" + img.getId());
		img.saveImageFromBase64();
		imgDao.upsertImage(img);
		return img;
	}
	
	public static Image upsertImage(Image img, HttpServletRequest request) throws IllegalArgumentException, Exception {

		ImageDao imgDao = ImageDaoFactory.getImageDao();
		if (img.getId() == null) {
			img.setId(UUID.randomUUID().toString());
		} else {
			if (imgDao.deleteImage(img.getId())) {
				deleteImageFile(img.getId());
			}
		}
		img.setUrl(request.getContextPath() + "/image/" + img.getId());
		img.saveImageFromBase64();
		imgDao.upsertImage(img);
		return img;
	}


	public static void deleteImageFile(String id) {
		File binaryImageFile = ImageOperation.getBinaryFile(id);
		binaryImageFile.delete();
	}

	public static ResponseEntity<ResponseRest> getDecriptBase64Error() {
		ResponseRest response = new ResponseRest();
		RestError error = new RestError();
		error.setCode(RestError.Code.DECRIPTIONERROR).setMessage("Impossible to decript image base64");
		response.setError(error);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_ACCEPTABLE);
	}

}
