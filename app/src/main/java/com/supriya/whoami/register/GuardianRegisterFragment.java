package com.supriya.whoami.register;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.supriya.whoami.DatePickerFragment;
import com.supriya.whoami.login.LoginActivity;
import com.supriya.whoami.R;

import java.util.Calendar;
import java.util.HashMap;

public class GuardianRegisterFragment extends Fragment {

    AutoCompleteTextView autoCompleteTextViewgender;
    String[] gender = new String[]{"Male", "Female", "Other"};
    EditText textDOB, repass, editContact, editmail, editname, editlocation, editpass;
    Button signUp;
    TextView singin;
    ProgressDialog dialog;
    FirebaseAuth auth;
    DatabaseReference ref;

    private String userTypeStr, nameStr, emailStr, phoneStr, dobStr, genderStr, locationStr, passStr, confirmPassStr;
    private String deviceId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_guardian_register_fragment, container, false);

        deviceId = Settings.Secure.getString(GuardianRegisterFragment.this.getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        auth = FirebaseAuth.getInstance();
        textDOB = view.findViewById(R.id.editTextDOBG);
        repass = view.findViewById(R.id.editTextReenterPasswordG);
        signUp = view.findViewById(R.id.buttonSignUpG);
        singin = view.findViewById(R.id.signintxtG);
        editContact = view.findViewById(R.id.editTextContactG);
        editmail = view.findViewById(R.id.editTextMailG);
        editname = view.findViewById(R.id.editTextNameG);
        editlocation = view.findViewById(R.id.editTextLocationG);
        editpass = view.findViewById(R.id.editTextPasswordG);
        autoCompleteTextViewgender = view.findViewById(R.id.AutoCompleteTextviewGenderG);

        dialog = new ProgressDialog(requireContext());
        dialog.setCancelable(false);
        dialog.setMessage("Loading.....");

        autoCompleteTextViewgender = view.findViewById(R.id.AutoCompleteTextviewGenderG);

        ref = FirebaseDatabase.getInstance().getReference().child("UserData");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    RegistrationActivity.maxId = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, gender);
        autoCompleteTextViewgender.setAdapter(adapter2);

        autoCompleteTextViewgender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "" + autoCompleteTextViewgender.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        textDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        repass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passStr != confirmPassStr) {
                    repass.setError("Recheck Password");
                }
                return;
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePhoneNo()) {
                    editContact.setError("Verify Mobile No");
                }
                if (editContact.length() > 10) {
                    editContact.setError("Mobile No is Invalid");
                }
                registerUser();
            }
        });

        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        return view;

    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            textDOB.setText(day + "-" + month + "-" + year);
        }
    };

    private Boolean validateEmail() {
        String val = editmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            editmail.setError("Field Cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            editmail.setError("Invalid email address");
            return false;
        } else {
            editmail.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = editContact.getText().toString();
        if (val.isEmpty()) {
            editContact.setError("Field cannot be empty");
            return false;
        } else {
            editContact.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = editpass.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if (val.isEmpty()) {
            editpass.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            editpass.setError("Password is too weak");
            return false;
        } else {
            editpass.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String val = repass.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if (val.isEmpty()) {
            repass.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            repass.setError("Password is too weak");
            return false;
        } else {
            repass.setError(null);
            return true;
        }
    }

    private void registerUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserData");
        final FirebaseUser user = auth.getCurrentUser();
        if (!validateEmail() || !validatePhoneNo() || !validatePassword() || !validateConfirmPassword()) {
            return;
        }
        userTypeStr = "Guardian";
        nameStr = editname.getEditableText().toString();
        emailStr = editmail.getEditableText().toString();
        phoneStr = editContact.getEditableText().toString();
        dobStr = textDOB.getEditableText().toString();
        genderStr = autoCompleteTextViewgender.getEditableText().toString();
        locationStr = editlocation.getEditableText().toString();
        passStr = editpass.getEditableText().toString();
        confirmPassStr = repass.getEditableText().toString();

        Query query = reference.orderByChild("deviceId").equalTo(deviceId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "Device already registered with us", Toast.LENGTH_SHORT).show();
                } else {
                    if (validate()) {
                        signUpUser(user, userTypeStr, nameStr, emailStr, phoneStr, dobStr, genderStr, locationStr, passStr);
                        dialog.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(getContext(), "not able to save data to firebase", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void signUpUser(FirebaseUser user, String userTypeStr, String nameStr, String emailStr, String phoneStr, String dobStr, String genderStr, String locationStr, String passStr) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserData");
        auth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    String userId = auth.getCurrentUser().getUid();
                    HashMap<String, Object> map = new HashMap<>();

                    map.put("UserType", userTypeStr);
                    map.put("Email", emailStr);
                    map.put("Name", nameStr);
                    map.put("Mobile", phoneStr);
                    map.put("DOB", dobStr);
                    map.put("Gender", genderStr);
                    map.put("Location", locationStr);
                    map.put("Password", passStr);
                    map.put("userId", userId);

                    reference.child(String.valueOf(RegistrationActivity.maxId + 1)).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Register Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private boolean validate() {
        if (passStr.compareTo(confirmPassStr) != 0) {
            Toast.makeText(getContext(), "Password not Same", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
