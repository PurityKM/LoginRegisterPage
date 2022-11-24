package com.example.myloginregisterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();
        TextView v = findViewById(R.id.dispNm);

        String username = USER.getDisplayName();
        String email = USER.getEmail();

        String display;

        if (username == null) {
          display = "Hi, " + email;
        } else {
            display = "Hi, " + username;
        }

        v.setText("Hi, " + email);
    }

    public void logout(View v) {
        Button b = (Button) v;

       try {

           // Logout
           FirebaseAuth.getInstance().signOut();

           startActivity(new Intent(this, LoginActivity.class));


       } catch (Exception e) {
           Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }

}