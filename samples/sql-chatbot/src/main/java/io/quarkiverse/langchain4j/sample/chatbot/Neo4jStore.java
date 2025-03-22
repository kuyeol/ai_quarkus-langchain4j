package io.quarkiverse.langchain4j.sample.chatbot;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;

public class Neo4jStore
{
  public static final String USERNAME = "neo4j";
  public static final String ADMIN_PASSWORD = "password";

  public static void main(String[] args) {




    EmbeddingStore<TextSegment> embeddingStore = Neo4jEmbeddingStore.builder()
                                                                    .withBasicAuth("neo4j://182.218.135.247:7474", USERNAME, ADMIN_PASSWORD)
                                                                    .dimension(384)
                                                                    .metadataPrefix("metadata.")
                                                                    .label("vintagestore_label")
                                                                    .indexName("vintagestore_idx")
                                                                    .build();
    EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
    TextSegment segment1   = TextSegment.from("I've been to France twice.");
    Embedding   embedding1 = embeddingModel.embed(segment1).content();
    embeddingStore.add(embedding1, segment1);

    TextSegment segment2 = TextSegment.from("New Delhi is the capital of India.");
    Embedding embedding2 = embeddingModel.embed(segment2).content();
    embeddingStore.add(embedding2, segment2);

    Embedding queryEmbedding = embeddingModel.embed("Did you ever travel abroad?").content();
    EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                                                           .queryEmbedding(queryEmbedding)
                                                           .maxResults(1)
                                                           .build();
    EmbeddingSearchResult<TextSegment> relevant       = embeddingStore.search(request);
    EmbeddingMatch<TextSegment>        embeddingMatch = relevant.matches().get(0);

    System.out.println(embeddingMatch.score());
    System.out.println(embeddingMatch.embedded().text());
  }


}
