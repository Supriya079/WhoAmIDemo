package com.supriya.whoami.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PersonDataEntity {

    @PrimaryKey(autoGenerate = true)
    int personId;

    String personName;
    String personRelation;
    String personImg;

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

    public String getPersonRelation() {
        return personRelation;
    }

    public void setPersonRelation(String personRelation) {
        this.personRelation = personRelation;
    }

    public String getPersonImg() {
        return personImg;
    }

    public void setPersonImg(String personImg) {
        this.personImg = personImg;
    }
}
