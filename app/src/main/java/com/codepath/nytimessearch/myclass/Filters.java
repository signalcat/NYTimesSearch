package com.codepath.nytimessearch.myclass;

import android.content.Intent;

import org.parceler.Parcel;

import java.util.Calendar;
import java.util.logging.Filter;

/**
 * Created by hezhang on 9/20/17.
 */
@Parcel
public class Filters {

    public void setArt(boolean check) {
        this.art = check;
    }

    public boolean isArt() {
        return art;
    }

    public void setFashion(boolean check) {
        this.fashion = check;
    }

    public boolean isFashion() {
        return fashion;
    }

    public void setSport(boolean check) {
        this.sport = check;
    }

    public boolean isSport() {
        return sport;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDate() {
        return date;
    }

    public String getOrder() {
        return order;
    }

    boolean art;
    boolean fashion;
    boolean sport;

    String date;
    String order;

    // empty constructor needed by Parceler library
    public Filters() {
    }

    public Filters(String date, String order, Boolean art, Boolean fashion, Boolean sport) {

        this.date = date;
        this.order = order;
        this.art = false;
        this.fashion = false;
        this.sport = false;
    }

}
