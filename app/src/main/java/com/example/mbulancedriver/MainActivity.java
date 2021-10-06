package com.example.mbulancedriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mbulancedriver.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ActivityMainBinding binding;
    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {

            binding.button2.setOnClickListener(v -> {

                email = binding.editTextTextEmailAddress.getText().toString();
                password = binding.editTextTextPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    Intent intent=new Intent(MainActivity.this,Home.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    Toast.makeText(MainActivity.this, "failed: "+task.getException(), Toast.LENGTH_SHORT).show();

                                }

                            });

                }

            });

            binding.button3.setOnClickListener(v -> {

                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

            });

        } else {

            Intent intent=new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();

        }

    }
}