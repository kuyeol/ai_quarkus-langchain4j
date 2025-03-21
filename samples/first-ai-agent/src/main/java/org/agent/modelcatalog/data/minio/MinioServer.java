package org.agent.modelcatalog.data.minio;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class MinioServer
{

  @Inject
  MinioAsyncClient asyncClient;

  @Inject
  BucketWrite      bucketWrite;

  @Inject
  MinioClient client;


  public MinioServer()
  {
    bucketWrite = new BucketWrite( client );
  }


  public BucketWrite getWriter(){
    return bucketWrite;
  }

  public GetObjectResponse getObject(String bucket , String objectName)
  {
    try {
      CompletableFuture<GetObjectResponse> future = asyncClient.getObject( GetObjectArgs.builder()
                                                                                        .bucket( bucket )
                                                                                        .object( objectName )
                                                                                        .build() );
      return future.get();
    } catch ( InsufficientDataException |
              InternalException |
              InvalidKeyException |
              IOException |
              NoSuchAlgorithmException |
              XmlParserException |
              InterruptedException |
              ExecutionException e ) {

      throw new RuntimeException(
        "Can not get object" ,
        e );
    }
  }


}
