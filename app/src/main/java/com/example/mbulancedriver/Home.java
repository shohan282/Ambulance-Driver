package com.example.mbulancedriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mbulancedriver.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;


public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        startService(new Intent(this,Service.class));

        mAuth = FirebaseAuth.getInstance();

        binding.logout.setOnClickListener(v -> {

            mAuth.signOut();
            Intent intent = new Intent(Home.this,MainActivity.class);
            startActivity(intent);

        });

    }
}