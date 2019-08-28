package com.buzzmicroservices.locationservice.controller;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.buzzmicroservices.locationservice.models.GeoLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.reactive.function.client.WebClient;



@RestController
@RequestMapping("/location")
public class LocationResourceController{

    private static final String GEOLOCATION = "geolocation";   
    private static final String TIMEZONE = "timezone";



    @Value("${ipstack.access_key}")
    private String apiStackAccessKey;

    @Value("${google.api.timezone.key}")
    private String googleAPIAccessKey;

    @Autowired
    @Qualifier(GEOLOCATION)
    private WebClient geoWebClient;
    @Autowired
    @Qualifier(TIMEZONE)
    private WebClient timezoneWebClient;

    @Value("${test.ip}")
    private String testIp;
    @Value("${test.city}")
    private String testCity;
    @Value("${test.lat}")
    private String testLat;
    @Value("${test.long}")
    private String testLong;
    @Value("${test.timezoneid}")
    private String testTimezoneId;
    @Value("${test.country}")
    private String testCountry;
    @Autowired
    BuildProperties buildProperties;


    Logger logger = LoggerFactory.getLogger(LocationResourceController.class);


    @RequestMapping("/health")
    @CrossOrigin
    public HashMap<String,String> getHealth(HttpServletRequest request){
        HashMap<String, String> map = new HashMap<>();
        map.put("Status", "We are live but hopefully OK");
        map.put("Version", buildProperties.getVersion());
        return map;
    }

    //public List<GeoLocation> getGeoLocation(@PathVariable("ip") String ip, HttpServletRequest request){
    @RequestMapping(method = RequestMethod.GET)
    @CrossOrigin
    //@RunWith(SpringRunner.class)
    public List<GeoLocation> getGeoLocation(HttpServletRequest request){    
        logger.info(apiStackAccessKey);
        //logger.info(ip);
        logger.info(request.getRemoteAddr());

        
        GeoLocation result = getLatLong(request);
        GeoLocation resultTimeZone = new GeoLocation();
        logger.info(result.getLatitude() + "-" + result.getLongitude() +"-" +Instant.now().getEpochSecond() +"-" + googleAPIAccessKey);
        if((result.getLatitude() == null|| result.getLongitude() == null)){
            result = new GeoLocation(testIp, testCity, testCountry,"US", testLat, testLong, testTimezoneId);
        }
        else{
             resultTimeZone = getTime(result);
             result.setTimeZoneId(resultTimeZone.getTimeZoneId());
        }

        return Collections.singletonList(result);
        
    }


    public GeoLocation getLatLong(HttpServletRequest request) {
        logger.info(request.getHeader("X-FORWARDED-FOR"));
        GeoLocation result = geoWebClient
        .post()
        .uri(uriBuilder ->uriBuilder
                    //.path(request.getRemoteAddr().equals(localIpv6) ?  hardcodedIp : request.getRemoteAddr() )
                    .path(request.getRemoteAddr())
                    .queryParam("access_key",apiStackAccessKey)
                    .build())
        .retrieve()
        .bodyToMono(GeoLocation.class)
        .block();
        return result;
    }


    public GeoLocation getTime(GeoLocation result) {
        GeoLocation resultTimeZone = timezoneWebClient
        .post()
        .uri(uriBuilder ->uriBuilder
                    .queryParam("location", result.getLatitude()+","+result.getLongitude())
                    .queryParam("timestamp", Instant.now().getEpochSecond())
                    .queryParam("key",googleAPIAccessKey)
                    .build())
        .retrieve()
        .bodyToMono(GeoLocation.class)
        .block();
        return resultTimeZone;
    }
}

