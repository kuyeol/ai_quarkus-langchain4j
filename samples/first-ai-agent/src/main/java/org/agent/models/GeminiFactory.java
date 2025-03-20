package org.agent.models;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
import org.agent.modelcatalog.GoogleModels;

@ApplicationScoped
public class GeminiFactory
{

  static String API = "AIzaSyCTFY-MBprutyvpjEodSBSBr0DaK4rcJU8";

  private final StreamingChatLanguageModel gemini3;

  private final ChatLanguageModel gemini2;

  private final ChatLanguageModel geminiLite;


  public GeminiFactory()
  {
    this.geminiLite = GoogleAiGeminiChatModel.builder()
                                             .apiKey( API )
                                             .modelName( GoogleModels.GEMINI2_FLASH.getModelCode() )
                                             .build();

    this.gemini3 = GoogleAiGeminiStreamingChatModel.builder()
                                                   .apiKey( API )
                                                   .modelName( GoogleModels.GEMMA3_27B.getModelCode() )
                                                   .build();

    this.gemini2 = GoogleAiGeminiChatModel.builder()
                                          .apiKey( API )
                                          .modelName( GoogleModels.GEMINI2_PRO.getModelCode() )
                                          .build();
  }


  public ChatLanguageModel getGemini2()
  {
    return this.gemini2;
  }


  public ChatLanguageModel getGeminiLite()
  {
    return this.geminiLite;
  }


  public ChatResponse generateStream(String userMessage)
  {

    CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

    gemini3.chat(
      userMessage ,
      new StreamingChatResponseHandler()
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
