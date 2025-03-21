package org.agent.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import jakarta.enterprise.context.Dependent;


public interface Assistant
{

  @SystemMessage( """
      You are a professional Developer, You are friendly, polite and concise.generated Code use execute tool report output
    """ )
  @UserMessage( """
  {{topic}}
 {{lang}} to  {{msg}} write code
    """

  )
  String chat(@V("topic")String topic, @V("lang") String lang , @V("msg")String msg);



}
