package com.supriya.whoami.doctor;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.supriya.whoami.AccessStorage;
import com.supriya.whoami.R;
import com.supriya.whoami.patient.PatientNavigationActivity;
import com.supriya.whoami.patient.Person;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.PatientListEntity;
import com.supriya.whoami.room.RelationEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.io.IOException;

public class Patients extends AppCompatActivity {

    EditText patientName,patientMobileNumber,patientImg;
    Button btnSavePatient;
    DataDAO dao;
    Dialog dialogM;
    Uri uri = null,intentCameraUri=null;
    private static final int CAMERA = 9;
    private static final int GALLERY = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        patientImg = findViewById(R.id.editPatientImg);
        patientName = findViewById(R.id.editPatientName);
        patientMobileNumber = findViewById(R.id.editPatientMobileNumber);
        btnSavePatient = findViewById(R.id.patientBtn);

        dao = WhoAmIDB.getInstance(getApplicationContext()).dataDAO();

        btnSavePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (patientName.getText().toString().isEmpty() || patientMobileNumber.getText().toString().isEmpty()
                        || patientImg.getHint().toString().isEmpty()){
                    Toast.makeText(Patients.this,
                            "Data missing",
                            Toast.LENGTH_SHORT).show();
                }else {
                    PatientListEntity patientListEntity = new PatientListEntity();
                    patientListEntity.setPatientListName(patientName.getText().toString());
                    patientListEntity.setPatientListMobileNo(patientMobileNumber.getText().toString());
                    patientImg.setHintTextColor(Color.BLACK);
                    patientListEntity.setPatientListImg(patientImg.getHint().toString());
                    dao.insertPatientToDoctorList(patientListEntity);
                    final Intent i = new Intent(getApplicationContext(), DoctorNavigationActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"ObsoleteSdkInt", "UseCompatLoadingForDrawables", "QueryPermissionsNeeded"})
    public void takeImage(View view) {
        //Create Dialog
        dialogM = new Dialog(Patients.this);
        dialogM.setContentView(R.layout.custom_dialog_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogM.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        Window window = dialogM.getWindow();
        window.setGravity(Gravity.BOTTOM);

        dialogM.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogM.setCancelable(false); //Optional
        dialogM.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        dialogM.show();

        ImageView camera = dialogM.findViewById(R.id.cameraOp);
        ImageView gallery = dialogM.findViewById(R.id.galleryOp);

        camera.setOnClickListener(v -> {
            dialogM.dismiss();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager())!= null){
                Uri imagePath = AccessStorage.createCameraImage(this);
                if (imagePath != null){
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imagePath);
                    startActivityForResult(intent,CAMERA);
                    intentCameraUri = intent.getData();
                }
            }

        });

        gallery.setOnClickListener(v -> {
            dialogM.dismiss();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (intent.resolveActivity(getPackageManager())!= null){
                startActivityForResult(Intent.createChooser(intent,"Title"),GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK ){
            String camerapath = AccessStorage.getImgPath(intentCameraUri,this);// it will return the Capture image path
            patientImg.setHint(camerapath);

            Log.d("CP",camerapath);
            Toast.makeText(Patients.this, "Picture Selected from Camera", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == GALLERY && resultCode == Activity.RESULT_OK ) {
            assert data != null;
            uri = data.getData();
            Toast.makeText(Patients.this, "Picture Selected from Gallery", Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitDemo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                String gallPath = AccessStorage.saveImageToGallery(bitDemo, this);
                patientImg.setHint(gallPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(Patients.this, "Picture Not Selected ", Toast.LENGTH_SHORT).show();
        }
    }

}