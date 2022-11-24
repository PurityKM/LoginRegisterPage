package com.example.myloginregisterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Intent loginPage, homePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        loginPage = new Intent(this, LoginActivity.class);
        homePage = new Intent(this, Home.class);


        if (user != null) {
            startActivity(homePage);
        } else {
            startActivity(loginPage);
        }

        // Prevent going back
        finish();


    }
}