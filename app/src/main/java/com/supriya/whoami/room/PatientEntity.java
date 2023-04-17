package com.supriya.whoami.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PatientEntity {

    @PrimaryKey(autoGenerate = true)
    int patientId;

    String patientName;
    String patientImg;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientImg() {
        return patientImg;
    }

    public void setPatientImg(String patientImg) {
        this.patientImg = patientImg;
    }
}
