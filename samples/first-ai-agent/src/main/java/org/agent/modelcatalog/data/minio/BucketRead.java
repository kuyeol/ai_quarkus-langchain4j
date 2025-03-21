package org.agent.modelcatalog.minio;

import io.minio.errors.InsufficientDataException;
import io.minio.errors.XmlParserException;
import io.minio.messages.ListPartsResult;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class BucketRead
{
  private static final int MAX_PART_COUNT = 1000;



  //
  //public ListPartsResult listParts(String bucket, String objectName, String uploadId) {
  //  try {
  //    return new ListPartsResult(bucket, null, objectName, MAX_PART_COUNT, null,
  //                               uploadId, null, null)
  //               .get()
  //               .result();
  //  } catch ( InsufficientDataException
  //            |
  //            NoSuchAlgorithmException |
  //            XmlParserException | InterruptedException
  //            |
  //            ExecutionException |
  //            IOException e) {
  //
  //    throw new RuntimeException("Can not list multipart uploads.", e);
  //  }
  //}

}
