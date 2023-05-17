package com.supriya.whoami.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PatientListEntity {

    @PrimaryKey(autoGenerate = true)
    int patientListId;

    String patientListName;
    String patientListMobileNo;
    String patientListImg;

    public String getPatientListMobileNo() {
        return patientListMobileNo;
    }

    public void setPatientListMobileNo(String patientListMobileNo) {
        this.patientListMobileNo = patientListMobileNo;
    }

    public int getPatientListId() {
        return patientListId;
    }

    public void setPatientListId(int patientListId) {
        this.patientListId = patientListId;
    }

    public String getPatientListName() {
        return patientListName;
    }

    public void setPatientListName(String patientListName) {
        this.patientListName = patientListName;
    }

    public String getPatientListImg() {
        return patientListImg;
    }

    public void setPatientListImg(String patientListImg) {
        this.patientListImg = patientListImg;
    }

}
