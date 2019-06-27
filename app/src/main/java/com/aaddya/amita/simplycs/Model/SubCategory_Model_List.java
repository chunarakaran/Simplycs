package com.aaddya.amita.simplycs.Model;

public class SubCategory_Model_List {

    public String id;
    public String ImageURL;
    public String ImageTitle;
    boolean isNew;


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

    public String getImageTitle() {

        return ImageTitle;
    }

    public void setImageTitle(String Imagetitlename) {

        this.ImageTitle = Imagetitlename;
    }

}
