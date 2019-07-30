package com.buzzmicroservices.locationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class LocationApplication {
	private static final String GEOLOCATION = "geolocation";
	private static final String TIMEZONE = "timezone";

	@Value("${ipstack.url}")
	String geoUrl;
	@Value("${google.api.timezone.url}")
    String timezoneUrl;
	public static void main(String[] args) {
		SpringApplication.run(LocationApplication.class, args);
	}
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	@Bean(name=GEOLOCATION)
	public WebClient getWebClient(){
	return  WebClient.builder().baseUrl(geoUrl).build();
	}

	@Bean(name=TIMEZONE)
	public WebClient getTimezoneWebClient(){
	return  WebClient.builder().baseUrl(timezoneUrl).build();
	}

}
