package org.agent.modelcatalog.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class BucketWrite
{

  private final MinioClient client;


  public BucketWrite(MinioClient client)
  {
    this.client = client;
  }


  public void FileUpload() throws IOException, NoSuchAlgorithmException, InvalidKeyException
  {
    try {

      boolean found = client.bucketExists( BucketExistsArgs.builder().bucket( "asiatrip" ).build() );
      if ( !found ) {
        client.makeBucket( MakeBucketArgs.builder().bucket( "asiatrip" ).build() );
      } else {
        System.out.println( "Bucket 'asiatrip' already exists." );
      }

      client.uploadObject( UploadObjectArgs.builder()
                                           .bucket( "asiatrip" )
                                           .object( "asiaphotos-2015.zip" )
                                           .filename( "/asiaphotos.zip" )
                                           .build() );
      System.out.println( "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as " +
                          "object 'asiaphotos-2015.zip' to bucket 'asiatrip'." );
    } catch ( MinioException e ) {
      System.out.println( "Error occurred: " + e );
      System.out.println( "HTTP trace: " + e.httpTrace() );
    }
  }


}
