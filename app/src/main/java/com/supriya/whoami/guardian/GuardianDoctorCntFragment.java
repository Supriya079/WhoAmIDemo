package com.supriya.whoami.guardian;

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
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.DoctorCntEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.util.List;

public class GuardianDoctorCntFragment extends Fragment {

    TextView no_data,home,doctorCnt,location;
    ImageView empty_imageview;
    RecyclerView doctorCntRecycler;
    FloatingActionButton fabAddDoctorPhone;
    DataDAO dataDAO;
    RecyclerAdapter recyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_guardian_doctorcnt_fragment,container,false);
        empty_imageview = view.findViewById(R.id.empty_imageview);
        no_data = view.findViewById(R.id.no_data);
        doctorCntRecycler = view.findViewById(R.id.doctorCntRecyclerview);
        fabAddDoctorPhone = view.findViewById(R.id.fabAddDoctorCntG);
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

        fabAddDoctorPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_guardian, new GuardianDoctorContact()).addToBackStack(GuardianDoctorCntFragment.class.getSimpleName()).commit();
            }
        });

        dataDAO = WhoAmIDB.getInstance(getContext()).dataDAO();
        recyclerAdapter = new RecyclerAdapter(dataDAO.getAllDoctorCnt());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        if (recyclerAdapter.getItemCount() !=0 ){
            doctorCntRecycler.setLayoutManager(gridLayoutManager);
            doctorCntRecycler.setAdapter(recyclerAdapter);
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }else {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        List<DoctorCntEntity> data;

        public RecyclerAdapter(List<DoctorCntEntity> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_design_doctor,parent,false);
            return new RecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

            DoctorCntEntity doctorCntEntity = data.get(position);
            holder.doctorName.setText(doctorCntEntity.getDoctorName());
            holder.doctorContact.setText(doctorCntEntity.getDoctorCnt());

            holder.call_image_D.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(),DetailsActivity.class);
//                    intent.putExtra("name", billEntity.getObject_Name());;
//                    startActivity(intent);
                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse(emergencyCntEntity.getPersonCnt()));
                    String n = doctorCntEntity.getDoctorCnt();
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
            ImageView call_image_D;
            TextView doctorName,doctorContact;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                doctorName = itemView.findViewById(R.id.nameDC);
                doctorContact = itemView.findViewById(R.id.contactDC);
                call_image_D = itemView.findViewById(R.id.call_image_DC);

            }
        }
    }

}
