package com.amir.secondservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SSController {
	
	@Autowired
	private Environment env;
	
	private static Logger log  = LoggerFactory.getLogger(SSController.class);
	
	@GetMapping("/ss/ping")
	public String hitSecondService() {
		String runningPort = env.getProperty("local.server.port");
		log.info("Running in SecondService-with Port:"+runningPort);
		return "This is second service running port is: "+runningPort;
	}

}
