package com.supriya.whoami.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.supriya.whoami.R;
import com.supriya.whoami.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    AutoCompleteTextView autoCompleteTextViewUsertype;
    String[] usertype = new String[]{"Patient", "Guardian", "Doctor"};
    public static long maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        autoCompleteTextViewUsertype = findViewById(R.id.AutoCompleteTextviewUsertype);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.dropdown_item, usertype);
        autoCompleteTextViewUsertype.setAdapter(adapter1);
        autoCompleteTextViewUsertype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + autoCompleteTextViewUsertype.getText().toString(), Toast.LENGTH_SHORT).show();
                if (autoCompleteTextViewUsertype.getText().toString().trim().equals(usertype[0])){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new PatientRegisterFragment()).commit();
                }
                else if (autoCompleteTextViewUsertype.getText().toString().trim().equals(usertype[1])){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new GuardianRegisterFragment()).commit();
                }
                else if (autoCompleteTextViewUsertype.getText().toString().trim().equals(usertype[2])){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new DoctorRegisterFragment()).commit();

                }
                else {
                    autoCompleteTextViewUsertype.setError("select valid field");
                }
            }
        });

        getSupportActionBar().hide();

//        deviceId = Settings.Secure.getString(RegistrationActivity.this.getContentResolver(),Settings.Secure.ANDROID_ID);
//        auth = FirebaseAuth.getInstance();
//
//        dialog = new ProgressDialog(RegistrationActivity.this);
//        dialog.setCancelable(false);
//        dialog.setMessage("Loading.....");
//


//        autoCompleteTextViewgender = findViewById(R.id.AutoCompleteTextviewGender);
//        String[] gender = new String[]{"Male", "Female", "Other"};
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.dropdown_item, gender);
//        autoCompleteTextViewgender.setAdapter(adapter2);
//
//        autoCompleteTextViewgender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "" + autoCompleteTextViewgender.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        binding.editTextDOB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectDate();
//            }
//        });
//
//        binding.editTextReenterPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (passStr != confirmPassStr){
//                    binding.editTextReenterPassword.setError("Recheck Password");
//                }
//                return;
//            }
//        });
//
//        signUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
////                startActivity(i);
//                if (!validatePhoneNo()){
//                    binding.editTextContact.setError("Verify Mobile No");
//                }
//                if (binding.editTextContact.length()>10){
//                    binding.editTextContact.setError("Mobile No is Invalid");
//                }
//                registerUser();
//            }
//        });
//
//        singin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
//                startActivity(i);
//            }
//        });

    }

//    private void selectDate() {
//        final Calendar c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//        showDialog(DATE_PICKER_ID);
//    }
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id){
//            case DATE_PICKER_ID:
//                return new DatePickerDialog(this,pickerListener,year,month,day);
//            default:
//                throw new IllegalStateException("Unexpected value: " + id);
//        }
//    }
//
//    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
//            year = selectedYear;
//            month = selectedMonth;
//            day = selectedDay;
//
//            binding.editTextDOB.setText(new StringBuilder().append(month+1)
//            .append("-").append(day).append("-").append(year).append(" "));
//        }
//    };
//
//    private Boolean validateEmail(){
//        String val = binding.editTextMail.getText().toString();
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//        if (val.isEmpty()){
//            binding.editTextMail.setError("Field Cannot be empty");
//            return false;
//        } else if (!val.matches(emailPattern)){
//            binding.editTextMail.setError("Invalid email address");
//            return false;
//        } else {
//            binding.editTextMail.setError(null);
//            return true;
//        }
//    }
//
//    private Boolean validatePhoneNo(){
//        String val = binding.editTextContact.getText().toString();
//        if (val.isEmpty()){
//            binding.editTextContact.setError("Field cannot be empty");
//            return false;
//        }else{
//            binding.editTextContact.setError(null);
//            return true;
//        }
//    }
//
//    private Boolean validatePassword(){
//        String val = binding.editTextPassword.getText().toString();
//        String passwordVal = "^"+
//                "(?=.*[a-zA-Z])"+
//                "(?=.*[@#$%^&+=])"+
//                "(?=\\S+$)"+
//                ".{4,}"+
//                "$";
//        if (val.isEmpty()){
//            binding.editTextPassword.setError("Field cannot be empty");
//            return false;
//        }else if (!val.matches(passwordVal)){
//            binding.editTextPassword.setError("Password is too weak");
//            return false;
//        }else {
//            binding.editTextPassword.setError(null);
//            return true;
//        }
//    }
//
//    private Boolean validateConfirmPassword(){
//        String val = binding.editTextReenterPassword.getText().toString();
//        String passwordVal = "^"+
//                "(?=.*[a-zA-Z])"+
//                "(?=.*[@#$%^&+=])"+
//                "(?=\\S+$)"+
//                ".{4,}"+
//                "$";
//        if (val.isEmpty()){
//            binding.editTextReenterPassword.setError("Field cannot be empty");
//            return false;
//        } else if (!val.matches(passwordVal)){
//            binding.editTextReenterPassword.setError("Password is too weak");
//            return false;
//        } else {
//            binding.editTextReenterPassword.setError(null);
//            return true;
//        }
//    }
//
//    private void registerUser() {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserData");
//        final FirebaseUser user = auth.getCurrentUser();
//        if (!validateEmail() || !validatePhoneNo() || !validatePassword() || !validateConfirmPassword()){
//            return;
//        }
//        userTypeStr = binding.AutoCompleteTextviewUsertype.getEditableText().toString();
//        nameStr = binding.editTextName.getEditableText().toString();
//        emailStr = binding.editTextMail.getEditableText().toString();
//        phoneStr = binding.editTextContact.getEditableText().toString();
//        dobStr = binding.editTextDOB.getEditableText().toString();
//        genderStr = binding.AutoCompleteTextviewGender.getEditableText().toString();
//        locationStr = binding.editTextLocation.getEditableText().toString();
//        passStr = binding.editTextPassword.getEditableText().toString();
//        confirmPassStr = binding.editTextReenterPassword.getEditableText().toString();
//
//        Query query = reference.orderByChild("deviceId").equalTo(deviceId);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    Toast.makeText(RegistrationActivity.this, "Device already registered with us", Toast.LENGTH_SHORT).show();
//                }else {
//                    if (validate()){
//                        signUpUser(user,userTypeStr,nameStr,emailStr,phoneStr,dobStr,genderStr,locationStr,passStr);
//                        dialog.show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

//    private void signUpUser(FirebaseUser user,String userTypeStr, String nameStr, String emailStr, String phoneStr, String dobStr,String genderStr, String locationStr, String passStr) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserData");
//        auth.createUserWithEmailAndPassword(emailStr,passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    dialog.dismiss();
//                    String userId = auth.getCurrentUser().getUid();
//                    HashMap<String,Object> map = new HashMap<>();
//
//                    map.put("UserType",userTypeStr);
//                    map.put("Email",emailStr);
//                    map.put("Name",nameStr);
//                    map.put("Mobile",phoneStr);
//                    map.put("DOB",dobStr);
//                    map.put("Gender",genderStr);
//                    map.put("Location",locationStr);
//                    map.put("Password",passStr);
//                    map.put("userId",userId);
//
//                    reference.child(userId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(RegistrationActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private boolean validate(){
//        if (passStr.compareTo(confirmPassStr) != 0){
//            Toast.makeText(RegistrationActivity.this, "Password not Same", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
    
}