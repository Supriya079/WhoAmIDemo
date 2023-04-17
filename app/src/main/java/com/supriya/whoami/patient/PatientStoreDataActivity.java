package com.supriya.whoami.patient;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.supriya.whoami.AccessStorage;
import com.supriya.whoami.R;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.PatientEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientStoreDataActivity extends AppCompatActivity {

    EditText patientName,patientPathImg;
    Button btnSave;
    Dialog dialogM;
    DataDAO dataDAO;
    String imagePath,pname;
    Uri intentCameraUri=null,uri = null;
    private static final int CAMERA = 9;
    private static final int GALLERY = 7;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_store_data);
        patientName = findViewById(R.id.editTextStoreName);
        patientPathImg = findViewById(R.id.editTextStoreImagePath);
        btnSave = findViewById(R.id.savedata);

        checkAndRequestPermissions();

        dataDAO = WhoAmIDB.getInstance(getApplicationContext()).dataDao();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (patientName.getText().toString().isEmpty()){
                    Toast.makeText(PatientStoreDataActivity.this, "Data Missing", Toast.LENGTH_SHORT).show();
                }else {
                    pname = patientName.getText().toString();
                    PatientEntity patientEntity = new PatientEntity();
                    patientEntity.setPatientName(patientName.getText().toString());
                    patientPathImg.setHintTextColor(Color.BLACK);
                    patientEntity.setPatientImg(imagePath);
                    dataDAO.insertPatientData(patientEntity);
                    Intent intent = new Intent(PatientStoreDataActivity.this,PatientNavigationActivity.class);
                    intent.putExtra("pn",pname);
                    intent.putExtra("path",imagePath);
                    startActivity(intent);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"ObsoleteSdkInt", "UseCompatLoadingForDrawables", "QueryPermissionsNeeded"})
    public void takeImage(View view) {
        //Create Dialog
        dialogM = new Dialog(PatientStoreDataActivity.this);
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
            patientPathImg.setHint(camerapath);
            imagePath  = camerapath;
            Log.d("CP",camerapath);
            Toast.makeText(PatientStoreDataActivity.this, "Picture Selected from Camera", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == GALLERY && resultCode == Activity.RESULT_OK ) {
            assert data != null;
            uri = data.getData();
            Toast.makeText(PatientStoreDataActivity.this, "Picture Selected from Gallery", Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitDemo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                String gallPath = AccessStorage.saveImageToGallery(bitDemo, this);
                imagePath = gallPath;
                patientPathImg.setHint(gallPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        else if (requestCode == FILES && resultCode == Activity.RESULT_OK){
//            Uri content_describer = data.getData();
//            String src = content_describer.getPath();
//            source = new File(src);
//            Log.d("src is ", source.toString());
//            String filename = content_describer.getLastPathSegment();
//            Log.d("FileName is ",filename);
//            destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ filename);
//            Log.d("Destination is ", destination.toString());
//            intentFileUri = Uri.parse(filename);
//            sourceFilePath = AccessStorage.getSourceFilePath(this,intentFileUri);
//            Log.d("getP",""+sourceFilePath);
//            String filePath = AccessStorage.saveFileToStorage(this,sourceFilePath);
//            mediaEdittext.setHint(filePath);
//        }
        else {
            Toast.makeText(PatientStoreDataActivity.this, "Picture Not Selected ", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int writepermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readpermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("Service Permissions are required for this app")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {
                    //  permissionsclass.requestPermission(type,code);
                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")));
                })
                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> finish());
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Permission callback called-------");
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for permissions
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "read and write services permission granted");

                    //else any one or all the permissions are not granted
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                    // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showDialogOK(
                                (dialog, which) -> {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            // proceed with logic by disabling the related features or quit the app.
                                            finish();
                                            break;
                                    }
                                });
                    }
                    //permission is denied (and never ask again is  checked)
                    //shouldShowRequestPermissionRationale will return false
                    else {
                        explain();
                    }
                }
            }
        }

    }

}