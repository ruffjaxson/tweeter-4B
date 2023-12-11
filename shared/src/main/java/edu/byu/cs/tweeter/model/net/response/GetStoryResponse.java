package edu.byu.cs.tweeter.model.net.response;


import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * A paged response for a {@link GetStoryRequest}.
 */
public class GetStoryResponse extends PagedResponse<Status> {

        /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetStoryResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param items    the statuses to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public GetStoryResponse(List<Status> items, boolean hasMorePages) {
        super(hasMorePages, items);
    }
}
