package com.example.amita.simplycs.Adapter;

public class TestListDataAdapter {

    public String id;
    public String Title;
    public String Duration;
    public String Marks;
    public String Rules;


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

    public String getDuration(){

        return Duration;
    }

    public void setDuration(String Testduration)
    {
        this.Duration=Testduration;
    }

    public String getMarks(){

        return Marks;
    }

    public void setMarks(String Testmarks)
    {
        this.Marks=Testmarks;
    }

    public String getRules(){

        return Rules;
    }

    public void setRules(String Testrules)
    {
        this.Rules=Testrules;
    }

}
