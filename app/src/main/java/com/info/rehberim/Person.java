package com.info.rehberim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Person {
    private int personId;
    private String personName;
    private String personTelNumber;
    private String personEmail;
    private Bitmap personImage;

    public Person() {
    }

    public Person(int personId,String personName, String personTelNumber, String personEmail, Bitmap personImage) {
        this.personId = personId;
        this.personName = personName;
        this.personTelNumber = personTelNumber;
        this.personEmail = personEmail;
        this.personImage = personImage;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonTelNumber() {
        return personTelNumber;
    }

    public void setPersonTelNumber(String personTelNumber) {
        this.personTelNumber = personTelNumber;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public Bitmap getPersonImage() {
        return personImage;
    }

    public void setPersonImage(Bitmap personImage) {
        this.personImage = personImage;
    }
}
