package com.fernando.stoomteste.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assert;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.fernando.stoomteste.models.Endereco;
import com.fernando.stoomteste.resources.EnderecoResource;


class EnderecoResourcesTest {

	
	
	@Test
	void testValidateField(){
		
		Boolean valid;
		Endereco address = new Endereco();
		
		
		address.setStreetName("Rua tannus fares");
		address.setNumber(100);
		address.setComplement("");
		address.setNeighbourhood("Imperia Parque");
		address.setCity("Campinas");
		address.setState("SP");
		address.setCountry("Brasil");
		address.setZipcode("362356213-3");
		address.setLatitude(null);
		address.setLongitude(null);
		
		
        Endereco addressTwo = new Endereco();
		
        addressTwo.setId(1);
        addressTwo.setStreetName("");
        addressTwo.setNumber(100);
        addressTwo.setComplement("");
        addressTwo.setNeighbourhood("Imperia Parque");
        addressTwo.setCity("Campinas");
        addressTwo.setState("SP");
        addressTwo.setCountry("Brasil");
        addressTwo.setZipcode("362356213-3");
        addressTwo.setLatitude(null);
        addressTwo.setLongitude(null);
		
		
		valid = EnderecoResource.validateField(address);
		org.junit.Assert.assertTrue(valid);
				
		valid = EnderecoResource.validateField(null);
		org.junit.Assert.assertFalse(valid);
		
		valid = EnderecoResource.validateField(addressTwo);
		org.junit.Assert.assertFalse(valid);
	}
	
	@Test
	void testGetLocationInfo(){
		
		String url =  "https://maps.googleapis.com/maps/api/geocode/json?address=71+joao+campos,+campinas,+sp&key=AIzaSyDTK0igIQTCi5EYKL9tzOIJ9N6FUASGZos";
		JSONObject jsonObject = new JSONObject();
		 
		jsonObject = EnderecoResource.getLocationInfo(url);
		org.junit.Assert.assertNotEquals(null, jsonObject);
		
		jsonObject = EnderecoResource.getLocationInfo(url);
		org.junit.Assert.assertNotEquals("", jsonObject);
		
	}
	
	@Test
	void testGetLatLong(){
		String url =  "https://maps.googleapis.com/maps/api/geocode/json?address=71+joao+campos,+campinas,+sp&key=AIzaSyDTK0igIQTCi5EYKL9tzOIJ9N6FUASGZos";
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObjectTwo = new JSONObject();
		Boolean valid; 
		jsonObject = EnderecoResource.getLocationInfo(url);
		
		
		valid = EnderecoResource.getLatLong(jsonObject);
		org.junit.Assert.assertTrue(valid);
		
		valid = EnderecoResource.getLatLong(null);
		org.junit.Assert.assertFalse(valid);
		
		valid = EnderecoResource.getLatLong(jsonObjectTwo);
		org.junit.Assert.assertFalse(valid);
		
	}
	
	
	

}
