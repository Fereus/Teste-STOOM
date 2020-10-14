package com.fernando.stoomteste.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernando.stoomteste.models.Endereco;
import com.fernando.stoomteste.repository.EnderecoRepository;

import okhttp3.OkHttpClient;
import okhttp3.Request;

@RestController
@RequestMapping(value = "/api")
public class EnderecoResource {
	
	private static Double latitude;
	private static Double longitude;
	private static String urlGoogle = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	private static String key = "&key=AIzaSyDTK0igIQTCi5EYKL9tzOIJ9N6FUASGZos";

	@Autowired
	EnderecoRepository enderecoRepository;

	@GetMapping("/enderecos")
	public List<Endereco> addressList() throws Exception {
		
		List <Endereco> Addresses = enderecoRepository.findAll();
		
		if (Addresses.isEmpty())
			throw new Exception("Nenhum endereço encontrado.");
			
		
		return Addresses;

	}

	@GetMapping("/endereco/{id}")
	public ResponseEntity addressById(@PathVariable(value = "id") long id) throws Exception {

		Endereco address = enderecoRepository.findById(id);
		if (address == null) {

			throw new Exception("Endereço não encontrado.");

		}

		return new ResponseEntity<Endereco>(address, HttpStatus.CREATED).ok(address);
	}
	
	

	@PostMapping("/endereco")
	public Endereco saveAddress(@RequestBody Endereco endereco) throws Exception {
		
		Boolean valid = validateField(endereco);
		
		if(valid == false)
			throw new Exception("Favor preencher todos os campos corretamente.");
		
		if (endereco.getLatitude() == null || endereco.getLongitude() == null) {
			
			String address = endereco.getStreetName().replace(" ", "+");
			String state = endereco.getState().replace(" ", "+");
			String city = endereco.getCity().replace(" ", "+");
			String url = urlGoogle+endereco.getNumber() + address + ",+" + city + ",+" + state + key;
		    
		    JSONObject jsonObject = getLocationInfo(url);
			Boolean retLatLong = getLatLong(jsonObject);
			
			if(retLatLong == false)
				throw new Exception("Confirme se o endereço foi preenchido corretamente.");
			
			endereco.setLatitude(latitude);
			endereco.setLongitude(longitude);
		}

		
		return  enderecoRepository.save(endereco);

	}

	@PutMapping("/endereco")
	public Endereco updateAddress(@RequestBody Endereco endereco) throws Exception {
		
		Boolean valid = validateField(endereco);
		
		if(valid == false)
			throw new Exception("Favor preencher todos os campos corretamente.");
		
		if (endereco.getLatitude() == null || endereco.getLongitude() == null) {
			
			String address = endereco.getStreetName().replace(" ", "+");
			String state = endereco.getState().replace(" ", "+");
			String city = endereco.getCity().replace(" ", "+");
			String url = urlGoogle+endereco.getNumber() + address + ",+" + city + ",+" + state + key;
		    
		    JSONObject jsonObject = getLocationInfo(url);
			Boolean retLatLong = getLatLong(jsonObject);
			
			if(retLatLong == false)
				throw new Exception("Confirme se o endereço foi preenchido corretamente.");
			
			endereco.setLatitude(latitude);
			endereco.setLongitude(longitude);
		}


		return enderecoRepository.save(endereco);

	}

	@DeleteMapping("endereco")
	public void deleteAddress(@RequestBody Endereco endereco) throws Exception {
		
		if(endereco == null)
			throw new Exception("Endereço invalido.");
			
		Endereco address = enderecoRepository.findById(endereco.getId());	
		
		if(address == null)
			throw new Exception("Endereço invalido.");
		
		enderecoRepository.delete(endereco);
	}

	
	
	public static boolean validateField(Endereco address) {
		
		if (address == null ) {
			return false;
		}
		
		if (address.getStreetName().isEmpty() || address.getNumber() == null || address.getNeighbourhood().isEmpty() || address.getCity().isEmpty()
				|| address.getState().isEmpty() || address.getCountry().isEmpty() || address.getZipcode().isEmpty()) {
			return false;
		}
		
		return true;
	}
	
	public static JSONObject getLocationInfo(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

        
        HttpPost httppost = new HttpPost(url);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
           
            e.printStackTrace();
        }
        
        

        return jsonObject;
    }
	
	
	
	public static boolean getLatLong(JSONObject jsonObject) {
		
		if(jsonObject == null) {
			return false;
		}

        try {

        	longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");

        } catch (JSONException e) {
            return false;

        }

        return true;
    }

}
