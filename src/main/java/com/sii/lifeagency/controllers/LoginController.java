package com.sii.lifeagency.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sii.lifeagency.login.UserSession;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sii.lifeagency.dao.TokenMapDao;
import com.sii.lifeagency.dao.factory.TokenMapDaoFactory;
import com.sii.lifeagency.pojo.TokenMapDevices;
import com.sii.lifeagency.rest.ResponseRest;
import com.sii.lifeagency.rest.RestError;
import com.sii.lifeagency.util.PushNotification;

/**
 * Handles requests for the application home page.
 */
@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseRest> login(HttpServletRequest request, @RequestBody UserSession userSession) {
		
		ResponseRest response = new ResponseRest();
		try {
			HttpSession session = request.getSession();
			session.setAttribute("user", userSession);			
			
			return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
		} catch (Exception e) {
			RestError error = new RestError().setCode(RestError.Code.UNAUTHORIZED).setMessage(e.getMessage());
			response.setError(error);
			logger.error("Login error: ", e);
			return new ResponseEntity<ResponseRest>(response, HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> logout(HttpServletRequest request) {
		request.getSession().invalidate();
		ResponseRest response = new ResponseRest();
		response.setData("success");
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> test(HttpServletRequest request) {
		ResponseRest response = new ResponseRest();
		response.setData("Welcome");
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<ResponseRest> addToken(@RequestBody JSONObject token,HttpServletRequest request) {		
		TokenMapDao tokenMapDao = TokenMapDaoFactory.getMapTokenDao();
		TokenMapDevices tokenMapDevices = tokenMapDao.getTokenMap();
		if (tokenMapDevices == null) {
			tokenMapDevices = new TokenMapDevices();
		}
		tokenMapDevices.addToken(token.get("token").toString());
		tokenMapDao.upsertTokenMap(tokenMapDevices);
		
		ResponseRest response = new ResponseRest();
		response.setData("success");
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);		
	}

	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public ResponseEntity<ResponseRest> getToken(HttpServletRequest request) {
		TokenMapDao tokenMapDao = TokenMapDaoFactory.getMapTokenDao();
		TokenMapDevices tokenMapDevices = tokenMapDao.getTokenMap();
		ResponseRest response = new ResponseRest();
		response.setData(tokenMapDevices);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);		
	}

	
	@RequestMapping(value = "/token/{token}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseRest> delToken( @PathVariable(value = "token") String token,HttpServletRequest request) {
		ResponseRest response = new ResponseRest();
		response.setData("success");
		
		TokenMapDao tokenMapDao = TokenMapDaoFactory.getMapTokenDao();
		TokenMapDevices tokenMapDevices = tokenMapDao.getTokenMap();
		if (tokenMapDevices == null) {
			return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);		
		}
		tokenMapDevices.removeToken(token);
		tokenMapDao.upsertTokenMap(tokenMapDevices);
		
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);		
	}
	
	@RequestMapping(value = "/token/invalid", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseRest> delAllInvalidTokens(String token,HttpServletRequest request) {
		List<String> listInvalidTokens = PushNotification.delAllInvalidTokens();
		ResponseRest response = new ResponseRest();
		response.setData(listInvalidTokens);		
		
		return new ResponseEntity<ResponseRest>(response, HttpStatus.OK);		
	}

	public static ResponseEntity<ResponseRest> getNotAuthenticatedRestError() {
		ResponseRest response = new ResponseRest();
		RestError error = new RestError().setCode(RestError.Code.UNKNOWN).setMessage("User not authenticated");
		response.setError(error);
		return new ResponseEntity<ResponseRest>(response, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseRest> exceptionHandler(Exception ex) {
		return RestError.getExceptionRest(ex);
	}

}
