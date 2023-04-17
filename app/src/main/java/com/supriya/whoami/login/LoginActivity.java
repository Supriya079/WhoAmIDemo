package com.supriya.whoami.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supriya.whoami.doctor.DoctorNavigationActivity;
import com.supriya.whoami.guardian.GuardianNavigationActivity;
import com.supriya.whoami.patient.PatientNavigationActivity;
import com.supriya.whoami.R;
import com.supriya.whoami.register.RegistrationActivity;
import com.supriya.whoami.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String emailStr, passwordStr, userType;
    private GoogleSignInClient signInClient;
    TextView signup;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signup = findViewById(R.id.signuptxt);
        autoCompleteTextView = findViewById(R.id.AutoCompleteTextview);
        String[] usertype = new String[]{"Patient", "Guardian", "Doctor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, usertype);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        signInClient = GoogleSignIn.getClient(this,gso);
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");

//        if (auth.getCurrentUser() != null){
//            startActivity(new Intent(LoginActivity.this, PatientNavigationActivity.class));
//            FirebaseUser user = auth.getCurrentUser();
//
//            Log.d("xyz", "onAuthStateChanged:signed_in:" + user.getUid());
//
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserData");
//            System.out.println(reference);
//            reference.orderByChild("UserType").equalTo("Patient").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.getValue() != null){
//                        startActivity(new Intent(LoginActivity.this, PatientNavigationActivity.class));
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//
//            if (ut.equals("Patient")){
//                startActivity(new Intent(LoginActivity.this, PatientNavigationActivity.class));
//            }else if (ut.equals("Guardian")){
//                startActivity(new Intent(LoginActivity.this, GuardianNavigationActivity.class));
//            }else if (ut.equals("Doctor")){
//                startActivity(new Intent(LoginActivity.this, DoctorNavigationActivity.class));
//            }else {
//                Toast.makeText(LoginActivity.this, "Check Category", Toast.LENGTH_SHORT).show();
//            }
//
//            finish();
//        }

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        binding.txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).hide();

    }

    private Boolean validateEmail(){
        String val = binding.editTextId.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()){
            binding.editTextId.setError("Field Cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)){
            binding.editTextId.setError("Invalid email address");
            return false;
        } else {
            binding.editTextId.setError(null);
            return true;
        }
    }
    private Boolean validatePassword(){
        String val = binding.editTextPass.getText().toString();
        String passwordVal = "^"+
                "(?=.*[a-zA-Z])"+
                "(?=.*[@#$%^&+=])"+
                "(?=\\S+$)"+
                ".{4,}"+
                "$";
        if (val.isEmpty()){
            binding.editTextPass.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(passwordVal)){
            binding.editTextPass.setError("Password is too weak");
            return false;
        }else {
            binding.editTextPass.setError(null);
            return true;
        }
    }

    private void loginUser() {
        if (!validateEmail() || !validatePassword()){
            return;
        }
        else {
            userType = binding.AutoCompleteTextview.getText().toString();
            emailStr = binding.editTextId.getText().toString();
            passwordStr = binding.editTextPass.getText().toString();
            dialog.show();
            loginUsers(emailStr,passwordStr);
        }
    }

    private void loginUsers( String emailStr, String passwordStr) {
        auth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    //startActivity(new Intent(LoginActivity.this, PatientNavigationActivity.class));
                    if (userType.equals("Patient")){
                        startActivity(new Intent(LoginActivity.this, PatientNavigationActivity.class));
                    }else if (userType.equals("Guardian")){
                        startActivity(new Intent(LoginActivity.this, GuardianNavigationActivity.class));
                    }else if (userType.equals("Doctor")){
                        startActivity(new Intent(LoginActivity.this, DoctorNavigationActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Check Category", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = auth.getCurrentUser();
//        //updateUI(currentUser);
//    }

}