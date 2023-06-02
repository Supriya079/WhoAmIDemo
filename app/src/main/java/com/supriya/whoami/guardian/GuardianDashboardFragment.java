package com.supriya.whoami.guardian;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.supriya.whoami.R;
import com.supriya.whoami.guardian.reminder.CreateEventG;
import com.supriya.whoami.guardian.reminder.EventAdapterG;
import com.supriya.whoami.room.ReminderEntity;
import com.supriya.whoami.room.WhoAmIDB;
import com.supriya.whoami.setReminder.CreateEvent;

import java.util.List;

public class GuardianDashboardFragment extends Fragment {

    TextView no_data,home,doctorCnt,location;
    ImageView empty_imageview;
    WhoAmIDB whoAmIDB;
    EventAdapterG eventAdapter;
    RecyclerView reminderRecycler;
    FloatingActionButton fabAddReminder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_guardian_dashboard_fragment,container,false);

        whoAmIDB = WhoAmIDB.getInstance(getContext());
        empty_imageview = view.findViewById(R.id.empty_imageview);
        no_data = view.findViewById(R.id.no_data);
        reminderRecycler = view.findViewById(R.id.reminderRecyclerviewG);
        fabAddReminder = view.findViewById(R.id.fabAddReminderG);
        home = view.findViewById(R.id.homeBottomG);
        doctorCnt = view.findViewById(R.id.doctorCntG);
        //location = view.findViewById(R.id.locationBottomG);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_guardian, new GuardianDashboardFragment()).commit();
            }
        });

        doctorCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_guardian, new GuardianDoctorCntFragment()).commit();
//                startActivity(new Intent(getContext(),PatientEmergencyFragment.class));
            }
        });

//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_guardian, new GuardianDashboardFragment()).commit();
//            }
//        });

        fabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateEventG.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
    }

    private void setAdapter() {
        List<ReminderEntity> classList = whoAmIDB.dataDAO().getAllData();
        eventAdapter = new EventAdapterG(getContext(), classList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        if (eventAdapter.getItemCount() !=0 ){
            reminderRecycler.setLayoutManager(gridLayoutManager);
            reminderRecycler.setAdapter(eventAdapter);
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }else {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }
        reminderRecycler.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        reminderRecycler.setAdapter(eventAdapter);
    }

}