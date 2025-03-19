package org.agent;

import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.agent.models.GeminiFactory;
import org.agent.models.OllamaAgentFactory;
import org.agent.service.AssistService;

@ApplicationScoped
@Path( "/chat" )
public class ExampleResource
{

  @Inject
  OllamaAgentFactory ollama;

  @Inject
  GeminiFactory gemini;


  public ExampleResource(AssistService assistService)
  {
    this.assistService = assistService;
  }


  @GET
  @Path( "{msg}" )
  @Produces( MediaType.TEXT_PLAIN )
  public String hello(@PathParam( "msg" ) String msg)
  {

    String response = ollama.generateResponse( msg );

    System.out.println( response );
    return "Hello from Quarkus REST" + "\n" + "Response from AI: " + response;
  }


  @GET
  @Path( "stream/{msg}" )
  @Produces( MediaType.TEXT_PLAIN )
  public ChatResponse chatbot(@PathParam( "msg" ) String msg)
  {

    return ollama.generateStream( msg );
  }


  @GET
  @Path( "gemini/{msg}" )
  @Produces( MediaType.TEXT_PLAIN )
  public ChatResponse chatGemini(@PathParam( "msg" ) String msg)
  {

    return gemini.generateStream( msg );
  }



  final AssistService assistService;


  @GET
  @Path( "/assist" )
  public String assit()
  {

    return assistService.assist();
  }


}
