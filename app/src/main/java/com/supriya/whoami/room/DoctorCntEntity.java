package com.supriya.whoami.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DoctorCntEntity {

    @PrimaryKey(autoGenerate = true)
    int docCntId;

    String doctorName;
    String doctorCnt;

    public int getDocCntId() {
        return docCntId;
    }

    public void setDocCntId(int docCntId) {
        this.docCntId = docCntId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorCnt() {
        return doctorCnt;
    }

    public void setDoctorCnt(String doctorCnt) {
        this.doctorCnt = doctorCnt;
    }
}
