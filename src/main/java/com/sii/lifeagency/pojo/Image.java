package com.sii.lifeagency.pojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.util.ImageOperation;

//@JsonInclude(Include.NON_NULL)
@Document(collection = "images")
public class Image {

	private static final Logger logger = LoggerFactory.getLogger(Image.class);

	@Id
	String id;
	String url;
	@Transient
	String base64;

	public String getId() {
		return id;
	}

	public Image setId(String id) {
		this.id = id;
		return this;
	}

	public String getBase64() {
		return base64;
	}

	public Image setBase64(String base64) {
		this.base64 = base64;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Image setUrl(String url) {
		this.url = url;
		return this;
	}

	protected File getBinaryFile() {
		File directory = new File(GlobalConfiguration.getInstance().getPathImage());
		if (!directory.exists()) {
			directory.mkdir();
		}
		File image = new File(directory.getPath() + File.separator + this.getId());
		return image;
	}

	public void saveImageFromBase64() throws Exception {
		if (this.getBase64() == null) {
			throw new Exception("no base64 found");
		}
		File binaryImageFile = ImageOperation.getBinaryFile(id);
		byte binaryImage[] = Base64.getDecoder().decode(this.getBase64());
		try (FileOutputStream outputStream = new FileOutputStream(binaryImageFile);) {
			outputStream.write(binaryImage);
			logger.info("Image saved in: " + binaryImageFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Impossible to convert base64", e);
		}
	}
	
	public String toString() {
		return "Image [id=" + id + ", base64=" + base64 + ", binaryFile=" + getBinaryFile().getPath() + "]";
	}

}
