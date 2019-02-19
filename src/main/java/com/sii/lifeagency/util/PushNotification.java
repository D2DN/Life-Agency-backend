package com.sii.lifeagency.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Notification;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.sii.lifeagency.configuration.GlobalConfiguration;
import com.sii.lifeagency.dao.TokenMapDao;
import com.sii.lifeagency.dao.factory.TokenMapDaoFactory;
import com.sii.lifeagency.pojo.TokenDevice;
import com.sii.lifeagency.pojo.TokenMapDevices;

public class PushNotification extends Thread {

	private String title;
	private String body;
	private List<String> notificationListToken = new ArrayList<String>();
	private TokenMapDevices tokenMapDevices;

	private static final Logger logger = LoggerFactory.getLogger(PushNotification.class);

	public void run() {

		TokenMapDao tokenMapDao = TokenMapDaoFactory.getMapTokenDao();
		tokenMapDevices = tokenMapDao.getTokenMap();
		
		for (String token : tokenMapDevices.getMapTokens().keySet()) {
			notificationListToken.add(token);			
		}
		
		Sender sender = new Sender(GlobalConfiguration.getInstance().getGcmApiKey());

		Notification notification = new Notification.Builder("lifeagency.png")
				.title(this.getTitle())
				.body(this.getBody())
				.build();
		Message msg = new Message.Builder().notification(notification).build();

		try {
			MulticastResult multiResult = sender.send(msg, notificationListToken, GlobalConfiguration.getInstance().getGcmRetries());
			this.updateTokenStatus(multiResult);
		} catch (InvalidRequestException e) {
			logger.error("Invalid Request", e);
		} catch (IOException e) {
			logger.error("IO Exception", e);
		}
		tokenMapDao.upsertTokenMap(tokenMapDevices);
		
		PushNotification.delAllInvalidTokens();
		
		logger.info("Push Notification process done");
	}
	
	public void updateTokenStatus(MulticastResult multiResult) {
		List<Result> results = multiResult.getResults();
		for (int i = 0; i < notificationListToken.size(); i++) {
			Result result = results.get(i);
			if (!StringUtils.isEmpty(result.getErrorCodeName())) {
				tokenMapDevices.getToken(notificationListToken.get(i)).addFailtAttemps();
				logger.error("Error occurred while sending push notification :" + result.getErrorCodeName());
				logger.error("  Token: " + notificationListToken.get(i));					
			} else {
				tokenMapDevices.getToken(notificationListToken.get(i)).updateValidPush();
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public static void sendAsyncPushNotification(String title, String body) {
		PushNotification pushNotification = new PushNotification();
		pushNotification.setTitle(title);
		pushNotification.setBody(body);
		pushNotification.start();
	}
	
	public static List<String> delAllInvalidTokens() {
		TokenMapDao tokenMapDao = TokenMapDaoFactory.getMapTokenDao();
		TokenMapDevices tokenMapDevices = tokenMapDao.getTokenMap();
		if (tokenMapDevices == null) 
			return null;		

		List<String> listInvalidTokens = getAllInvalidTokens(tokenMapDevices);
		for (String token : listInvalidTokens) {
			tokenMapDevices.removeToken(token);
		}
		tokenMapDao.upsertTokenMap(tokenMapDevices);	
		
		return listInvalidTokens;
	}
	
	public static List<String> getAllInvalidTokens(TokenMapDevices tokenMapDevices) {
		List<String> listInvalidTokens = new ArrayList<String>();
		for (TokenDevice tokenDevice : tokenMapDevices.getMapTokens().values()) {
			long daysSinceLastValidPush = tokenDevice.getLastValidPush().until( LocalDateTime.now(), ChronoUnit.DAYS);
			if (tokenDevice.getLastSequenceFailsAttempts() > GlobalConfiguration.getInstance().getGcmLimitFailsAttemps() &&
				daysSinceLastValidPush > GlobalConfiguration.getInstance().getGcmLimitFailDays()) 
				listInvalidTokens.add(tokenDevice.getToken());
		}		
		return listInvalidTokens;
	}

}
