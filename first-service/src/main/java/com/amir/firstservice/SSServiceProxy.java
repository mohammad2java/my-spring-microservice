package com.amir.firstservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@RibbonClient
@FeignClient(name="second-service")
public interface SSServiceProxy {
	
	@GetMapping("/ss/ping")
	public String hitSecondService();

}
