package com.aaddya.amita.simplycs.Model;

public class SubjectiveQues_Model_List {

    public String id;
    public String question;
    public String scale;
    public String explanation;
    public String dropzone;


    public String getId() {

        return id;
    }

    public void setId(String Id) {

        this.id = Id;
    }


    public String getQuestion() {

        return question;
    }

    public void setQuestion(String Question) {

        this.question = Question;
    }

    public String getScale() {

        return scale;
    }

    public void setScale(String Scale) {

        this.scale = Scale;
    }

    public String getExplanation(){
        return explanation;
    }

    public void setExplanation(String Explanation){
        this.explanation=Explanation;
    }

    public String getDropzone(){
        return dropzone;
    }

    public void setDropzone(String Dropzone){
        this.dropzone=Dropzone;
    }

}
