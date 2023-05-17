package com.supriya.whoami.guardian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.supriya.whoami.R;
import com.supriya.whoami.patient.PatientEmergencyFragment;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.DoctorCntEntity;
import com.supriya.whoami.room.EmergencyCntEntity;
import com.supriya.whoami.room.WhoAmIDB;

public class GuardianDoctorContact extends Fragment {

    EditText doctorName,doctorContact;
    Button btnSaveDoctorCnt;
    DataDAO dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_guardian_doctor_cnt,container,false);

        doctorName = view.findViewById(R.id.editDoctorNameG);
        doctorContact = view.findViewById(R.id.editDoctorContactG);
        btnSaveDoctorCnt = view.findViewById(R.id.doctorCntBtnG);

        dao = WhoAmIDB.getInstance(getActivity().getApplicationContext()).dataDAO();

        btnSaveDoctorCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doctorName.getText().toString().isEmpty() || doctorContact.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),
                            "Data missing",
                            Toast.LENGTH_SHORT).show();
                }else {
                    DoctorCntEntity doctorCntEntity = new DoctorCntEntity();
                    doctorCntEntity.setDoctorName(doctorName.getText().toString());
                    doctorCntEntity.setDoctorCnt(doctorContact.getText().toString());
                    dao.insertDoctorCnt(doctorCntEntity);
//                    final Intent i = new Intent(getActivity().getApplicationContext(), PatientEmergencyFragment.class);
//                    startActivity(i);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_guardian, new GuardianDoctorCntFragment()).commit();
                }
            }
        });

        return view;
    }
}
