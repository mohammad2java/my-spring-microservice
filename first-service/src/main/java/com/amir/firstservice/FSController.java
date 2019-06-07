package com.amir.firstservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FSController {
	
	@Autowired
	private PropConfig propConfig;
	
	@Autowired
	private SSServiceProxy proxy;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/fs/db")
	public DBConfig getDBConfi() {
		return new DBConfig(propConfig.getDbName(),propConfig.getDbUser());
	}

	
	@GetMapping("/fs/hi")
	public String getFSPing() {
		String runningPort = env.getProperty("local.server.port");
		return "This is second service running port is: "+runningPort;

	}

		
	
	@GetMapping("/fs/ping")
	public String getPing() {
		String ret = "first-service and called to ";
		String ssResponse = new RestTemplate().getForObject("http://localhost:8180/ss/ping", String.class);
		ret = ret+ssResponse;
		return ret;
	}
	
	@GetMapping("/fs/feign/ping")
	public String getPingByFeign() {
		String ret = "first-service and called to by feign ";
		String ssResponse = proxy.hitSecondService();
		ret = ret+ssResponse;
		return ret;
	}
}

