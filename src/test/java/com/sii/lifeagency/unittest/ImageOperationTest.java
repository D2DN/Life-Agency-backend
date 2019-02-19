package com.sii.lifeagency.unittest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.factory.ImageDaoFactory;
import com.sii.lifeagency.dao.impl.ImageDaoMongo;
import com.sii.lifeagency.pojo.Image;
import com.sii.lifeagency.util.ImageOperation;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ImageDaoFactory.class)
public class ImageOperationTest {

	static File imageDirectory = new File(GlobalConfiguration.getInstance().getPathImage());
	static String contextPath = "SiiLifeAgency";
	static ImageDaoMongo imgDao = mock(ImageDaoMongo.class);
	static HttpServletRequest request = mock(HttpServletRequest.class);

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FileUtils.deleteDirectory(imageDirectory);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FileUtils.deleteDirectory(imageDirectory);
	}
	
	@Before
	public void setUp() {
		PowerMockito.mockStatic(ImageDaoFactory.class);
		when(ImageDaoFactory.getImageDao()).thenReturn(imgDao);
		when(request.getContextPath()).thenReturn(contextPath);
	}

	@Test
	public void testGetBinaryFile() throws Throwable {
		String imageStoredName = "imageStore" + File.separator + "xyz";
		FileUtils.deleteDirectory(imageDirectory);

		assertEquals("directoryMustNotExist - ", false, imageDirectory.exists());
		assertEquals("binary file - ", imageStoredName, ImageOperation.getBinaryFile("xyz").getPath());
		assertEquals("directoryMustExist - ", true, imageDirectory.exists());
	}

	@Test
	public void testSaveAndDeleteImage() throws IllegalArgumentException, Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		String strBase64 = IOUtils.toString(classLoader.getResourceAsStream("image64"));

		when(imgDao.getImage(anyString())).thenReturn(null);

		Image img = new Image();
		img.setBase64(strBase64);
		Image imgResult = ImageOperation.saveImage(img, request);
		File binaryImageFile = ImageOperation.getBinaryFile(imgResult.getId());

		assertEquals("binary file must be created - ", true, binaryImageFile.exists());
		ImageOperation.deleteImageFile(imgResult.getId());
		assertEquals("binary file must not exist - ", false, binaryImageFile.exists());
	}

	@Test
	public void testSaveConflit() throws IllegalArgumentException, Exception {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Register already exists");

		when(imgDao.getImage(anyString())).thenReturn(new Image().setId("xyz"));
		ImageOperation.saveImage(new Image(), request);
	}

}
