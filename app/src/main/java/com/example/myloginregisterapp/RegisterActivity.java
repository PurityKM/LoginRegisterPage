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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private TextView username, email, password, confirmPass;

    private Button signupButton;

    private boolean usernameHasError, emailHasError, passwordHasError, confirmPassHasError = Boolean.FALSE;

    private Intent homePage;

    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        TextView btn= findViewById(R.id.existingAccount);

        username = findViewById(R.id.inputUserName);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        confirmPass = findViewById(R.id.inputConfirmPassword);
        signupButton = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        homePage = new Intent(this, Home.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupAction(signupButton, username.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userNm = charSequence.toString();

                if (userNm != null && userNm.length() > 5) {
                    username.setError(null);
                    usernameHasError =  Boolean.FALSE;
                } else {
                    //username.setError("Invalid Username");
                    username.setError("Username is too short!");
                    usernameHasError =  Boolean.TRUE;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String emailContent = charSequence.toString();

                if (emailContent != null && emailContent.length() > 5) {
                    email.setError(null);
                    emailHasError =  Boolean.FALSE;
                } else {
                    //email.setError("Invalid email");
                    email.setError("Invalid email");
                    emailHasError =  Boolean.TRUE;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass = charSequence.toString();
                String passConf = confirmPass.toString();

                if (pass.length() > 5) {

                    if (pass.compareTo(passConf) == 0) {
                        confirmPass.setError(null);
                        confirmPassHasError =  Boolean.FALSE;
                    } else {
                        confirmPass.setError("Passwords do not match");
                        confirmPassHasError =  Boolean.TRUE;
                    }

                } else {

                    if (pass != null && pass.length() <= 5) {
                        password.setError("password too short");
                    }
                    if (pass == null || pass.length() == 0) {
                        password.setError("Please fill this field");
                    }

                    confirmPassHasError =  Boolean.TRUE;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passConf = charSequence.toString();
                String pass = password.getText().toString();

                if (passConf.length() != 0 && pass.length() != 0) {

                    if (passConf.compareTo(pass) == 0) {
                        confirmPass.setError(null);
                        confirmPassHasError =  Boolean.FALSE;
                    } else {
                        confirmPass.setError("Passwords do not match");
                        confirmPassHasError =  Boolean.TRUE;
                    }

                } else {
                    confirmPassHasError = Boolean.TRUE;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void signupAction(Button b, String usernameTx, String emailData, String passwordData) {


        try {



            if (!emailHasError && !passwordHasError && !usernameHasError && !confirmPassHasError && usernameTx != null && passwordData != null && passwordData != null) {

                b.setEnabled(Boolean.FALSE);
                b.setAlpha(0.3F);

                mAuth.createUserWithEmailAndPassword(emailData, passwordData).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        makeToast("Signed up as, ".concat(usernameTx));
                        startActivity(homePage);
                        finish();
                    }
                }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser usr = mAuth.getCurrentUser();

                            if (usr != null) {

                                // Update
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(usernameTx)
                                        .build();



                                usr.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("tx", "User profile updated.");
                                                }
                                            }
                                        });

                                //Refresh cache (username)
                                usr.reload();
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeToast(e.getMessage());


                        if (e.getMessage().contains("email")) {
                            email.setError(e.getMessage());
                        }

                        b.setEnabled(true);
                        b.setAlpha(1);
                    }
                });

            } else {

                if (emailData == null || emailData.length() == 0) {
                    email.setError("Please fill this field");
                    throw new Exception("email not duly fulled");
                }

                if (passwordData == null || passwordData.length() == 0) {
                    password.setError("Please fill this field");
                    throw new Exception("pass not filled");
                }
            }

        } catch (Exception e) {

            b.setEnabled(true);
            b.setAlpha(1);

            if (e.getMessage().contains("empty or null")) {
                makeToast("Please fill in your details");
            }

            if (e.getMessage().contains("email")) {
                email.setError(e.getMessage());
            }

            Log.d("LoginExeption", e.getMessage());
        }
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}