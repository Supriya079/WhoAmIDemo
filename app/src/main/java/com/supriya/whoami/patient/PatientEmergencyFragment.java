package com.supriya.whoami.patient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.supriya.whoami.R;
import com.supriya.whoami.patient.todolist.ToDoListActivity;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.EmergencyCntEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.util.List;

public class PatientEmergencyFragment extends Fragment {

    TextView no_data,home,todo,emergency,location;
    ImageView empty_imageview;
    RecyclerView emergencyRecycler;
    FloatingActionButton fabAddEmergencyPhone;
    DataDAO dataDAO;
    RecyclerAdapter recyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_patient_emergency_fragment,container,false);
        empty_imageview = view.findViewById(R.id.empty_imageview);
        no_data = view.findViewById(R.id.no_data);
        emergencyRecycler = view.findViewById(R.id.phoneRecyclerview);
        fabAddEmergencyPhone = view.findViewById(R.id.fabAddEmergencyCnt);
        home = view.findViewById(R.id.homeBottom);
        todo = view.findViewById(R.id.todoBottom);
        emergency = view.findViewById(R.id.emergencyBottom);
        location = view.findViewById(R.id.locationBottom);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),PatientNavigationActivity.class));
            }
        });

        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ToDoListActivity.class));
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_patient, new PatientEmergencyFragment()).commit();
//                startActivity(new Intent(getContext(),PatientEmergencyFragment.class));
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),PatientNavigationActivity.class));
            }
        });

        dataDAO = WhoAmIDB.getInstance(getContext()).dataDAO();
        recyclerAdapter = new RecyclerAdapter(dataDAO.getAllEmergencyCnt());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        if (recyclerAdapter.getItemCount() !=0 ){
            emergencyRecycler.setLayoutManager(gridLayoutManager);
            emergencyRecycler.setAdapter(recyclerAdapter);
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }else {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }


        fabAddEmergencyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_patient, new EmergencyContact()).addToBackStack(PatientEmergencyFragment.class.getSimpleName()).commit();
            }
        });

        return view;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        List<EmergencyCntEntity> data;

        public RecyclerAdapter(List<EmergencyCntEntity> emergencyCntEntities) {
            this.data = emergencyCntEntities;
        }

        @NonNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_design_emergency,parent,false);
            return new RecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
            EmergencyCntEntity emergencyCntEntity = data.get(position);
            holder.personNE.setText(emergencyCntEntity.getPersonNameE());
            holder.personRE.setText(emergencyCntEntity.getPersonRelationE());
            holder.personCntE.setText(emergencyCntEntity.getPersonCnt());

            holder.call_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(),DetailsActivity.class);
//                    intent.putExtra("name", billEntity.getObject_Name());;
//                    startActivity(intent);
                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse(emergencyCntEntity.getPersonCnt()));
                    String n = emergencyCntEntity.getPersonCnt();
                    intent.setData(Uri.parse("tel:"+n));
                    startActivity(intent);
                    Toast.makeText(getContext(), "Call", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView call_image;
            TextView personNE,personRE,personCntE;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                personNE = itemView.findViewById(R.id.namePE);
                personRE = itemView.findViewById(R.id.relationPE);
                personCntE = itemView.findViewById(R.id.contactPE);
                call_image = itemView.findViewById(R.id.call_image);
            }
        }
    }

}
