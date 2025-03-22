package org.agent.neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore.Neo4jDistanceType;


public class NeoContainer
{

  private static String URI      = "neo4j://182.218.135.247:7474";
  private static String USERNAME = "neo4j";
  private static String PASSWORD = "password";

  public NeoContainer(EmbeddingModel embeddingModel) {
    this.embeddingModel = embeddingModel;
  }


  @Bean
  public Driver driver() {
    return GraphDatabase.driver(URI,
                                AuthTokens.basic(USERNAME,
                                                 PASSWORD));
  }

  @Bean
  public VectorStore vectorStore(Driver driver,
                                 EmbeddingModel embeddingModel)
  {
    return Neo4jVectorStore.builder(driver,
                                    embeddingModel)
                           .databaseName("neo4j")                // Optional: defaults to "neo4j"
                           .distanceType(Neo4jDistanceType.COSINE)// Optional: defaults to COSINE
                           .embeddingDimension(1536)                      // Optional: defaults to 1536
                           .label("Document")                     // Optional: defaults to "Document"
                           .embeddingProperty("embedding")        // Optional: defaults to "embedding"
                           .indexName("custom-index")             // Optional: defaults to "spring-ai-document-index"
                           .initializeSchema(true)                // Optional: defaults to false
                           .batchingStrategy(new TokenCountBatchingStrategy()) // Optional: defaults to TokenCountBatchingStrategy
                           .build();
  }

  private final EmbeddingModel embeddingModel= new OllamaEmbeddingModel("http://182.218.135.247:11434","",false,false,false,false).build();

  @Bean
  EmbeddingResponse embeddingResponse = embeddingModel.call(
    new EmbeddingRequest(List.of("Hello World", "World is big and salvation is near"),
                         OllamaOptions.builder()
                                      .model("Different-Embedding-Model-Deployment-Name"))
      .truncates(false)
      .build());
}
