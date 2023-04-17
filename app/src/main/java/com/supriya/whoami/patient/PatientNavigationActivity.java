package com.supriya.whoami.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.supriya.whoami.R;
import com.supriya.whoami.databinding.ActivityPatientNavigationBinding;
import com.supriya.whoami.login.LoginActivity;

public class PatientNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityPatientNavigationBinding binding;
    ProgressDialog dialog;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(PatientNavigationActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        auth = FirebaseAuth.getInstance();

        NavigationView navigationView = findViewById(R.id.navigation_view_patient);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientDashboardFragment());
        mDrawerLayout = findViewById(R.id.patientDrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.start,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientDashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientDashboardFragment()).commit();
                break;
            case R.id.emergency:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientEmergencyFragment()).commit();
                break;
            case R.id.homeLocation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientDashboardFragment()).commit();
                break;
            case R.id.to_do_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientDashboardFragment()).commit();
                break;
            case R.id.reminder:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PatientDashboardFragment()).commit();
                break;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(PatientNavigationActivity.this, LoginActivity.class));
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
