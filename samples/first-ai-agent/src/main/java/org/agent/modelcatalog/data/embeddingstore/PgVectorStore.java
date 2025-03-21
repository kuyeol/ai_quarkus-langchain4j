package org.agent.modelcatalog.data.embeddingstore;


import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;


public class PgVectorStore
{






  final static String  HOST  = "182.218.135.247";
  final static Integer PORT  = 5432;
  final static String  USER  = "quarkus";
  final static String  PASS  = "quarkus";
  final static String DB = "chatmemory";
  final static String  TABLE = "pgvector";

  public PgVectorStore() {
  }

  public static void main(String[] args) {
    PgVectorStore store = new PgVectorStore();
    store.get();
  }


  //todo: sql connectred

  public void get() {

      EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
      EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                                                                         .createTable(true)
                                                                         .dropTableFirst(true)
                                                                         .host(HOST)
                                                                         .port(PORT)
                                                                         .database(DB)
                                                                         .user(USER)
                                                                         .password(PASS)
                                                                         .table("test")
                                                                         .dimension(544)
                                                                         .build();

    TextSegment segment1   = TextSegment.from("I've been to France twice.");
    Embedding   embedding1 = embeddingModel.embed(segment1).content();
    embeddingStore.add(embedding1,
                       segment1);

    TextSegment segment2   = TextSegment.from("New Delhi is the capital of India.");
    Embedding   embedding2 = embeddingModel.embed(segment2).content();
    embeddingStore.add(embedding2,
                       segment2);

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
