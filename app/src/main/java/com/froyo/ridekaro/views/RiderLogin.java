package com.froyo.ridekaro.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.froyo.ridekaro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class RiderLogin extends AppCompatActivity {

    private Button btnRiderRegister, btnRiderLogin;
    private EditText etRiderEmail, etRiderPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireBaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);
        firebaseAuth = FirebaseAuth.getInstance();
        initViewsAndListeners();

        fireBaseListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(RiderLogin.this, HomeScreen.class));
                    finish();
                }

            }
        };

    }

    private void initViewsAndListeners() {
        btnRiderRegister = findViewById(R.id.btnRiderRegister);
        btnRiderLogin = findViewById(R.id.btnRiderLogin);
        etRiderEmail = findViewById(R.id.etRiderEmail);
        etRiderPassword = findViewById(R.id.etRiderPassword);

        btnRiderRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etRiderEmail.getText().toString();
                final String password = etRiderPassword.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RiderLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RiderLogin.this, "sign up error", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Riders").child(userId);
                            currentUser.setValue(true);
                        }

                    }
                });
            }
        });
        btnRiderLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etRiderEmail.getText().toString();
                final String password = etRiderPassword.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(RiderLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RiderLogin.this, "sign in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fireBaseListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(fireBaseListener);
    }
}