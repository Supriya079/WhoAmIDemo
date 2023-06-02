package com.supriya.whoami.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.supriya.whoami.databinding.ActivityForgetPasswordBinding;

import org.w3c.dom.Text;

public class ForgetPasswordActivity extends AppCompatActivity {

    ActivityForgetPasswordBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(ForgetPasswordActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        binding.forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassword();
            }
        });

        getSupportActionBar().hide();

    }
    private Boolean validateEmail(){
        String val = binding.email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()){
            binding.email.setError("Field Cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)){
            binding.email.setError("Invalid email address");
            return false;
        } else {
            binding.email.setError(null);
            return true;
        }
    }

    private void forgetPassword() {

        if (!validateEmail()){
            return;
        }
        dialog.show();
        auth.sendPasswordResetEmail(binding.email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (task.isSuccessful()){
                    startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                    finish();
                    Toast.makeText(ForgetPasswordActivity.this, "Please Check Your Email Address!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter correct email ID", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}