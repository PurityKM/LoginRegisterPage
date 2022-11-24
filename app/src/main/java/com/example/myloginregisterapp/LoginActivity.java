package com.example.myloginregisterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Intent homePage;
    private TextView email, password;
    private Button loginBtn;

    FirebaseAuth mAuth;

    private boolean hasErrorsUsername, hasErrorsPassword = Boolean.TRUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        loginBtn = findViewById(R.id.btnLogin);

        homePage = new Intent(this, Home.class);

        mAuth = FirebaseAuth.getInstance();

        TextView btn= findViewById(R.id.txtSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction(loginBtn, email.getText().toString(), password.getText().toString());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    email.setError(null);
                    hasErrorsUsername = Boolean.FALSE;
                } else {
                    hasErrorsUsername = Boolean.TRUE;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    email.setError(null);
                    hasErrorsPassword = Boolean.FALSE;
                } else {
                    hasErrorsPassword = Boolean.TRUE;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void loginAction(Button b, String emailData, String passwordData) {


       try {

           if (!hasErrorsPassword && !hasErrorsUsername && passwordData != null && passwordData != null) {

               b.setEnabled(Boolean.FALSE);
               b.setAlpha(0.3F);

               mAuth.signInWithEmailAndPassword(emailData, passwordData).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       makeToast("Login success");
                       startActivity(homePage);
                       finish();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       makeToast(e.getMessage());

                       String error = e.getMessage();

                       if (error.contains("user") && !error.contains("password")) {
                           email.setError(error);
                       }

                       if (error.contains("password")) {
                           password.setError(error);
                       }

                       b.setEnabled(true);
                       b.setAlpha(1);
                   }
               });

           } else {

               if (emailData == null) {
                   email.setError("Please fill this field");
                   throw new Exception("email not duly fulled");
               }

               if (passwordData == null) {
                   password.setError("Please fill this field");
                   throw new Exception("pass not filled");
               }
           }

       } catch (Exception e) {

           b.setEnabled(true);
           b.setAlpha(1);
           makeToast(e.getMessage());
           Log.d("LoginExeption", e.getMessage());
       }
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}