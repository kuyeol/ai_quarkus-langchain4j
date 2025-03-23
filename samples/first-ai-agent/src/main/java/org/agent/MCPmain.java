package org.agent;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.agent.models.GeminiFactory;
import org.agent.models.OllamaAgentFactory;

import java.util.List;

public class MCPmain
{

  public static void main(String[] args)
  throws Exception
  {


    ChatLanguageModel model = new OllamaAgentFactory().providesModel("qwen");

    McpTransport transport = new StdioMcpTransport.Builder().command(List.of("/usr/local/bin/docker",
                                                                             "run",
                                                                             "-e",
                                                                             GIT_TOKEN,
                                                                             "-i",
                                                                             "mcp/github")).logEvents(true).build();

    McpClient mcpClient = new DefaultMcpClient.Builder().transport(transport).build();


    ToolProvider toolProvider = McpToolProvider.builder().mcpClients(List.of(mcpClient)).build();

    Bot bot = AiServices.builder(Bot.class).chatLanguageModel(model).toolProvider(toolProvider).build();

    try {
      String response = bot.chat("Summarize the last 3 commits of the LangChain4j GitHub repository");
      System.out.println("RESPONSE: " + response);
    } finally {
      mcpClient.close();
    }
  }
}
