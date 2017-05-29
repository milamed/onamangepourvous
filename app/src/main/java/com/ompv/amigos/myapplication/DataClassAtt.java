package com.ompv.amigos.myapplication;


import android.net.Uri;

/**
 * Created by yessine on 24/03/17.
 */

public class DataClassAtt {

    private String firstName;

    private String email;
    private String password;
    private String mimage;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.email = password;
    }


    public String getMimage() {
        return mimage;
    }

    public void setMimage(String mimage) {
        this.mimage = mimage;
    }

    public DataClassAtt(String firstName, String email, String mimage) {

        this.firstName = firstName;
        this.email = email;
        this.mimage = mimage;
    }

    public DataClassAtt() {

    }

}
