package com.supriya.whoami.patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.supriya.whoami.AccessStorage;
import com.supriya.whoami.R;
import com.supriya.whoami.login.LoginActivity;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.PatientEntity;
import com.supriya.whoami.room.PersonDataEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.io.File;
import java.util.List;

public class PatientDashboardFragment extends Fragment {

    Button logout;
    FirebaseAuth auth;
    TextView username, no_data,home,todo,emergency,location;
    ImageView dashboardImg,empty_imageview;
    String getName,getPath;
    Bitmap bmImg;
    DataDAO dataDAO;
    RecyclerView personRecycler;
    FloatingActionButton fabAddPerson;
    RecyclerAdapter recyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_patient_dashboard_fragment,container,false);
        logout = view.findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        username = view.findViewById(R.id.userName);
        dashboardImg = view.findViewById(R.id.imageViewdashboard);
        empty_imageview = view.findViewById(R.id.empty_imageview);
        no_data = view.findViewById(R.id.no_data);
        personRecycler = view.findViewById(R.id.personRecyclerview);
        fabAddPerson = view.findViewById(R.id.fabAddPerson);
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
                startActivity(new Intent(getContext(),PatientNavigationActivity.class));
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),PatientEmergencyFragment.class));
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),PatientNavigationActivity.class));
            }
        });

        dataDAO = WhoAmIDB.getInstance(getContext()).dataDao();
        recyclerAdapter = new RecyclerAdapter(dataDAO.getAllPerson());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        if (recyclerAdapter.getItemCount() !=0 ){
            personRecycler.setLayoutManager(gridLayoutManager);
            personRecycler.setAdapter(recyclerAdapter);
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }else {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }

        fabAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Person.class));
            }
        });

//        getName = getActivity().getIntent().getStringExtra("pn");
//        getPath = getActivity().getIntent().getStringExtra("path");
//        username.setText(getName);
//        File roomReturnedPath = new File(getPath);
//        if (roomReturnedPath.getName().endsWith(".pdf"))
//        {
//            bmImg  = AccessStorage.pdfToBitmap(roomReturnedPath);
//        }
//        else if (roomReturnedPath.getName().endsWith(".jpg")){
//            bmImg = BitmapFactory.decodeFile(getPath);
//        }
//        dashboardImg.setImageBitmap(bmImg);

        return view;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        List<PersonDataEntity> data;

        public RecyclerAdapter(List<PersonDataEntity> personDataEntities) {
            this.data = personDataEntities;
        }

        @NonNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_design,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
            PersonDataEntity personDataEntity = data.get(position);
            holder.personN.setText(personDataEntity.getPersonName());
            holder.personR.setText(personDataEntity.getPersonRelation());
            File roomReturnedPath = new File(personDataEntity.getPersonImg());
            if (roomReturnedPath.getName().endsWith(".pdf"))
            {
                bmImg  = AccessStorage.pdfToBitmap(roomReturnedPath);
            }
            else if (roomReturnedPath.getName().endsWith(".jpg")){
                bmImg = BitmapFactory.decodeFile(personDataEntity.getPersonImg());
            }
            holder.imageViewPerson.setImageBitmap(bmImg);

            holder.recyclerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(),DetailsActivity.class);
//                    intent.putExtra("name", billEntity.getObject_Name());
//                    intent.putExtra("location", billEntity.getLocation());
//                    intent.putExtra("image", billEntity.getImagePath());
//                    intent.putExtra("description",billEntity.getDescription());
//                    intent.putExtra("dob",billEntity.getDate().toString());
//                    startActivity(intent);
                    Toast.makeText(getContext(), "Data clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout recyclerLayout;
            ImageView imageViewPerson;
            TextView personN,personR;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                personN = itemView.findViewById(R.id.nameP);
                personR = itemView.findViewById(R.id.relationP);
                imageViewPerson = itemView.findViewById(R.id.grid_image);
                recyclerLayout = itemView.findViewById(R.id.recyclerlayout);
            }
        }
    }

}
