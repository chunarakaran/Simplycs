package com.aaddya.amita.simplycs.Model;

public class Course_Model_List {

    public String id;
    public String ImageURL;
    public String CourseTitle;
    public String CourseDesc;
    public String CoursePrice;
    public String CourseDiscount;
    public String CourseStartDate;


    public String getId() {

        return id;
    }

    public void setId(String Id) {

        this.id = Id;
    }


    public String getImageUrl() {

        return ImageURL;
    }

    public void setImageUrl(String imageServerUrl) {

        this.ImageURL = imageServerUrl;
    }

    public String getCourseTitle() {

        return CourseTitle;
    }

    public void setCourseTitle(String CourseTitleName) {

        this.CourseTitle = CourseTitleName;
    }

    public String getCourseDesc() {

        return CourseDesc;
    }

    public void setCourseDesc(String courseDesc) {

        this.CourseDesc = courseDesc;
    }

    public String getCoursePrice() {

        return CoursePrice;
    }

    public void setCoursePrice(String coursePrice) {

        this.CoursePrice = coursePrice;
    }

    public String getCourseDiscount() {

        return CourseDiscount;
    }

    public void setCourseDiscount(String courseDiscount) {

        this.CourseDiscount = courseDiscount;
    }

    public String getCourseStartDate() {

        return CourseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {

        this.CourseStartDate = courseStartDate;
    }

}
