package org.agent.modelcatalog.data.database;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.ChatMemory;
import java.util.List;

public class AiMemory implements ChatMemory
{

  @Override
  public Object id()
  {
    return null;
  }


  @Override
  public void add(ChatMessage chatMessage)
  {

  }


  @Override
  public List<ChatMessage> messages()
  {
    return List.of();
  }


  @Override
  public void clear()
  {

  }


}
