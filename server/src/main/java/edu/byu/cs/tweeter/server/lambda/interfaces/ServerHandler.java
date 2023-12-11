package edu.byu.cs.tweeter.server.lambda.interfaces;


public abstract class ServerHandler<T> {

    public abstract void validateRequest(T request) throws RuntimeException;
    public void validateRequestAndLogReceipt(T request) throws RuntimeException {
        System.out.println("================= " + this.getClass() + " has received a request: " + request.toString());
        validateRequest(request);
    }
}
