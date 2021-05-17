package com.storyteller.gui.test_model;

import java.util.Date;

public class IdCard {
    private String number;
    private Date date;
    private String place;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "number='" + number + '\'' +
                ", date=" + date +
                ", place='" + place + '\'' +
                '}';
    }
}
