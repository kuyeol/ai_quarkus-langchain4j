package io.quarkiverse.langchain4j.sample.chatbot;

import com.pgvector.PGvector;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import java.sql.*;
import java.util.UUID;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;


// tag::adocSkip[]

/**
 * @author Antonio Goncalves
 * http://www.antoniogoncalves.org
 * --
 */
// end::adocSkip[]
public class MusicianAssistant
{


  final static String  HOST     = "182.218.135.247";
  final static Integer PORT     = 5432;
  final static String  USERNAME = "quarkus";
  final static String  PASS     = "quarkus";
  final static String  DB       = "embedd";
  final static String  TABLE    = "pgvector";

  public static final  String USER     = "quarkus";
  public static final  String PASSWORD = "quarkus";
  private static final String URL      = "jdbc:postgresql://182.218.135.247:5432/embedd";

  public static void main(String[] args)
  throws SQLException
  {
    try (Connection conn = DriverManager.getConnection(URL,
                                                       USER,
                                                       PASSWORD))
    {
      // 1. pgvector 확장 설치 확인
      enableVectorExtension(conn);

      // 2. 테이블 생성
      createTable(conn);
      EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
      EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                                                                         .host(HOST)
                                                                         .port(PORT)
                                                                         .database(DB)
                                                                         .user(USERNAME)
                                                                         .password(PASS)
                                                                         .table("test")
                                                                         .dimension(embeddingModel.dimension())
                                                                         .build();


      TextSegment segment1 = TextSegment.from("I like football.",
                                              Metadata.metadata("userId",
                                                                "1"));
      Embedding embedding1 = embeddingModel.embed(segment1).content();
      embeddingStore.add(embedding1,
                         segment1);

      TextSegment segment2 = TextSegment.from("I like basketball.",
                                              Metadata.metadata("userId",
                                                                "2"));
      Embedding embedding2 = embeddingModel.embed(segment2).content();
      embeddingStore.add(embedding2,
                         segment2);

      Embedding queryEmbedding = embeddingModel.embed("What is your favourite sport?").content();

      // search for user 1

      Filter onlyForUser1 = metadataKey("userId").isEqualTo("1");

      EmbeddingSearchRequest embeddingSearchRequest1 = EmbeddingSearchRequest.builder()
                                                                             .queryEmbedding(queryEmbedding)
                                                                             .filter(onlyForUser1)
                                                                             .build();

      EmbeddingSearchResult<TextSegment> embeddingSearchResult1 = embeddingStore.search(embeddingSearchRequest1);
      EmbeddingMatch<TextSegment>        embeddingMatch1        = embeddingSearchResult1.matches().get(0);

      System.out.println(embeddingMatch1.score());
      System.out.println(embeddingMatch1.embedded().text());

      // search for user 2

      Filter onlyForUser2 = metadataKey("userId").isEqualTo("2");

      EmbeddingSearchRequest embeddingSearchRequest2 = EmbeddingSearchRequest.builder()
                                                                             .queryEmbedding(queryEmbedding)
                                                                             .filter(onlyForUser2)
                                                                             .build();

      EmbeddingSearchResult<TextSegment> embeddingSearchResult2 = embeddingStore.search(embeddingSearchRequest2);
      EmbeddingMatch<TextSegment>        embeddingMatch2        = embeddingSearchResult2.matches().get(0);

      System.out.println(embeddingMatch2.score());
      System.out.println(embeddingMatch2.embedded().text());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 1. pgvector 확장 활성화
  private static void enableVectorExtension(Connection conn)
  throws SQLException
  {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute("CREATE EXTENSION IF NOT EXISTS vector;");
      System.out.println("pgvector 확장 기능이 활성화되었습니다.");
    }
  }

  // 2. 테이블 생성
  private static void createTable(Connection conn)
  throws SQLException
  {
    String sql = """
                 CREATE TABLE IF NOT EXISTS test (
                     embedding_id UUID PRIMARY KEY,
                     embedding vector NULL,
                     text TEXT NULL,
                     metadata JSON NULL
                 );
                 """;
    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
      System.out.println("테이블이 생성되었습니다.");
    }
  }

  // 3. 임베딩 데이터 저장
  private static UUID saveEmbedding(Connection conn,
                                    PGvector vector)
  throws SQLException
  {


    UUID id = UUID.randomUUID();

    String sql = "INSERT INTO vintagestore_collection (id, embedding) VALUES ($1, $2)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setObject(1,
                      id);
      pstmt.setObject(2,
                      vector);
      pstmt.executeUpdate();
      System.out.println("임베딩 저장 완료: " + id);
    }
    return id;
  }

  // 4. 유사한 임베딩 검색
  private static void searchSimilarEmbeddings(Connection conn,
                                              float[] queryVector)
  throws SQLException
  {
    String sql = "SELECT embedding_id, embedding <=> ? AS distance FROM vintagestore_collection ORDER BY distance LIMIT 5";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setObject(1,
                      new PGvector(queryVector));
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          UUID   id       = (UUID) rs.getObject("embedding_id");
          double distance = rs.getDouble("distance");
          System.out.println("유사한 임베딩: " + id + ", 거리: " + distance);
        }
      }
    }
  }
}



