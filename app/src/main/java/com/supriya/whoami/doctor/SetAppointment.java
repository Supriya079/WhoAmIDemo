package com.supriya.whoami.doctor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.supriya.whoami.R;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.PatientListEntity;
import com.supriya.whoami.room.ReminderEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.util.ArrayList;
import java.util.Calendar;

public class SetAppointment extends Fragment {

    EditText editMessage,editDate;
    Button btnSendAppointment;
    int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    TextView nameOfPatient;
    String receivedName,receivedPhone,appointmentMessage, date;
    WhoAmIDB databaseClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_set_appointment,container,false);
        editDate = view.findViewById(R.id.appointmentDate);
        editMessage = view.findViewById(R.id.appointmentMessage);
        btnSendAppointment = view.findViewById(R.id.btnAddAppointment);
        nameOfPatient = view.findViewById(R.id.nameOfPatientAppointment);

        databaseClass = WhoAmIDB.getInstance(getContext().getApplicationContext());

        receivedName = this.getArguments().getString("namePA");
        receivedPhone = this.getArguments().getString("phone");
        nameOfPatient.setText(receivedName);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            editDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickerDialog.dismiss();
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        btnSendAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentMessage = editMessage.getText().toString();
                date = editDate.getText().toString();
                Toast.makeText(getContext(), "text: "+appointmentMessage, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "date: "+date, Toast.LENGTH_SHORT).show();

                // send message
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> parts = smsManager.divideMessage("Hey"+" "+ receivedName +" "+ appointmentMessage +" "+ "on" +" "+ date);
                smsManager.sendTextMessage("+91"+receivedPhone,null, String.valueOf(parts).replace("[", "").replace("]", ""),null,null);
                Toast.makeText(getContext(), "Message Sent", Toast.LENGTH_SHORT).show();

                // reminder
                ReminderEntity entityClass = new ReminderEntity();
                String value = (appointmentMessage);
                String d = (date);
                String time = ("8:00 AM");
                entityClass.setEventdate(d);
                entityClass.setEventname(value);
                entityClass.setEventtime(time);
                databaseClass.dataDAO().insertAll(entityClass);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_doctor,new DoctorDashboardFragment()).commit();
            }
        });

        return view;

    }
}
