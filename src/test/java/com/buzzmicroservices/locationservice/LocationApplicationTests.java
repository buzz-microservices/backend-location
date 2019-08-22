package com.buzzmicroservices.locationservice;


import javax.servlet.http.HttpServletRequest;
import com.buzzmicroservices.locationservice.controller.LocationResourceController;
import com.buzzmicroservices.locationservice.models.GeoLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationApplicationTests {

final LocationResourceController locationResource = Mockito.spy(new LocationResourceController());


   private MockMvc mockMvc;
   private GeoLocation mockGeo;
   private GeoLocation mockGeoAndTZ;




   @Before
   public void setUp() throws Exception{
	mockGeo = new GeoLocation("127.0.0.1","HOME","HOME","US","LAT","LONG",null);
	mockGeoAndTZ = new GeoLocation("","","","","","","TZ");
	mockMvc = MockMvcBuilders.standaloneSetup(locationResource).build();
	Mockito.doReturn(mockGeo).when(locationResource).getLatLong(Mockito.any(HttpServletRequest.class));
	Mockito.doReturn(mockGeoAndTZ).when(locationResource).getTime(mockGeo);




   }
	@Test
	public void exampleTest()throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/location"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("[0].ip").value("127.0.0.1"))
		.andExpect(MockMvcResultMatchers.jsonPath("[0].city").value("HOME"))
		.andExpect(MockMvcResultMatchers.jsonPath("[0].country_name").value("HOME"))
		.andExpect(MockMvcResultMatchers.jsonPath("[0].latitude").value("LAT"))
		.andExpect(MockMvcResultMatchers.jsonPath("[0].longitude").value("LONG"))
		.andExpect(MockMvcResultMatchers.jsonPath("[0].timeZoneId").value("TZ"));

	
	}




}
