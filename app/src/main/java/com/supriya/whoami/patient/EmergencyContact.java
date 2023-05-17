package com.supriya.whoami.patient;

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
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.EmergencyCntEntity;
import com.supriya.whoami.room.WhoAmIDB;

public class EmergencyContact extends Fragment {

    EditText personNameE,personRelationE,personContactE;
    Button btnSavePersonE;
    DataDAO dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_emergency_contact,container,false);
        personNameE = view.findViewById(R.id.editPersonNameE);
        personRelationE = view.findViewById(R.id.editPersonRelationE);
        personContactE = view.findViewById(R.id.editPersonContact);
        btnSavePersonE = view.findViewById(R.id.personBtn);

        dao = WhoAmIDB.getInstance(getActivity().getApplicationContext()).dataDAO();

        btnSavePersonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personNameE.getText().toString().isEmpty() || personRelationE.getText().toString().isEmpty()
                        || personContactE.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),
                            "Data missing",
                            Toast.LENGTH_SHORT).show();
                }else {
                    EmergencyCntEntity emergencyCntEntity = new EmergencyCntEntity();
                    emergencyCntEntity.setPersonNameE(personNameE.getText().toString());
                    emergencyCntEntity.setPersonRelationE(personRelationE.getText().toString());
                    emergencyCntEntity.setPersonCnt(personContactE.getText().toString());
                    dao.insertEmergencyCnt(emergencyCntEntity);
//                    final Intent i = new Intent(getActivity().getApplicationContext(), PatientEmergencyFragment.class);
//                    startActivity(i);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_patient, new PatientEmergencyFragment()).commit();
                }
            }
        });

        return view;
    }
}
