package edu.byu.cs.tweeter.server.lambda.interfaces;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;

public abstract class PagedServerHandler<T> extends ServerHandler<PagedRequest<T>> {

    @Override
    public void validateRequest(PagedRequest<T> request) throws RuntimeException {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token");
        }
    }

}
