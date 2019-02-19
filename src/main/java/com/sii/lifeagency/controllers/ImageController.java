package com.sii.lifeagency.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sii.lifeagency.dao.ImageDao;
import com.sii.lifeagency.dao.factory.ImageDaoFactory;
import com.sii.lifeagency.pojo.Image;
import com.sii.lifeagency.rest.ResponseRest;
import com.sii.lifeagency.rest.RestError;
import com.sii.lifeagency.util.ImageOperation;

@RestController
class ImageController {

	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@RequestMapping(value = "/image", method = RequestMethod.POST)
	public ResponseEntity<ResponseRest> insertImage(Locale locale, @RequestBody Image image, HttpServletRequest request,
			Model model) {
		
		ResponseRest response = new ResponseRest();
		RestError error = new RestError();
		try {
			image = ImageOperation.saveImage(image, request);
		} catch (IllegalArgumentException e) {
			error.setCode(RestError.Code.DUPLICATE)
					.setMessage("Image with the id " + image.getId() + " already exists");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			return ImageOperation.getDecriptBase64Error();
		}
		response.setData(image.setBase64(null));
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> getImages(Locale locale, Model model, HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		ImageDao imgDao = ImageDaoFactory.getImageDao();
		response.setData(imgDao.getAllImages());
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/image/{idImage}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(Locale locale, Model model, @PathVariable(value = "idImage") String id,
			HttpServletRequest request) {
		byte binaryImage[] = null;

		try {
			InputStream is;
			is = new FileInputStream(ImageOperation.getBinaryFile(id));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			logger.error("", e);
			return new ResponseEntity<byte[]>(binaryImage, HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			logger.error("", e);
			return new ResponseEntity<byte[]>(binaryImage, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/image/{idImage}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseRest> delImage(Locale locale, Model model, @PathVariable(value = "idImage") String id,
			HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		ImageDao imageDao = ImageDaoFactory.getImageDao();
		if (imageDao.deleteImage(id)) {
			ImageOperation.deleteImageFile(id);
			response.setData("sucess");
			return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
		}
		RestError error = new RestError();
		error.setCode(RestError.Code.NOTFOUND).setMessage("Image not found");
		response.setError(error);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/image", method = RequestMethod.PUT)
	public ResponseEntity<ResponseRest> updateImage(Locale locale, @RequestBody Image image, HttpServletRequest request,
			Model model) {
		
		ResponseRest response = new ResponseRest();
		RestError error = new RestError();
		ImageDao imageDao = ImageDaoFactory.getImageDao();
		
		if (image.getId() == null || image.getBase64() == null) {
			error.setCode(RestError.Code.INVALID).setMessage("Image's id and base64 are mandatory");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		if (imageDao.getImage(image.getId()) == null) {
			error.setCode(RestError.Code.NOTFOUND).setMessage("Image not found");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_FOUND);
		}
		try {
			ImageOperation.upsertImage(image, request);
		} catch (Exception e) {
			return ImageOperation.getDecriptBase64Error();
		}		
		response.setData(image.setBase64(null));
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseRest> exceptionHandler(Exception ex) {
		return RestError.getExceptionRest(ex);

	}
}
