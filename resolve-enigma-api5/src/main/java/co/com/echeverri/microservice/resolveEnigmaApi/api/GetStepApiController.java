package co.com.echeverri.microservice.resolveEnigmaApi.api;

import co.com.echeverri.microservice.resolveEnigmaApi.model.JsonApiBodyRequest;
import co.com.echeverri.microservice.resolveEnigmaApi.model.JsonApiBodyResponseErrors;
import co.com.echeverri.microservice.resolveEnigmaApi.model.JsonApiBodyResponseSuccess;
import co.com.echeverri.microservice.resolveEnigmaApi.service.ResolveEnigmaService;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.net.SyslogAppenderBase;
import io.swagger.annotations.*;

import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-03-12T18:52:44.200-05:00[America/Bogota]")
@Controller
public class GetStepApiController implements GetStepApi {

    private static final Logger log = LoggerFactory.getLogger(GetStepApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    
    private Object response;
    
    @EndpointInject(uri="direct:resolve-enigma")
    private FluentProducerTemplate producerTemplateResolveEnigma;

    @org.springframework.beans.factory.annotation.Autowired
    public GetStepApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
/*
    public ResponseEntity<List<JsonApiBodyResponseSuccess>> getStep(@ApiParam(value = "request body get enigma step" ,required=true )  @Valid @RequestBody JsonApiBodyRequest body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<List<JsonApiBodyResponseSuccess>>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    @GetMapping(value = "/orquestador")
    public ResponseEntity<String> getOrquestador(){
    	ResolveEnigmaService resolveEnigmaService = new ResolveEnigmaService();
    	String responseOrquestador = resolveEnigmaService.fraseCompleta();
    	return new ResponseEntity<>(responseOrquestador,HttpStatus.OK);
    }
    
    public ResponseEntity<List<JsonApiBodyResponseSuccess>> getStep(@ApiParam(value="body",required=true)@Valid @RequestBody JsonApiBodyRequest body){
		try {
			producerTemplateResolveEnigma.request();
			return new ResponseEntity<JsonApiBodyResponseSuccess>(objectMapper.readValue("{\r\n\"data\": [{\r\n\"header\": {\r\n\"id\": \"id\",\r\n\"type\": \"type\"\r\n},\r\n\"answer\": \"answer\"\r\n}]\r\n}",JsonApiBodyResponseSuccess.class),HttpStatus.OK);
		}catch (Exception e) {
			log.error("Couldn't serialize response for content type application/json",e);
			return new ResponseEntity<JsonApiBodyResponseSuccess>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    */
    
	    public ResponseEntity<?> getStep(@ApiParam(value="body",required=true) @Valid @RequestBody JsonApiBodyRequest body) {
	        try {
	        	//producerTemplateResolveEnigma.send();
	            //List<JsonApiBodyResponseSuccess> responseList = new ArrayList<>();
	            //responseList.add(objectMapper.readValue("{\r\n\"data\": [{\r\n\"header\": {\r\n\"id\": \"id\",\r\n\"type\": \"type\"\r\n},\r\n\"answer\": \"answer\"\r\n}]\r\n}", JsonApiBodyResponseSuccess.class));
	            response = producerTemplateResolveEnigma.withBody(body).request();
	            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	            System.out.println(timestamp + " Respuesta Servicio");
	        	return new ResponseEntity<JsonApiBodyResponseSuccess>((JsonApiBodyResponseSuccess)response, HttpStatus.OK);
	        } catch (Exception e) {
	            log.error("Couldn't serialize response for content type application/json", e);
	            return new ResponseEntity<JsonApiBodyResponseErrors>((JsonApiBodyResponseErrors)response,HttpStatus.FAILED_DEPENDENCY);
	        }
	    }

}
