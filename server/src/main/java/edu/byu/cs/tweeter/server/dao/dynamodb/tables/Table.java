package edu.byu.cs.tweeter.server.dao.dynamodb.tables;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public abstract class Table {

    /**
     * Gets the name of the table in DynamoDB that contains the items being accessed.
     * @return the name of the DynamoDB table
     */
    public abstract String tableName();
    public abstract String indexTableName();
    public abstract String partitionKey();
    public abstract String sortKey();
    public abstract String partitionValue();
    public abstract Object sortValue();

}
