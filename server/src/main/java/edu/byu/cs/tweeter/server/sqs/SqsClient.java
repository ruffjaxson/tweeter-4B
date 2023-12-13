package edu.byu.cs.tweeter.server.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;


public class SqsClient {
    public static final String POSTS_Q = "https://sqs.us-west-2.amazonaws.com/300626769705/PostsQ";
    public static final String JOBS_Q = "https://sqs.us-west-2.amazonaws.com/300626769705/JobsQ";
    public static void addMessageToQueue(Object objToSend, String queueUrl) {


        System.out.println("queueUrl:" + queueUrl);
        System.out.println("Attempting to add the following objToSend to: " + objToSend);
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(JsonSerializer.serialize(objToSend));

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);
    }
} 