package co.com.echeverri.microservice.resolveEnigmaApi.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResolveEnigmaService {
	
	public String fraseCompleta (){
		RestTemplate restTemplate=new RestTemplate();
		RestTemplate restTemplate2=new RestTemplate();
		RestTemplate restTemplate3=new RestTemplate();
		String fooResourceUrl="http://localhost:8080/v1/getOneEnigma/servicio1";
		String fooResourceUrl2="http://localhost:8081/v1/getOneEnigma/servicio2";
		String fooResourceUrl3="http://localhost:8082/v1/getOneEnigma/servicio3";
		ResponseEntity<String> response =restTemplate.getForEntity(fooResourceUrl, String.class);
		ResponseEntity<String> response2 =restTemplate2.getForEntity(fooResourceUrl2, String.class);
		ResponseEntity<String> response3 =restTemplate3.getForEntity(fooResourceUrl3, String.class);
		
		String body1 = response.getBody();
		String body2 = response2.getBody();
		String body3 = response3.getBody();
		
		String concatenated = body1.concat(body2).concat(body3);
		
		return concatenated;
	}

}
