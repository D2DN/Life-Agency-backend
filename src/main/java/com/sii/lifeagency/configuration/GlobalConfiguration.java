package com.sii.lifeagency.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GlobalConfiguration {

	private static GlobalConfiguration gc;

	private String pathImage;
	private String databaseName;
	private String databaseServer;
	private int databasePort;
	private String databaseType;
	private String gcmApiKey; 
	private int gcmRetries;
	public final static String KEYTOKENMAP = "TOKENMAP";
	private int gcmLimitFailDays;
	private int gcmLimitFailsAttemps; 
	private String azureAppId;
	private List<String> validGroups;

	public GlobalConfiguration() {
		final Properties properties = new Properties();
		try (final InputStream stream = GlobalConfiguration.class.getClassLoader()
				.getResourceAsStream("config.properties")) {
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.pathImage = properties.get("pathImage").toString();
		this.databaseName = properties.get("databaseName").toString();
		this.databaseServer = properties.get("databaseServer").toString();
		this.databasePort = Integer.parseInt(properties.get("databasePort").toString());
		this.databaseType = properties.get("databaseType").toString();
		this.validGroups = new ArrayList<String>();
		this.gcmApiKey = properties.getProperty("gcmapikey").toString();
		this.gcmRetries = Integer.parseInt(properties.getProperty("gcmretries"));
		this.gcmLimitFailDays = Integer.parseInt(properties.getProperty("gcmlimitfaildays"));
		this.gcmLimitFailsAttemps = Integer.parseInt(properties.getProperty("gcmlimitfailattemps"));
		this.azureAppId = properties.getProperty("azureAppId");
		validGroups.add("grp-lifeagency-adm");
		validGroups.add("grp-lifeagency-write");
		validGroups.add("grp-lifeagency-read");

	}

	public static GlobalConfiguration getInstance() {
		if (gc == null) {
			gc = new GlobalConfiguration();
		}
		return gc;
	}

	public String getPathImage() {
		return pathImage;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getDatabaseServer() {
		return databaseServer;
	}

	public int getDatabasePort() {
		return databasePort;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public List<String> getValidGroups() {
		return validGroups;
	}

	public String getGcmApiKey() {
		return gcmApiKey;
	}

	public int getGcmRetries() {
		return gcmRetries;
	}

	public int getGcmLimitFailDays() {
		return gcmLimitFailDays;
	}

	public int getGcmLimitFailsAttemps() {
		return gcmLimitFailsAttemps;
	}

	public String getAzureAppId() {
		return azureAppId;
	}

}
