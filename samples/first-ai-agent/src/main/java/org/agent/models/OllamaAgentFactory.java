package org.agent.models;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class OllamaAgentFactory
{

  static String MODEL_NAME = "gemma3:4b"; // try other local ollama model names

  static String BASE_URL = "http://182.218.135.247:11434";

  private final ChatLanguageModel languageModel;

  private  final StreamingChatLanguageModel streamModel;


  @Inject
  public OllamaAgentFactory()
  {
    this.languageModel = OllamaChatModel.builder()
                                        .baseUrl( BASE_URL )
                                        .modelName( MODEL_NAME )
                                        .temperature( 0.2 )
                                        .timeout( Duration.ofSeconds( 60000 ) )
                                        .build();

    this.streamModel = OllamaStreamingChatModel.builder().baseUrl( BASE_URL ).modelName( MODEL_NAME ).temperature( 0.0 ).build();
  }


  public ChatResponse generateStream(String userMessage)
  {
    CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();
    streamModel.chat( userMessage , new StreamingChatResponseHandler()
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


  public String generateResponse(String userMessage)
  {
    try {
      ChatResponse response = languageModel.chat( ChatRequest.builder().messages( UserMessage.from( userMessage ) ).build() );

      return response.aiMessage().text();
    } catch ( Exception e ) {
      // 로깅 추가하는 것이 좋습니다
      return "AI 응답 생성 중 오류가 발생했습니다: " + e.getMessage();
    }
  }


  public static void main(String... arg)
  {
    ChatLanguageModel model  = OllamaChatModel.builder().baseUrl( BASE_URL ).modelName( MODEL_NAME ).build();
    String            answer = model.chat( "List top 10 cites in China" );
    System.out.println( answer );
  }


}
