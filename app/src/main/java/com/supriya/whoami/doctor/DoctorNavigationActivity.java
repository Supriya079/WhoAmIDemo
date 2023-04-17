package com.supriya.whoami.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.supriya.whoami.R;
import com.supriya.whoami.databinding.ActivityDoctorNavigationBinding;
import com.supriya.whoami.databinding.ActivityGuardianNavigationBinding;
import com.supriya.whoami.databinding.ActivityPatientNavigationBinding;
import com.supriya.whoami.guardian.GuardianDashboardFragment;
import com.supriya.whoami.guardian.GuardianNavigationActivity;

public class DoctorNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityDoctorNavigationBinding binding;
    ProgressDialog dialog;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(DoctorNavigationActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        NavigationView navigationView = findViewById(R.id.navigation_view_doctor);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GuardianDashboardFragment());
        mDrawerLayout = findViewById(R.id.doctorDrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.start,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GuardianDashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DoctorDashboardFragment()).commit();
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
