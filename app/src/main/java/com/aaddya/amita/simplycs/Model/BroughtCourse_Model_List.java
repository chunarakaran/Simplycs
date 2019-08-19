package com.aaddya.amita.simplycs.Model;

public class BroughtCourse_Model_List {

    public String id;
    public String Title;
    public String FromDate;
    public String ToDate;
    public String DaysLeft;


    public String getId() {

        return id;
    }

    public void setId(String Id) {

        this.id = Id;
    }


    public String getTitle() {

        return Title;
    }

    public void setTitle(String Titlename) {

        this.Title = Titlename;
    }

    public String getFromDate() {

        return FromDate;
    }

    public void setFromDate(String fromDate) {

        this.FromDate = fromDate;
    }

    public String getToDate() {

        return ToDate;
    }

    public void setToDate(String toDate) {

        this.ToDate = toDate;
    }

    public String getDaysLeft() {

        return DaysLeft;
    }

    public void setDaysLeft(String daysLeft) {

        this.DaysLeft = daysLeft;
    }

}
