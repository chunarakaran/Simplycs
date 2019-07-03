package com.aaddya.amita.simplycs.Model;

public class Quetion_Model_List {

    private String quetion_id,question,scale,op1,op2,op3,op4,answer;

    int selctedId;

    public Quetion_Model_List(String id, String quetion_id, String question, String scale, String op1, String op2, String op3, String op4) {
        this.quetion_id = quetion_id;
        this.question = question;
        this.scale=scale;
        this.op1 = op1;
        this.op2 = op2;
        this.op3= op3;
        this.op4 = op4;
        this.answer = answer;

    }

    public int getSelctedId(){
        return selctedId;
    }

    public void setSelctedId(int selctedId){
        this.selctedId=selctedId;
    }


    public String getQuetion_id() {
        return quetion_id;
    }

    public void setQuetion_id(String quetion_id) {
        this.quetion_id = quetion_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
