Spring microservice Notes(using Spring boot)
----------------------------------------------
		 
		What is microservice:
		This is smallest business unit which publish rest webservice along with layered architecture(ui/rest/service/dao/) and task each other in very easy way.
		
		What you want to achieved using microservice.
		1-	Deployed independent smallest business unit as micro-service
		2-	We can increase/decrease instance of microservice as per requirement
		3-	Load backing mechanism in case of multiple instance.
		4-	Central configuration location for all microservice
		5-	One api gatway for all microservice (logging, authentication, so on)
		6-	Unique id per request across all microservice (for log tracing in case of production issue)
		7-	Central server for all microservice logs.
		8-	Dynamic instance maintenance across all micro-service
		
		List of spring cloud starter
		1-	Config client & Config server (central config management)
		2-	Feign (easy way to call client)
		3-	Ribbon (Client load balancing)
		4-	Eureka client & Eureka server
		5-	Zuul api (Gateway Api) - Zuul is no longer under active development by Netflix. Spring now provides Spring Cloud Gateway as a replacement.
		6-	Cloud-Sleuth ( log tracing)
		7-	Zipkin ( log tracing)
		8-	hystrix /Resilience4J (circuit breaker )
		9-	ELK  (central logging) /corologix
		
		
		
		Config client & Config server
		--------------------------------------------
			Why:
			Just make configuration files must in central location. (all micro-service application. properties must be in same /single location just to 			add/modify will be easy)
			How:
			1.	create one cloud-config-server by adding starter dependency called- Config server
			2.	add following entries in application.properties.
			spring.application.name=<app-name>
			server.port=8888
			spring.cloud.config.server.git.uri=file:///D:/myLearning/git_repo
			3.	make sure above git location having microservice config file in following format
			<app-name>-[env].properties example-first-service.properties /first-service-dev.properties
			4.	enable the config server by adding annotation at root level.
			@EnableConfigServer
			@SpringBootApplication
			public class SpringCloudConfigServerApplication {}
			5.	create micro-service using config client starter dependency.
			6.	add following entry in application.properties.
			spring.application.name=first-service
			server.port=8080
			spring.cloud.config.uri=http://localhost:8888
			Feign
			Why:
			Just to make easy call to other microservice. This is one type of alternative for RestTemplate call.
			
			How:
			
			1.	Add feign starter dependency into pom
			2.	Add top level annotation in caller service. @EnableFeignClients
			3.	Create proxy interface for callee service & annotation.
			@FeignClient(name="second-service",url="localhost:8180")
			4.	Add copy all callee service method (only declaration)
			@GetMapping("/ss/ping")
			public String hitSecondService();
			
			
			
			
			Ribbon
			Why:
			Achieve the feature of load balancing with multiple instance of microservice
			
			How:
			5.	Add Ribbon dependency in micro-service pom.xml
			6.	Add class level annotation with proxy interface.
			@RibbonClient(name="second-service")
			7.	And configure host instance in application.properties 
			Syntax-<app-name>. listOfServers=server1,server2….
			second-service.ribbon.listOfServers=http://localhost:8180,http://localhost:8181
			
			Eureka server & client
			
			Why:
			To achieve dynamic server instance registration, it help to ribbon to not bound servers instance in properties file.
			
			How :
			1-	Make eureka server using starter dependency Eureka server
			2-	Add top level annotation
			a.	@SpringBootApplication
			b.	@EnableEurekaServer
			c.	public class EurekaNamingServerApplication
			
			3-	add properties in application.properties
			a.	spring.application.name=eureka-naming-server
			b.	server.port=8761
			c.	eureka.client.register-with-eureka=false
			d.	eureka.client.fetch-registry=false
			
			4-	verify by url: http://localhost:8761/
			5-	need to modify all micro-service by adding eureka client dependency called- Eureka discovery
			6-	add top level annotation of Eureka client
			a.	@EnableDiscoveryClient
			b.	public class FirstServiceApplication {
			
			7-	add eureka server uri in all micro-service properties.
			a.	eureka.client.service-url.default-zone=http://localhost:8761/eureka
			
				
			
			
			
			
			
			
			Zuul api gateway
			
			Why:
			This is api gateway for all micro-service ..or we can say if you want one gateway for all micro-service for getting common logging,authentication,,so…so on.
			
			How:
			1.	Create server by adding zuul and eureka discovery
			2.	Add top level annotation of zuul
			i.	@EnableZuulProxy
			ii.	@EnableDiscoveryClient
			iii.	@SpringBootApplication
			iv.	public class ZuulApiGatewayServerApplication 
			3.	Request for all micro-service must be in following format.
			i.	Zuul-host/<app-name>/uri-path
			ii.	Example- http://localhost:8765/first-service/fs/feign/ping
			
			4-Extend ZuulFilter class like- 
			@Component
			public class ZuulLoggingFilter extends ZuulFilter{

                          
			 => Spring Cloud Gateway requires Spring Boot 2.0 or later, because:
                                Spring Cloud Gateway:
				Built on Project Reactor and Spring WebFlux.
				Fully non-blocking, asynchronous, and reactive.
				Supports Path rewriting, rate limiting, filters, circuit breakers, OAuth2, etc.

                          <artifactId>spring-cloud-starter-gateway</artifactId>

                        ====== Spring Cloud Gateway Routes ======
			spring.cloud.gateway.routes[0].id=user-service
			spring.cloud.gateway.routes[0].uri=http://localhost:8081
			spring.cloud.gateway.routes[0].predicates[0]=Path=/users/**
			 
                        for Auth & Authz
	                ---------------------------
			public class AuthorizationFilter implements GatewayFilter {

                           
   
			Cloud-sleuth
			
			Why:
			This is to add unique-id with log across all micro-service service wise.
			
			How:
			1.	Add sleuth dependency 
			2.	Add sampler bean in top level for all micro-service and zuul server.
			@Bean
			public Sampler defaultSampler(){
			i.	return Sampler.ALWAYS_SAMPLE;
			}
			
			

			 Resilience4j in a Spring Boot project. We'll cover:
				Retry
				CircuitBreaker
				RateLimiter
						
			<dependency>
			    <groupId>io.github.resilience4j</groupId>
			    <artifactId>resilience4j-spring-boot2</artifactId>
			    <version>1.7.1</version>
			</dependency>
			
			application.properties
			# Retry Config
			resilience4j.retry.instances.myRetry.max-attempts=3
			resilience4j.retry.instances.myRetry.wait-duration=1s
			
			# Circuit Breaker Config
			resilience4j.circuitbreaker.instances.myCircuitBreaker.failure-rate-threshold=50
			resilience4j.circuitbreaker.instances.myCircuitBreaker.wait-duration-in-open-state=5s
			resilience4j.circuitbreaker.instances.myCircuitBreaker.sliding-window-size=5
			
			# Rate Limiter Config
			resilience4j.ratelimiter.instances.myRateLimiter.limit-for-period=2
			resilience4j.ratelimiter.instances.myRateLimiter.limit-refresh-period=1s
			
			
			 @Service
			public class ExternalService {
			
			    @Retry(name = "myRetry", fallbackMethod = "fallback")
			    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallback")
			    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
			    public String callExternalAPI() {
			        System.out.println("Calling external API...");
			        throw new RuntimeException("External API failed");
			    }
			
			    public String fallback(Throwable t) {
			        return "Fallback response due to: " + t.getMessage();
			    }
			}


                     CircuitBreaker [Resilence4J]
                    	[CLOSED] → (failure-rate exceeds threshold) → [OPEN]
			[OPEN]  → (wait-duration ends) → [HALF-OPEN]
			[HALF-OPEN] → (test calls succeed) → [CLOSED]
			[HALF-OPEN] → (any test call fails) → [OPEN]
			
	
			1-config client  (just need to add in pom.xml for all micro-service)
			2-config server(create one component and add this to pom and configure application.properties)
			spring.cloud.config.server.git.uri=file:///D:/myLearning/git_repo
			
			file:///D:/myLearning/git_repo this is location of git dir and properties file name must be in format <app-name>-[evn].properties
			
			first-service.properties
			first-service-dev.properties
			
			Testing by hitting following urls
			http://localhost:8888/first-service/default
			http://localhost:8888/first-service/dev
			
			
			
			
			every component must having following 2 entries in properties file.
			spring.application.name=spring-cloud-config-server
			server.port=8888
			




  you can access properties of center config servers using eureka
  ----------------------------------------------------------------
  
		  1) register config server into eureka
		  for specific branchs properties u can use following properties
		  spring.cloud.config.label=dev1
		  
		  
		  
		  
		  
		  
		 distributed Transaction management( based on event driven archetecture)
		 ====================================================================
	                    There are two ways of coordination sagas pattern:
			    ---------------------------------------------------
		1) Choreography - each local transaction publishes domain events that trigger local transactions in other services
	           this is called decentrlize approach ,this is mostly adapted by the industry.
	  
	  2) Orchestration - an orchestrator (object) tells the participants what local transactions to execute
	      this is called centralized approach not recommeded even if you have such specific requirement.
  











