package org.agent.modelcatalog.data.embeddingstore;

public class MovieSchemaSupport {

    public static String getSchemaString() {
        String columnsStr = MovieTableIntegrator.schemaStr;
        if (columnsStr.isEmpty()) {
            throw new IllegalStateException("MovieSchemaSupport#getSchemaString called too early");
        }
        return columnsStr;
    }
}
