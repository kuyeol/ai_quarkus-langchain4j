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


    public ListPartsResult listParts(String bucket,
                                     String objectName,
                                     String uploadId,
                                     int partNumberMarker)
    throws IOException, NoSuchAlgorithmException, XmlParserException, InsufficientDataException, ExecutionException,
           InterruptedException
    {
        try {
            return new ListPartsResult();
        } catch (RuntimeException e) {
            throw new RuntimeException("Can not list multipart uploads.",
                                       e);
        }
    }


    public FormDataFile listParts(String bucket,
                                  String objectName,
                                  String uploadId)
    {

        try {

            return new FormDataFile();


        } catch (RuntimeException e) {

            throw new RuntimeException("Can not list multipart uploads.",
                                       e);
        }
    }

}
