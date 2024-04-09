package co.com.echeverri.microservice.resolveEnigmaApi.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import co.com.echeverri.microservice.resolveEnigmaApi.model.client.ClientJsonApiBodyResponseSuccess;

@Component
public class GetStepOneClientRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("direct:get-step-one")
		.routeId("GetStepOne")
		.setHeader(Exchange.HTTP_METHOD,constant("POST"))
		.setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
		/*.setBody(simple("{\n  \"data\": [\n    {\n      \"header\": {\n        \"id\": \"12345\",\n        \"type\": \"TestGiraffeRefrigerator\"\n      },\n      \"enigma\": \"How to put a giraffe into a refrigerator?\"\n    }\n  ]\n}"))*/
		.hystrix()
		.hystrixConfiguration().executionTimeoutInMilliseconds(2000).end()
		.to("freemarker:templates/GetStepOneClientTemplate.ftl")
		//.log("Request microservice step one ${body}")
		.to("http4://localhost:8080/v1/getOneEnigma/getStep")
		.convertBodyTo(String.class)
		.log("String response microservice step one ${body}")
		.unmarshal().json(JsonLibrary.Jackson,ClientJsonApiBodyResponseSuccess.class)
		//.log("Java response microservice step one ${body}")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				ClientJsonApiBodyResponseSuccess stepOneResponse = (ClientJsonApiBodyResponseSuccess)exchange.getIn().getBody();
				if (stepOneResponse.getData().get(0).getAnswer().equalsIgnoreCase("Step1: Open The Refrigerator")) {
					exchange.setProperty("Step1", stepOneResponse.getData().get(0).getAnswer());
					//exchange.setProperty("Error", "0000");
					//exchange.setProperty("descError", "No error");
				}else {
					exchange.setProperty("Error", "0001");
					exchange.setProperty("descError", "Step one is not valid");
				}
				
			}
		})
		.endHystrix()
		.onFallback()
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.setProperty("Error", "0002");
				exchange.setProperty("descError", "Error consulting the step one");
				
			}
		})
		.end()
		.log("Response code ${exchangeProperty.Error}")
		.log("Response description ${exchangeProperty.descError}");
	}

}
