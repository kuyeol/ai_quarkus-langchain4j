package org.agent.models;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Stack;
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
                                             .apiKey(API)
                                             .modelName(GoogleModels.GEMINI2_FLASH.getModelCode())
                                             .build();

    this.gemini3 = GoogleAiGeminiStreamingChatModel.builder()
                                                   .apiKey(API)
                                                   .modelName(GoogleModels.GEMMA3_27B.getModelCode())
                                                   .build();

    this.gemini2 = GoogleAiGeminiChatModel.builder()
                                          .apiKey(API)
                                          .modelName(GoogleModels.GEMINI2_PRO.getModelCode())
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

    gemini3.chat(userMessage,
                 new StreamingChatResponseHandler()
                 {

                   @Override
                   public void onPartialResponse(String partialResponse)
                   {
                     System.out.print(partialResponse);
                   }


                   @Override
                   public void onCompleteResponse(ChatResponse completeResponse)
                   {
                     futureResponse.complete(completeResponse);
                   }


                   @Override
                   public void onError(Throwable error)
                   {
                     futureResponse.completeExceptionally(error);
                   }
                 });

    return futureResponse.join();
  }


  public Stack<UserMessage> stack = new Stack<UserMessage>();

  private final static int MAX_MEM = 10;

  public void messageTemp(UserMessage message) {

    if (stack.size() == MAX_MEM) {
      stack.pop();
      messageTemp(message);
    } else {
      stack.push(message);
    }

  }


  public AiMessage messageForm(String message) {


    UserMessage user = UserMessage.from(message);
    messageTemp(user);

    AiMessage answer = geminiLite.chat(stack.toArray(new UserMessage[0])).aiMessage();


    for (UserMessage userMessage : stack) {
      System.out.println(userMessage);
    }

    return answer;

  }


}
