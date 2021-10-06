package com.example.mbulancedriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mbulancedriver.databinding.ActivityHomeBinding;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth mAuth;
    SharedPreferences sp;
    String online,num,substr,dLat,dLng;
    FirebaseFirestore db;
    Handler handler;
    Runnable mToastRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        handler = new Handler();

        switchingStuff();

        mToastRunnable = new Runnable() {
            @Override
            public void run() {

                num= sp.getString("num", "");

                handler.postDelayed(this, 3000);

            }
        };

        mToastRunnable.run();

        if (num.startsWith("+")) {

            handler.removeCallbacks(mToastRunnable);

            callToTrack(num);

        }


    }

    private void callToTrack(String value) {

        substr = value.substring(value.length() - 9);

        binding.progressBar.setVisibility(View.VISIBLE);

        db.collection("users").whereEqualTo("phone",substr).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : task.getResult()) {

                    String name = document.getData().get("uName").toString();
                    dLat = document.getData().get("latitude").toString();
                    dLng = document.getData().get("longitude").toString();

                    binding.editTextTextPersonName.setText("Name: "+name);
                    binding.editTextPhone.setText("Phone: "+value);

                    binding.cardView.setVisibility(View.VISIBLE);

                    binding.progressBar.setVisibility(View.INVISIBLE);

                }

            }

        });

    }

    public void close(View view) {

        if(num.startsWith("+")){

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("last",num);
            editor.putString("num", "nothing");
            editor.apply();

        }

        binding.cardView.setVisibility(View.GONE);

        mToastRunnable.run();

    }

    public void getDirection(View view) {

        try {

            Uri gmmIntentUri = Uri.parse("google.navigation:q="+dLat+","+dLng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        } catch (Exception exception) {

            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps"));
            startActivity(i);

        }



    }

    public void lastCalled(View view) {

        if (num.startsWith("+")) {

            handler.removeCallbacks(mToastRunnable);
            callToTrack(num);

        } else {

            String last= sp.getString("last", "");
            callToTrack(last);

            handler.removeCallbacks(mToastRunnable);

        }

    }

    private void switchingStuff() {

        online= sp.getString("online", "");
        if (online.contains("yes")) {

            binding.switch1.setChecked(true);

        }

        binding.switch1.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {

                startService(new Intent(this,LocationService.class));
                Toast.makeText(this, "You are online", Toast.LENGTH_SHORT).show();

            } else {

                stopService(new Intent(this,LocationService.class));

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout:

                mAuth.signOut();

                if (online.contains("yes")) {

                    stopService(new Intent(this,LocationService.class));

                }

                Intent intent=new Intent(Home.this,MainActivity.class);
                startActivity(intent);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}