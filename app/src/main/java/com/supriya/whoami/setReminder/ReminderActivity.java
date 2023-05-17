package com.supriya.whoami.setReminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.supriya.whoami.R;
import com.supriya.whoami.patient.PatientEmergencyFragment;
import com.supriya.whoami.patient.PatientNavigationActivity;
import com.supriya.whoami.patient.todolist.ToDoListActivity;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.ReminderEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.util.List;

public class ReminderActivity extends AppCompatActivity {

    TextView no_data,home,todo,emergency,location;
    ImageView empty_imageview;
    WhoAmIDB whoAmIDB;
    EventAdapter eventAdapter;
    RecyclerView reminderRecycler;
    FloatingActionButton fabAddReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        whoAmIDB = WhoAmIDB.getInstance(this);

        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
        reminderRecycler = findViewById(R.id.reminderRecyclerview);
        fabAddReminder = findViewById(R.id.fabAddReminder);
        home = findViewById(R.id.homeBottom);
        todo = findViewById(R.id.todoBottom);
        emergency = findViewById(R.id.emergencyBottom);
        location = findViewById(R.id.locationBottom);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this, PatientNavigationActivity.class));
            }
        });

        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this, ToDoListActivity.class));
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_patient, new PatientEmergencyFragment()).commit();
//                startActivity(new Intent(getContext(),PatientEmergencyFragment.class));
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this,PatientNavigationActivity.class));
            }
        });

        fabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateEvent.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    private void setAdapter() {
        List<ReminderEntity> classList = whoAmIDB.dataDAO().getAllData();
        eventAdapter = new EventAdapter(getApplicationContext(), classList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ReminderActivity.this,1,GridLayoutManager.VERTICAL,false);
        if (eventAdapter.getItemCount() !=0 ){
            reminderRecycler.setLayoutManager(gridLayoutManager);
            reminderRecycler.setAdapter(eventAdapter);
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }else {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }
        reminderRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        reminderRecycler.setAdapter(eventAdapter);
    }


}