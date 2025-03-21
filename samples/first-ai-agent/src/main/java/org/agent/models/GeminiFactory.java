package org.agent.models;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import org.agent.modelcatalog.GoogleModels;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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


  public static List<ChatRequest> msgQue  = new LinkedList<ChatRequest>();
  public static List<UserMessage> userQue = new LinkedList<UserMessage>();

  private final static int MAX_MEM = 10;

  private static void messageTemp(UserMessage message) {

    if (msgQue.size() == MAX_MEM) {
      userQue.remove(0);
      messageTemp(message);
    } else {
      userQue.add(message);
    }

  }

  public AiMessage messageForm(String message) {

    UserMessage user = UserMessage.from(message);
    ChatRequest msgs = ChatRequest.builder().messages(user).build();
    messageTemp(user);

    Set<UserMessage> history = userQue.stream().collect(Collectors.toSet());
    AiMessage        answer  = geminiLite.chat(history.toArray(new UserMessage[0])).aiMessage();


    // AiMessage answer2 = geminiLite.chat(userQue.toArray(new UserMessage[0])).aiMessage();


    for (UserMessage msg : userQue) {
      System.out.println(msg);
    }


    return answer;

  }
}
