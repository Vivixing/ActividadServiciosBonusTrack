package co.com.echeverri.microservice.resolveEnigmaApi.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import co.com.echeverri.microservice.resolveEnigmaApi.model.client.ClientJsonApiBodyResponseSuccess;

@Component
public class GetStepThreeClientRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("direct:get-step-three")
		.routeId("GetStepThree")
		.setHeader(Exchange.HTTP_METHOD,constant("POST"))
		.setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
		.hystrix()
		.hystrixConfiguration().executionTimeoutInMilliseconds(2000).end()
		.to("freemarker:templates/GetStepThreeClientTemplate.ftl")
		.to("http4://localhost:8082/v1/getOneEnigma/getStep")
		.convertBodyTo(String.class)
		.log("String response microservice step three ${body}")
		.unmarshal().json(JsonLibrary.Jackson,ClientJsonApiBodyResponseSuccess.class)
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				ClientJsonApiBodyResponseSuccess stepOneResponse = (ClientJsonApiBodyResponseSuccess)exchange.getIn().getBody();
				if (stepOneResponse.getData().get(0).getAnswer().equalsIgnoreCase("Step3: Close the door")) {
					exchange.setProperty("Step3", stepOneResponse.getData().get(0).getAnswer());
				}else {
					exchange.setProperty("Error", "0001");
					exchange.setProperty("descError", "Step three is not valid");
				}
				
			}
		})
		.endHystrix()
		.onFallback()
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.setProperty("Error", "0002");
				exchange.setProperty("descError", "Error consulting the step three");
				
			}
		})
		.end()
		.log("Response code ${exchangeProperty.Error}")
		.log("Response description ${exchangeProperty.descError}");
	}

}
