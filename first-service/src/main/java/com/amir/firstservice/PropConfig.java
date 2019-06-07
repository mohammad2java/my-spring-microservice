package com.amir.firstservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("fs")
public class PropConfig {
	
	private String dbName;
	private String dbUser;
	
	
	public PropConfig(String dbName, String dbUser) {
		super();
		this.dbName = dbName;
		this.dbUser = dbUser;
	}
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public PropConfig() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
