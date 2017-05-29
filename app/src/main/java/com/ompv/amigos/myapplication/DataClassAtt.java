package com.ompv.amigos.myapplication;


import android.net.Uri;

/**
 * Created by yessine on 24/03/17.
 */

public class DataClassAtt {

    private String firstName;
    private int mIdUser ;
    private String email;
    private String password;
    private String mPhone;

    private String mimage;

    public int getmIdUser() {
        return mIdUser;
    }

    public void setmIdUser(int mIdUser) {
        this.mIdUser = mIdUser;
    }

    public DataClassAtt(String firstName, int mIdUser, String email, String password, String phone,String mimage) {
        this.firstName = firstName;
        this.mIdUser = mIdUser;
        this.email = email;
        this.mPhone = phone;
        this.password = password;
        this.mimage = mimage;
    }

    public DataClassAtt(String firstName, String email, String mimage) {

        this.firstName = firstName;
        this.email = email;
        this.mimage = mimage;
    }

    public DataClassAtt() {

    }


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

    @Override
    public String toString() {
        return "DataClassAtt{" +
                "firstName='" + firstName + '\'' +
                ", mIdUser=" + mIdUser +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mimage='" + mimage + '\'' +
                '}';
    }
}
