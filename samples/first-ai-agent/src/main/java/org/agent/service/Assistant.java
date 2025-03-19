package org.agent.service;

import dev.langchain4j.service.SystemMessage;
import jakarta.enterprise.context.Dependent;


public interface Assistant
{

  @SystemMessage( "You are a professional chef. You are friendly, polite and concise." )
  String chat(String userMessage);


}
