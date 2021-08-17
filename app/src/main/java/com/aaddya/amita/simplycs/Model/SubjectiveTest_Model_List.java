package com.aaddya.amita.simplycs.Model;

public class SubjectiveTest_Model_List {

    public String id;
    public String Title;
    public String Rules;
    public String isComplete;
    public String total_question;


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


    public String getRules(){

        return Rules;
    }

    public void setRules(String Testrules)
    {
        this.Rules=Testrules;
    }

    public String getIsComplete(){

        return isComplete;
    }

    public void setIsComplete(String isCompleted)
    {
        this.isComplete=isCompleted;
    }

    public String getTotal_question(){

        return total_question;
    }

    public void setTotal_question(String Total_question)

    {
        this.total_question=Total_question;
    }
}
