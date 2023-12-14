package edu.byu.cs.tweeter.server.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A page of data returned by the database.
 *
 * @param <T> type of data objects being returned.
 */
public class DataPage<T> {
    private List<T> values; // page of values returned by the database
    private boolean hasMorePages; // Indicates whether there are more pages of data available to be retrieved

    public DataPage(List<T> values, boolean hasMorePages) {
        this.values = values;
        this.hasMorePages = hasMorePages;
    }

    public DataPage() {
        setValues(new ArrayList<T>());
        setHasMorePages(false);
    }

    public void setValues(List<T> values) {
//        System.out.println("values: " + values);
        this.values = values;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public List<T> getValues() {
        return values;
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    @Override
    public String toString() {
        return "DataPage{" +
                "values=" + values +
                ", hasMorePages=" + hasMorePages +
                '}';
    }
}