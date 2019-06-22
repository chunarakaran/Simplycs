package com.aaddya.amita.simplycs.Model;

public class Webinar_Model_List {

    public String id;
    public String Title;
    public String VideoURL;
    public String duration;


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

    public String getVideoURL() {

        return VideoURL;
    }

    public void setVideoURL(String URL) {

        this.VideoURL = URL;
    }

    public String getDuration() {

        return duration;
    }

    public void setDuration(String dr) {

        this.duration = dr;
    }


//    @Override
//    public String toString() {
//        return "Video_Model_List{" +
//                "VideoURL='" + VideoURL + '\'' +
//                ", Title='" + Title + '\'' +
//                ", duration='" + duration + '\'' +
//                '}';
//    }

}
