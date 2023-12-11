package edu.byu.cs.tweeter.model.net.response;

import java.util.ArrayList;
import java.util.List;

/**
 * A response that can indicate whether there is more data available from the server.
 */
public class PagedResponse<T> extends Response {

    private final boolean hasMorePages;
    private final List<T> items;

    public PagedResponse(boolean hasMorePages, List<T> items) {
        super(true, null);
        this.hasMorePages = hasMorePages;
        this.items = items;
    }

    PagedResponse(boolean success, String message) {
        super(success, message);
        this.hasMorePages = false;
        this.items = new ArrayList<>();
    }




    public List<T> getItems() { return items; }

    /**
     * An indicator of whether more data is available from the server. A value of true indicates
     * that the result was limited by a maximum value in the request and an additional request
     * would return additional data.
     *
     * @return true if more data is available; otherwise, false.
     */
    public boolean getHasMorePages() {
        return hasMorePages;
    }
}
