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

    // Save relation data for patient memory
    @Insert
    void insertPersonData(RelationEntity relationEntity);

    @Query("SELECT * FROM RelationEntity")
    List<RelationEntity> getAllPerson();

    @Query("SELECT personId FROM RELATIONENTITY WHERE personName = :name")
    boolean isRelationNameExist(String name);

    // save person contacts
    @Insert
    void insertEmergencyCnt(EmergencyCntEntity emergencyCntEntity);

    @Query("SELECT * FROM EMERGENCYCNTENTITY")
    List<EmergencyCntEntity> getAllEmergencyCnt();

    // save doctor contacts for guardian
    @Insert
    void insertDoctorCnt(DoctorCntEntity doctorCntEntity);

    @Query("SELECT * FROM DOCTORCNTENTITY")
    List<DoctorCntEntity> getAllDoctorCnt();

    // save patient list for doctor module
    @Insert
    void insertPatientToDoctorList(PatientListEntity patientListEntity);

    @Query("SELECT * FROM PATIENTLISTENTITY")
    List<PatientListEntity> getAllPatientsList();

    @Query("SELECT patientListMobileNo FROM PATIENTLISTENTITY WHERE patientListName = :pName")
    int isPatientDataExist(String pName);

    @Query("DELETE FROM PatientListEntity WHERE patientListId = :id")
    void deletePatientById(int id);

    // reminder
    @Insert
    void insertAll(ReminderEntity entityClass);

    @Query("SELECT * FROM REMINDERENTITY")
    List<ReminderEntity> getAllData();

    @Query("DELETE FROM REMINDERENTITY WHERE id = :id")
    void deleteById(int id);

}
