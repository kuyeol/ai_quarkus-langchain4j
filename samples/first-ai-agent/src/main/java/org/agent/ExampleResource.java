package org.agent;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path( "/hello" )
public class ExampleResource
{



  @GET
  @Produces( MediaType.TEXT_PLAIN )
  public String hello()
  {

     AgentFactory AgentFactory = new AgentFactory();
      String response = AgentFactory.generateResponse("Hello, how are you?");

    System.out.println(response);
    return "Hello from Quarkus REST"+
            "\n" +
            "Response from AI: " + response;
  }


}
