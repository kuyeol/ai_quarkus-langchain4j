package org.agent;

import dev.langchain4j.model.chat.response.ChatResponse;
import io.vertx.core.http.HttpServerFileUpload;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.impl.FileUploadImpl;
import io.vertx.mutiny.ext.web.multipart.MultipartForm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import okhttp3.MultipartBody;
import org.agent.modelcatalog.minio.FormDataFile;
import org.agent.modelcatalog.minio.MinioServer;
import org.agent.models.GeminiFactory;
import org.agent.models.OllamaAgentFactory;
import org.agent.service.AssistService;
import org.jboss.resteasy.reactive.server.core.multipart.FormData;

@ApplicationScoped
@Path( "/chat" )
public class ExampleResource
{

  @Inject
  OllamaAgentFactory ollama;

  @Inject
  GeminiFactory gemini;

  @Inject
  AssistService assistService;

  @Inject
  MinioServer minioService;


  @POST
  @Path( "/upload" )
  @Consumes( MediaType.MULTIPART_FORM_DATA )
  @Produces( MediaType.APPLICATION_JSON )
  public Response uploadFile(FormDataFile ff) throws Exception
  {

    return Response.ok().build();
  }


  @GET
  @Path( "minio" )
  public Response getMinio()
  {
    InputStream inputStream = minioService.getObject(
      "rag" ,
      "aa.txt" );

    minioService.getObject(
      "rag" ,
      "aa.txt" );
    return Response.ok(
      inputStream ,
      MediaType.TEXT_PLAIN ).build();
  }


  @GET
  @Path( "minio/make" )
  public Response makeMinio() throws IOException, NoSuchAlgorithmException, InvalidKeyException
  {
    minioService.getWriter().FileUpload();
    return Response.ok( MediaType.TEXT_PLAIN ).build();
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


  @GET
  @Path( "/assist/{user}" )
  @Produces( MediaType.TEXT_PLAIN )
  public String assit(@PathParam( "user" ) String user)
  {
    AssistService.Topic msg = new AssistService.Topic(
      "algorithm" ,
      "java" ,
      user );

    return assistService.assist( msg );
  }


}
