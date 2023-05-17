package com.supriya.whoami.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EmergencyCntEntity {

    @PrimaryKey(autoGenerate = true)
    int cntId;

    String personNameE;
    String personRelationE;
    String personCnt;

    public int getCntId() {
        return cntId;
    }

    public void setCntId(int cntId) {
        this.cntId = cntId;
    }

    public String getPersonNameE() {
        return personNameE;
    }

    public void setPersonNameE(String personNameE) {
        this.personNameE = personNameE;
    }

    public String getPersonRelationE() {
        return personRelationE;
    }

    public void setPersonRelationE(String personRelationE) {
        this.personRelationE = personRelationE;
    }

    public String getPersonCnt() {
        return personCnt;
    }

    public void setPersonCnt(String personCnt) {
        this.personCnt = personCnt;
    }
}
