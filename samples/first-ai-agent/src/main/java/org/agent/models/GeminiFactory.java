package org.agent.models;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.concurrent.CompletableFuture;
import org.agent.modelcatalog.GoogleModels;
import org.agent.service.Assistant;

@ApplicationScoped
public class GeminiFactory
{

  static String API = "AIzaSyCTFY-MBprutyvpjEodSBSBr0DaK4rcJU8";

  private final StreamingChatLanguageModel gemini;


  @Inject
  public GeminiFactory()
  {
    this.gemini = GoogleAiGeminiStreamingChatModel.builder()
                                                  .apiKey( API )
                                                  .modelName( GoogleModels.GEMMA3_27B.getModelCode() )
                                                  .build();
  }


  public ChatResponse generateStream(String userMessage)
  {
    CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();
    gemini.chat( userMessage , new StreamingChatResponseHandler()
    {

      @Override
      public void onPartialResponse(String partialResponse)
      {
        System.out.print( partialResponse );
      }


      @Override
      public void onCompleteResponse(ChatResponse completeResponse)
      {
        futureResponse.complete( completeResponse );
      }


      @Override
      public void onError(Throwable error)
      {
        futureResponse.completeExceptionally( error );
      }
    } );

    return futureResponse.join();
  }





}
