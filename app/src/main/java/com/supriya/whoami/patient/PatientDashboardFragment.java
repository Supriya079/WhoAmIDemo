package com.supriya.whoami.patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.supriya.whoami.patient.todolist.ToDoListActivity;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.RelationEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.io.File;
import java.util.List;

public class PatientDashboardFragment extends Fragment {

    Button logout;
    FirebaseAuth auth;
    TextView username, no_data,home,todo,emergency,location;
    ImageView dashboardImg,empty_imageview;
    Bitmap bmImg;
    DataDAO dataDAO;
    RecyclerView personRecycler;
    FloatingActionButton fabAddPerson, fabImageRecognize;
    RecyclerAdapter recyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_patient_dashboard_fragment,container,false);
        logout = view.findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
//        username = view.findViewById(R.id.userName);
//        dashboardImg = view.findViewById(R.id.imageViewdashboard);
        empty_imageview = view.findViewById(R.id.empty_imageview);
        no_data = view.findViewById(R.id.no_data);
        personRecycler = view.findViewById(R.id.personRecyclerview);
        fabAddPerson = view.findViewById(R.id.fabAddPerson);
        fabImageRecognize = view.findViewById(R.id.fbImageRecognize);
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
                startActivity(new Intent(getContext(),PatientHomeLocation.class));
            }
        });

        dataDAO = WhoAmIDB.getInstance(getContext()).dataDAO();
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

        fabImageRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FaceRecognize.class));
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
//        dashboardImg.setImageBitmap(bmIm
        return view;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        List<RelationEntity> data;

        public RecyclerAdapter(List<RelationEntity> personDataEntities) {
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
            RelationEntity relationEntity = data.get(position);
            holder.personN.setText(relationEntity.getPersonName());
            holder.personR.setText(relationEntity.getPersonRelation());
            holder.personC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    String n = relationEntity.getPersonNumber();
                    intent.setData(Uri.parse("tel:"+n));
                    startActivity(intent);
                    Toast.makeText(getContext(), "Call", Toast.LENGTH_SHORT).show();
                }
            });
            File roomReturnedPath = new File(relationEntity.getPersonImg());
            if (roomReturnedPath.getName().endsWith(".pdf"))
            {
                bmImg  = AccessStorage.pdfToBitmap(roomReturnedPath);
            }
            else if (roomReturnedPath.getName().endsWith(".jpg")){
                bmImg = BitmapFactory.decodeFile(relationEntity.getPersonImg());
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
            ImageView imageViewPerson,personC;
            TextView personN,personR;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                personN = itemView.findViewById(R.id.nameP);
                personR = itemView.findViewById(R.id.relationP);
                personC = itemView.findViewById(R.id.contactP);
                imageViewPerson = itemView.findViewById(R.id.grid_image);
                recyclerLayout = itemView.findViewById(R.id.recyclerlayout);
            }
        }
    }

}


//<androidx.cardview.widget.CardView
//        android:id="@+id/cardView"
//        android:layout_width="match_parent"
//        android:layout_height="100dp"
//        android:layout_margin="20dp"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toTopOf="parent">
//
//<androidx.constraintlayout.widget.ConstraintLayout
//        android:layout_width="match_parent"
//        android:layout_height="match_parent">
//
//<ImageView
//                android:id="@+id/imageViewdashboard"
//                        android:layout_width="80dp"
//                        android:layout_height="80dp"
//                        android:layout_margin="20dp"
//                        android:src="@drawable/ic_profileimg"
//                        app:layout_constraintBottom_toBottomOf="parent"
//                        app:layout_constraintStart_toStartOf="parent"
//                        app:layout_constraintTop_toTopOf="parent" />
//
//<TextView
//                android:id="@+id/userName"
//                        android:layout_width="wrap_content"
//                        android:layout_height="wrap_content"
//                        android:layout_marginTop="36dp"
//                        android:text="User Name"
//                        android:textSize="22dp"
//                        android:textStyle="bold"
//                        app:layout_constraintEnd_toEndOf="parent"
//                        app:layout_constraintHorizontal_bias="0.198"
//                        app:layout_constraintStart_toEndOf="@+id/imageViewdashboard"
//                        app:layout_constraintTop_toTopOf="parent" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//</androidx.cardview.widget.CardView>