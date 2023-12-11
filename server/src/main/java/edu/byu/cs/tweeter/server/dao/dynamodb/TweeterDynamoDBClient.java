package edu.byu.cs.tweeter.server.dao.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class TweeterDynamoDBClient {
    private static DynamoDbEnhancedClient client;

    private static DynamoDbEnhancedClient buildClient(){
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build();
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    public static DynamoDbEnhancedClient getClient()
    {
        if (client == null) {
            client = buildClient();
        }
        return client;
    }
}
