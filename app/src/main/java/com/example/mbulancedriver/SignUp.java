package com.example.mbulancedriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.mbulancedriver.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseAuth mAuth;
    ActivitySignUpBinding binding;
    String email,password,name,uid,organization,phn,type,AC,cOVID;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);

        binding.signUp.setOnClickListener(v -> {

            email = binding.editTextTextEmailAddress.getText().toString();
            password = binding.editTextTextPassword.getText().toString();
            name = binding.editTextTextPersonName.getText().toString();
            organization = binding.editTextTextPersonOrganization.getText().toString();
            phn = binding.editTextPhone.getText().toString();
            type = binding.spinner.getSelectedItem().toString();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {

                                uid=mAuth.getCurrentUser().getUid();

                                Map<String, String> user = new HashMap<>();
                                user.put("uName", name);
                                user.put("uid", uid);
                                user.put("email", email);
                                user.put("phn", phn);
                                user.put("type", type);
                                user.put("AC", AC);
                                user.put("cOVID",cOVID);
                                user.put("organization",organization);

                                db.collection("driver").document(uid).set(user).addOnSuccessListener(aVoid -> { })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(SignUp.this, "name error", Toast.LENGTH_SHORT).show());

                                Intent intent=new Intent(SignUp.this,MainActivity.class);
                                startActivity(intent);

                            } else {

                                Toast.makeText(SignUp.this, "failed: "+task.getException(), Toast.LENGTH_SHORT).show();

                            }

                        });

            }
        });

    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.COVID_checkBox:
                if (checked)
                cOVID = "yes";
                break;
            case R.id.AC_checkBox:
                if (checked)
                AC = "yes";
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();
        if(TextUtils.equals(text,"Freezer Ambulance") || TextUtils.equals(text,"Select type")) {

            binding.checkbox.setVisibility(View.GONE);

        } else {

            binding.checkbox.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}