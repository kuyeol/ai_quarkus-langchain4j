package org.agent.modelcatalog.data.embeddingstore;


import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.quarkus.jdbc.postgresql.runtime.PostgreSQLAgroalConnectionConfigurer;

public class PgVectorStore
{


  EmbeddingStore embeddingStore = new PgVectorEmbeddingStore();

  Embedding embedding;




  final static String  HOST  = "182.218.135.247";
  final static Integer PORT  = 5432;
  final static String  USER  = "quarkus";
  final static String  PASS  = "quarkus";
  final static String  DB    = "chat memory";
  final static String  TABLE = "pgvector";

  public PgVectorStore( ) {


  }

  public static void get() {
    try  {
      EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
      EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                                                                         .host(HOST)
                                                                         .port(PORT)
                                                                         .database(DB)
                                                                         .user(USER)
                                                                         .password(PASS)
                                                                         .table("test")
                                                                         .dimension(embeddingModel.dimension())
                                                                         .build();
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }


  }




//todo: sql connectred

}
