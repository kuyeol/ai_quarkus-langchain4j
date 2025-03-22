package org.agent.modelcatalog.data.embeddingstore;


import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class PgVectorStore
{


  final static String  HOST  = "182.218.135.247";
  final static Integer PORT  = 5432;
  final static String  USER  = "quarkus";
  final static String  PASS  = "quarkus";
  final static String  DB    = "embedd";
  final static String  TABLE = "pgvector";

  static EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();


  private final PgPool           client;
  private final boolean          schemaCreate;
  final         MovieMuseSupport support;

  public PgVectorStore(PgPool client,
                       @ConfigProperty(name = "myapp.schema.create", defaultValue = "true") boolean schemaCreate,
                       MovieMuseSupport support)
  {
    this.client       = client;
    this.schemaCreate = schemaCreate;
    this.support      = support;

  }

  void onStart(@Observes StartupEvent ev) {
    if (schemaCreate) {
      initdb();
    }
  }

  private void initdb() {
    // TODO
  }

  //private static String URI      = "neo4j://182.218.135.247:7474";
  private static String URI      = "bolt://182.218.135.247:7687";
  private static String USERNAME = "neo4j";
  private static String PASSWORD = "password";

  public static void main(String[] args) {
    EmbeddingStore<TextSegment> neo4j = Neo4jEmbeddingStore.builder()
                                                           .withBasicAuth(URI,
                                                                          USERNAME,
                                                                          PASSWORD)
                                                           .dimension(384)
                                                           .build();


    Response<Embedding> dd         = embeddingModel.embed("gggg");
    TextSegment         segment1   = TextSegment.from("HI");
    Embedding           embedding1 = embeddingModel.embed(segment1).content();
    neo4j.add(embedding1,
              segment1);

    System.out.println(embedding1);
  }

//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.MongoException;
//import com.mongodb.ServerApi;
//import com.mongodb.ServerApiVersion;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//  public class MongoClientConnectionExample {
//    public static void main(String[] args) {
//      String connectionString = "mongodb+srv://zimop12:<db_password>@cluster0.ml1oqzw.mongodb.net/?appName=Cluster0";
//      ServerApi serverApi = ServerApi.builder()
//                                     .version(ServerApiVersion.V1)
//                                     .build();
//      MongoClientSettings settings = MongoClientSettings.builder()
//                                                        .applyConnectionString(new ConnectionString(connectionString))
//                                                        .serverApi(serverApi)
//                                                        .build();
//      // Create a new client and connect to the server
//      try (MongoClient mongoClient = MongoClients.create(settings)) {
//        try {
//          // Send a ping to confirm a successful connection
//          MongoDatabase database = mongoClient.getDatabase("admin");
//          database.runCommand(new Document("ping", 1));
//          System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
//        } catch (MongoException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//  }

  public void get() {

    EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
    EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                                                                       .createTable(true)
                                                                       .table("test")
                                                                       .host(HOST)
                                                                       .port(PORT)
                                                                       .database(DB)
                                                                       .user(USER)
                                                                       .password(PASS)
                                                                       .dropTableFirst(true)
                                                                       .dimension(384)
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
