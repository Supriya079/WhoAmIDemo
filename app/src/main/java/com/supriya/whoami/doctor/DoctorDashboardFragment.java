package com.supriya.whoami.doctor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.supriya.whoami.AccessStorage;
import com.supriya.whoami.R;
import com.supriya.whoami.login.LoginActivity;
import com.supriya.whoami.patient.PatientDashboardFragment;
import com.supriya.whoami.patient.PatientEmergencyFragment;
import com.supriya.whoami.patient.Person;
import com.supriya.whoami.patient.todolist.ToDoListActivity;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.PatientListEntity;
import com.supriya.whoami.room.RelationEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.io.File;
import java.util.List;
import java.util.Set;

public class DoctorDashboardFragment extends Fragment {

    TextView no_data;
    ImageView empty_imageview;
    DataDAO dataDAO;
    Bitmap bmImg;
    RecyclerView patientListRecycler;
    FloatingActionButton fabAddPatients;
    RecyclerAdapter recyclerAdapter;
    String patientName,patientMobile;
    int patientId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_doctor_dashboard_fragment,container,false);

        empty_imageview = view.findViewById(R.id.empty_imageview);
        no_data = view.findViewById(R.id.no_data);
        patientListRecycler = view.findViewById(R.id.patientRecyclerviewD);
        fabAddPatients = view.findViewById(R.id.fabAddPatientD);

        dataDAO = WhoAmIDB.getInstance(getContext()).dataDAO();
        recyclerAdapter = new RecyclerAdapter(dataDAO.getAllPatientsList());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        if (recyclerAdapter.getItemCount() !=0 ){
            patientListRecycler.setLayoutManager(gridLayoutManager);
            patientListRecycler.setAdapter(recyclerAdapter);
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }else {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }

        fabAddPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Patients.class));
            }
        });

        return view;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        List<PatientListEntity> data;

        public RecyclerAdapter(List<PatientListEntity> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_design_patientlist,parent,false);
            return new RecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            PatientListEntity patientListEntity = data.get(position);
            holder.personN.setText(patientListEntity.getPatientListName());
            patientName = patientListEntity.getPatientListName();
            patientMobile = patientListEntity.getPatientListMobileNo();
            patientId = patientListEntity.getPatientListId();
            File roomReturnedPath = new File(patientListEntity.getPatientListImg());
            if (roomReturnedPath.getName().endsWith(".pdf"))
            {
                bmImg  = AccessStorage.pdfToBitmap(roomReturnedPath);
            }
            else if (roomReturnedPath.getName().endsWith(".jpg")){
                bmImg = BitmapFactory.decodeFile(patientListEntity.getPatientListImg());
            }
            holder.imageViewPerson.setImageBitmap(bmImg);

            holder.patientlistmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUpMenu(view,position);
                }
            });

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
                    Toast.makeText(getContext(), "patient data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void showPopUpMenu(View view, int position) {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.doctorplistmenu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menuAssignAppointment:
                            //DoctorDashboardFragment doctorDashboardFragment = new DoctorDashboardFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("namePA",patientName);
                            bundle.putString("phone",patientMobile);

                            SetAppointment setAppointment = new SetAppointment();
                            setAppointment.setArguments(bundle);

                            getFragmentManager().beginTransaction().add(R.id.fragment_container_doctor,setAppointment).commit();
//
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            fragmentManager.beginTransaction().replace(R.id.fragment_container_doctor,setAppointment).addToBackStack(DoctorDashboardFragment.class.getSimpleName()).commit();

                            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_doctor, doctorDashboardFragment).addToBackStack(DoctorDashboardFragment.class.getSimpleName()).commit();
                            Toast.makeText(getContext(), "Appointment", Toast.LENGTH_SHORT).show();
                            break;

//                        case R.id.menuMedicine:
//                            Toast.makeText(getContext(), "Medicine", Toast.LENGTH_SHORT).show();
//                            break;

                        case R.id.menuDeletePatient:
                            DataDAO dataDAO = WhoAmIDB.getInstance(getContext()).dataDAO();
                            dataDAO.deletePatientById(patientId);
                            getFragmentManager().beginTransaction().add(R.id.fragment_container_doctor,new DoctorDashboardFragment()).commit();
                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout recyclerLayout;
            ImageView imageViewPerson;
            ImageButton patientlistmenu;
            TextView personN;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                personN = itemView.findViewById(R.id.namePatientList);
                imageViewPerson = itemView.findViewById(R.id.grid_image_patientList);
                recyclerLayout = itemView.findViewById(R.id.recyclerlayoutPatientList);
                patientlistmenu = itemView.findViewById(R.id.patientListMenu);
            }
        }

    }

}
