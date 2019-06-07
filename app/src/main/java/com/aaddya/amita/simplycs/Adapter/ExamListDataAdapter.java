package com.aaddya.amita.simplycs.Adapter;

public class ExamListDataAdapter {

    public String id;
    public String ImageURL;
    public String ExamTitle;
    public String ExamDesc;
    public String ExamDate;
    public String Exam_start_Time;
    public String Exam_end_Time;


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

    public String getExamTitle() {

        return ExamTitle;
    }

    public void setExamTitle(String CourseTitleName) {

        this.ExamTitle = CourseTitleName;
    }

    public String getexamDesc() {

        return ExamDesc;
    }

    public void setExamDesc(String courseDesc) {

        this.ExamDesc = courseDesc;
    }

    public String getExamDate() {

        return ExamDate;
    }

    public void setExamDate(String coursePrice) {

        this.ExamDate = coursePrice;
    }

    public String getExam_start_Time() {

        return Exam_start_Time;
    }

    public void setExam_start_Time(String courseDiscount) {

        this.Exam_start_Time = courseDiscount;
    }

    public String getExam_end_Time() {

        return Exam_end_Time;
    }

    public void setExam_end_Time(String courseHours) {

        this.Exam_end_Time = courseHours;
    }

}
