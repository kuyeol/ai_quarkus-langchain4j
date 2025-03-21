package org.agent.modelcatalog.data.embeddingstore;


public class PgVectorClient
{
  private String  host;
  private Integer port;
  private String  user;
  private String password;
  private String database;
  private String tableName;


  public String getTableName() {
    return tableName;
  }

  public String getDatabase() {
    return database;
  }

  public String getPassword() {
    return password;
  }

  public String getUser() {
    return user;
  }

  public Integer getPort() {
    return port;
  }

  public String getHost() {
    return host;
  }




  public PgVectorClient(String host,
                        Integer port,
                        String user,
                        String password,
                        String database,
                        String tableName
                       )
  {
    this.host     = host;
    this.port     = port;
    this.user     = user;
    this.password = password;
    this.database = database;
    this.tableName = tableName;
  }


  public static class PgVectorClientbuilder{
    private String  host;
    private Integer port;
    private String  user;
    private String password;
    private String database;
    private String tableName;


    PgVectorClientbuilder(){}

    public PgVectorClientbuilder host(String host) {
      this.host = host;
      return this;
    }

    public PgVectorClientbuilder port(Integer port) {
      this.port = port;

      return this;
    }

    public PgVectorClientbuilder user(String user) {
      this.user = user;
      return this;
    }

    public PgVectorClientbuilder password(String password) {
      this.password = password;
      return this;
    }

    public PgVectorClientbuilder database(String database) {
      this.database = database;
      return this;
    }

    public PgVectorClientbuilder tableName(String tableName) {
      this.tableName = tableName;
      return this;
    }

    public PgVectorClient build() {

      return new PgVectorClient(this.host, this.port, this.user, this.password, this.database, this.tableName);
    }
  }


}
