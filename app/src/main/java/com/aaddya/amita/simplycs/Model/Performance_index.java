package com.aaddya.amita.simplycs.Model;

import java.io.Serializable;

public class Performance_index implements Serializable {
    public String id;
    public String Test_id;
    public String user_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTest_id() {
        return Test_id;
    }

    public void setTest_id(String test_id) {
        Test_id = test_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getP_index() {
        return P_index;
    }

    public void setP_index(String p_index) {
        P_index = p_index;
    }

    public String getTest_name() {
        return Test_name;
    }

    public void setTest_name(String test_name) {
        Test_name = test_name;
    }

    public String P_index;
    public String Test_name;
}
