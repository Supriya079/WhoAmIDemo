package com.supriya.whoami.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataDAO {

    // Save Patient data
    @Insert
    void insertPatientData(PatientEntity patientEntity);

    @Query("SELECT patientName,patientImg FROM PatientEntity WHERE patientName = :pName")
    int isDataExist(String pName);

    // Save Person data for patient memory
    @Insert
    void insertPersonData(PersonDataEntity personDataEntity);

    @Query("SELECT * FROM PERSONDATAENTITY")
    List<PersonDataEntity> getAllPerson();

}
