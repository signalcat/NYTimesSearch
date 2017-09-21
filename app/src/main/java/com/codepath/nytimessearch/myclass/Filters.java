package com.codepath.nytimessearch.myclass;

import org.parceler.Parcel;

import java.util.logging.Filter;

/**
 * Created by hezhang on 9/20/17.
 */
@Parcel
public class Filters {
    String date;
    String order;
    String category;

    public void setDate(String date) {
        this.date = date;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public String getOrder() {
        return order;
    }

    public String getCategory() {
        return category;
    }

    // empty constructor needed by Parceler library
    public Filters() {
    }

    public Filters(String date, String order, String category) {
        this.date = date;
        this.order = order;
        this.category = category;
    }
}
