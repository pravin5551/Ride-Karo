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

public class CustomerLogin extends AppCompatActivity {

    private Button btnCustomerRegister, btnCustomerLogin;
    private EditText etCustomerEmail, etCustomerPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireBaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        firebaseAuth = FirebaseAuth.getInstance();
        initViewsAndListeners();

        fireBaseListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(CustomerLogin.this, CustomerActivity.class));
                    finish();
                }
            }
        };

    }

    private void initViewsAndListeners() {
        btnCustomerRegister = findViewById(R.id.btnCustomerRegister);
        btnCustomerLogin = findViewById(R.id.btnCustomerLogin);
        etCustomerEmail = findViewById(R.id.etCustomerEmail);
        etCustomerPassword = findViewById(R.id.etCustomerPassword);

        btnCustomerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etCustomerEmail.getText().toString();
                final String password = etCustomerPassword.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(CustomerLogin.this, "sign up error", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userId);
                            currentUser.setValue(true);
                        }


                    }
                });
            }
        });
        btnCustomerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etCustomerEmail.getText().toString();
                final String password = etCustomerPassword.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(CustomerLogin.this, "sign in error", Toast.LENGTH_SHORT).show();
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