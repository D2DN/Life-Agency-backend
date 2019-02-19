package com.sii.lifeagency.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.EventDao;
import com.sii.lifeagency.dao.EventTypeDao;
import com.sii.lifeagency.dao.ImageDao;
import com.sii.lifeagency.dao.factory.EventDaoFactory;
import com.sii.lifeagency.dao.factory.EventTypeDaoFactory;
import com.sii.lifeagency.dao.factory.ImageDaoFactory;
import com.sii.lifeagency.pojo.Event;
import com.sii.lifeagency.pojo.EventType;
import com.sii.lifeagency.pojo.Image;
import com.sii.lifeagency.rest.ResponseRest;
import com.sii.lifeagency.rest.RestError;
import com.sii.lifeagency.util.ImageOperation;
import com.sii.lifeagency.util.PushNotification;

@RestController
public class EventController {

	private static final Logger logger = LoggerFactory.getLogger(EventController.class);

	@RequestMapping(value = "/eventType", method = RequestMethod.POST)
	public ResponseEntity<ResponseRest> insertEventType(Locale locale, @RequestBody EventType eventType, Model model,
			HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		RestError error = new RestError();

		EventTypeDao eventTypeDao = EventTypeDaoFactory.getEventTypeDao();

		if (eventType.getId() == null) {
			eventType.setId(UUID.randomUUID().toString());
		}
		if (eventTypeDao.getEventType(eventType.getId()) != null) {
			error.setCode(RestError.Code.DUPLICATE)
					.setMessage("Event Type with the id " + eventType.getId() + " already exists");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.CONFLICT);
		}

		try {
			ImageOperation.saveImage(eventType.getBanner(), request);
		} catch (IllegalArgumentException e) {
			error.setCode(RestError.Code.DUPLICATE)
					.setMessage("Image with the id " + eventType.getBanner().getId() + " already exists");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			return ImageOperation.getDecriptBase64Error();
		}

		eventType.getBanner().setBase64(null);
		eventTypeDao.upsertEventType(eventType);

		response.setData(eventType);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/eventType", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> getEventTypes(Locale locale, Model model, HttpServletRequest request) {

		ResponseRest response = new ResponseRest();
		EventTypeDao eventTypeDao = EventTypeDaoFactory.getEventTypeDao();
		response.setData(eventTypeDao.getAllEventType());
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/eventType/{idEventType}", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> getEventType(Locale locale, @PathVariable(value = "idEventType") String id,
			Model model, HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		EventTypeDao eventTypeDao = EventTypeDaoFactory.getEventTypeDao();

		response.setData(eventTypeDao.getEventType(id));
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/eventType/{idEventType}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseRest> delEventType(Locale locale, @PathVariable(value = "idEventType") String id,
			Model model, HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		EventTypeDao eventTypeDao = EventTypeDaoFactory.getEventTypeDao();
		if (eventTypeDao.deleteEventType(id)) {
			response.setData("sucess");
			return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
		}
		RestError error = new RestError();
		error.setCode(RestError.Code.NOTFOUND).setMessage("Event Type not found");
		response.setError(error);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/event", method = RequestMethod.POST)
	public ResponseEntity<ResponseRest> insertEvent(Locale locale, @RequestBody Event event, Model model,
			HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		RestError error = new RestError();

		EventDao eventDao = EventDaoFactory.getEventDao();
		EventTypeDao eventTypeDao = EventTypeDaoFactory.getEventTypeDao();

		if (event.getId() == null) {
			event.setId(UUID.randomUUID().toString());
		}
		if (eventDao.getEvent(event.getId()) != null) {
			error.setCode(RestError.Code.DUPLICATE)
					.setMessage("Event with the id " + event.getId() + " already exists");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.CONFLICT);
		}

		if (eventTypeDao.getEventType(event.getEventType().getId()) == null) {
			error.setCode(RestError.Code.NOTFOUND)
					.setMessage("Event Type with the id " + event.getEventType().getId() + " not found");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_FOUND);
		}

		Image img = event.getImgEvent();
		if (img != null && img.getBase64() != null) {
			try {
				ImageOperation.saveImage(img, request);
			} catch (IllegalArgumentException e) {
				error.setCode(RestError.Code.DUPLICATE)
						.setMessage("Image with the id " + img.getId() + " already exists");
				response.setError(error);
				return new ResponseEntity<ResponseRest>(response, HttpStatus.CONFLICT);
			} catch (Exception e) {
				return ImageOperation.getDecriptBase64Error();
			}
			img.setBase64(null);
		}

		EventType eventType = eventTypeDao.getEventType(event.getEventType().getId());
		event.setEventType(eventType);

		eventDao.upsertEvent(event);

		PushNotification.sendAsyncPushNotification(event.getTitle(), event.getDescription());

		response.setData(event);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/event", method = RequestMethod.PUT)
	public ResponseEntity<ResponseRest> updateEvent(Locale locale, @RequestBody Event event, Model model,
			HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		RestError error = new RestError();

		EventDao eventDao = EventDaoFactory.getEventDao();
		EventTypeDao eventTypeDao = EventTypeDaoFactory.getEventTypeDao();
		ImageDao imageDao = ImageDaoFactory.getImageDao();

		if (event.getId() == null) {
			error.setCode(RestError.Code.INVALID).setMessage("Event's id is mandatory");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		if (eventDao.getEvent(event.getId()) == null) {
			error.setCode(RestError.Code.NOTFOUND).setMessage("Event's id not found");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_ACCEPTABLE);
		}

		if (eventTypeDao.getEventType(event.getEventType().getId()) == null) {
			error.setCode(RestError.Code.NOTFOUND)
					.setMessage("Event Type with the id " + event.getEventType().getId() + " not found");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_FOUND);
		}

		Image img = event.getImgEvent();

		if (img != null && (img.getId() != null || img.getBase64() != null)) {
			if (img.getBase64() != null) {
				try {
					ImageOperation.upsertImage(img, request);
				} catch (Exception e) {
					return ImageOperation.getDecriptBase64Error();
				}
			} else {
				if (imageDao.getImage(img.getId()) == null) {
					error.setCode(RestError.Code.NOTFOUND)
							.setMessage("Image with the id " + img.getId() + " not found");
					response.setError(error);
					return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_FOUND);
				}
				event.setImgEvent(imageDao.getImage(img.getId()));
			}
			img.setBase64("");
		}
		EventType eventType = eventTypeDao.getEventType(event.getEventType().getId());
		event.setEventType(eventType);

		eventDao.upsertEvent(event);

		response.setData(event);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/event", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> getAllEvents(Locale locale, Model model, HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();

		EventDao eventDao = EventDaoFactory.getEventDao();
		List<Event> lstEvents = eventDao.getAllEvents();

		Collections.sort(lstEvents, new Comparator<Event>() {
			@Override
			public int compare(final Event e1, final Event e2) {
				return e2.getDate().compareTo(e1.getDate());
			}
		});

		response.setData(lstEvents);

		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/event/{idEvent}", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> getEvent(Locale locale, @PathVariable(value = "idEvent") String id, Model model,
			HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		EventDao eventDao = EventDaoFactory.getEventDao();

		response.setData(eventDao.getEvent(id));
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/event/{idEvent}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseRest> delEvent(Locale locale, @PathVariable(value = "idEvent") String id, Model model,
			HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		EventDao eventDao = EventDaoFactory.getEventDao();

		if (eventDao.deleteEvent(id)) {
			response.setData("sucess");
			return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
		}
		RestError error = new RestError();
		error.setCode(RestError.Code.NOTFOUND).setMessage("Event not found");
		response.setError(error);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/properties", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> getProperties(Locale locale, Model model, HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();

		final Properties properties = new Properties();
		try (final InputStream stream = GlobalConfiguration.class.getClassLoader()
				.getResourceAsStream("config.properties")) {
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		properties.put("DirecotyImage", new File(GlobalConfiguration.getInstance().getPathImage()).getAbsolutePath());

		response.setData(properties);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/eventType", method = RequestMethod.PUT)
	public ResponseEntity<ResponseRest> updateEventType(Locale locale, @RequestBody EventType eventType, Model model,
			HttpServletRequest request) {
		
		ResponseRest response = new ResponseRest();
		RestError error = new RestError();

		EventTypeDao eventTypeDao = EventTypeDaoFactory.getEventTypeDao();
		ImageDao imageDao = ImageDaoFactory.getImageDao();

		if (eventType.getId() == null) {
			error.setCode(RestError.Code.INVALID).setMessage("EventType's id is mandatory");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_ACCEPTABLE);
		}

		if (eventTypeDao.getEventType(eventType.getId()) == null) {
			error.setCode(RestError.Code.NOTFOUND)
					.setMessage("Event Type with the id " + eventType.getId() + " not found");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_FOUND);
		}

		Image img = eventType.getBanner();

		if (img.getId() == null && img.getBase64() == null) {
			error.setCode(RestError.Code.INVALID).setMessage("Image is mandatory");
			response.setError(error);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		if (img.getBase64() != null) {
			try {
				ImageOperation.upsertImage(img, request);
			} catch (Exception e) {
				return ImageOperation.getDecriptBase64Error();
			}
		} else {
			eventType.setBanner(imageDao.getImage(img.getId()));
		}
		eventType.getBanner().setBase64(null);
		eventTypeDao.upsertEventType(eventType);
		response.setData(eventType);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseRest> exceptionHandler(Exception ex) {
		return RestError.getExceptionRest(ex);
	}

}
