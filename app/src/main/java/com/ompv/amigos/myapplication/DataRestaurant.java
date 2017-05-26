package com.ompv.amigos.myapplication;

/**
 * Created by evaneos on 26/05/17.
 */

public class DataRestaurant {
    private int mIdRestaurant;
    private String mPassword;
    private String mMail;
    private String mName;
    private float mLongt;
    private float mLat;
    private String mVille;
    private float mNote;
    private String mPhotos;
    private String mCategory;
    private String mSpecialty;
    private String mServices;
    private String mPayment;
    private String mPhone;

    public DataRestaurant(String mPassword, String mMail, String mName, float mLongt, float mLat, String mVille, float mNote, String mPhotos, String mCategory, String mSpecialty, String mServices, String mPayment, String mPhone) {
        this.mPassword = mPassword;
        this.mMail = mMail;
        this.mName = mName;
        this.mLongt = mLongt;
        this.mLat = mLat;
        this.mVille = mVille;
        this.mNote = mNote;
        this.mPhotos = mPhotos;
        this.mCategory = mCategory;
        this.mSpecialty = mSpecialty;
        this.mServices = mServices;
        this.mPayment = mPayment;
        this.mPhone = mPhone;
    }

    public DataRestaurant() {

    }

    public int getmIdRestaurant() {

        return mIdRestaurant;
    }

    public void setmIdRestaurant(int mIdRestaurant) {
        this.mIdRestaurant = mIdRestaurant;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmMail() {
        return mMail;
    }

    public void setmMail(String mMail) {
        this.mMail = mMail;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public float getmLongt() {
        return mLongt;
    }

    public void setmLongt(float mLongt) {
        this.mLongt = mLongt;
    }

    public float getmLat() {
        return mLat;
    }

    public void setmLat(float mLat) {
        this.mLat = mLat;
    }

    public String getmVille() {
        return mVille;
    }

    public void setmVille(String mVille) {
        this.mVille = mVille;
    }

    public float getmNote() {
        return mNote;
    }

    public void setmNote(float mNote) {
        this.mNote = mNote;
    }

    public String getmPhotos() {
        return mPhotos;
    }

    public void setmPhotos(String mPhotos) {
        this.mPhotos = mPhotos;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmSpecialty() {
        return mSpecialty;
    }

    public void setmSpecialty(String mSpecialty) {
        this.mSpecialty = mSpecialty;
    }

    public String getmServices() {
        return mServices;
    }

    public void setmServices(String mServices) {
        this.mServices = mServices;
    }

    public String getmPayment() {
        return mPayment;
    }

    public void setmPayment(String mPayment) {
        this.mPayment = mPayment;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }
}
