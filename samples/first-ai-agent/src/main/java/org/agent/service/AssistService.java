package org.agent.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.agent.modelcatalog.GoogleModels;

@ApplicationScoped
public class AssistService
{

  static String API = "AIzaSyCTFY-MBprutyvpjEodSBSBr0DaK4rcJU8";

  private final ChatLanguageModel gemi;


  @Inject
  public AssistService()
  {
    this.gemi = GoogleAiGeminiChatModel.builder().apiKey( API ).modelName( GoogleModels.GEMINI2_PRO.getModelCode() ).build();
  }


  @Transactional
  public String assist()
  {

    //  String aa = gemini.chat( "dddd" );

    String    answer    = "";
    Assistant assistant = AiServices.create( Assistant.class , gemi );

    System.out.println( answer );

    return assistant.chat( "Hello World!" );
  }


}
