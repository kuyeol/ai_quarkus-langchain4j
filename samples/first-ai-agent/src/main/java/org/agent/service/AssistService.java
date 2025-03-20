package org.agent.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import org.agent.modelcatalog.tool.MainExe;
import org.agent.models.GeminiFactory;
import org.agent.models.OllamaAgentFactory;

@ApplicationScoped
public class AssistService
{

  final GeminiFactory gemini;

  final OllamaAgentFactory olamaAgent;


  public AssistService(GeminiFactory gemini , OllamaAgentFactory olamaAgent)
  {
    this.gemini     = gemini;
    this.olamaAgent = olamaAgent;
  }


  public record Topic(String topic , String lang , String content)
  {

  }


  public String assist(Topic msg)
  {

    //    Assistant assistant = AiServices.create( Assistant.class , gemi );
    //Assistant assistant = AiServices.create( Assistant.class , gemini.getGemini2() );

    //String str =  assistant.chat(msg.topic,
    //                 msg.lang ,
    //                 msg.content );
    List<Document> documents = FileSystemDocumentLoader.loadDocuments(
      "D:\\Ung-ProJect\\ai_quarkus-langchain4j\\samples\\first" + "-ai-agent\\src\\main\\resources\\documents\\" );

    Assistant agent = AiServices.builder( Assistant.class )
                                .chatLanguageModel( gemini.getGeminiLite() )
                                .tools( new MainExe() )
                                .systemMessageProvider( chatMemId->msg.topic )
                                .chatMemory( MessageWindowChatMemory.withMaxMessages( 10 ) )
                                .build();

    return agent.chat(
      msg.topic ,
      msg.lang ,
      msg.content );
  }


  private static ContentRetriever createContentRetriever(List<Document> documents)
  {

    // Here, we create an empty in-memory store for our documents and their embeddings.
    InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    // Here, we are ingesting our documents into the store.
    // Under the hood, a lot of "magic" is happening, but we can ignore it for now.
    EmbeddingStoreIngestor.ingest(
      documents ,
      embeddingStore );

    // Lastly, let's create a content retriever from an embedding store.
    return EmbeddingStoreContentRetriever.from( embeddingStore );
  }


}
