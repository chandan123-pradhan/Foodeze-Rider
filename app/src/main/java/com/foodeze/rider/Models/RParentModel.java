package com.foodeze.rider.Models;

import java.util.Date;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class RParentModel {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public Date getDateFormate() {
        return dateFormate;
    }

    public void setDateFormate(Date dateFormate) {
        this.dateFormate = dateFormate;
    }

    public Date dateFormate;
}
