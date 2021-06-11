package com.froyo.ridekaro.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.froyo.ridekaro.R;

public class WhichUser extends AppCompatActivity {

    private Button btnRider, btnCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_which_user);
        initViewsAndListeners();
    }

    private void initViewsAndListeners() {
        btnRider = findViewById(R.id.btnRider);
        btnCustomer = findViewById(R.id.btnCustomer);

        btnRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WhichUser.this, RiderLogin.class));
                finish();
            }
        });

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WhichUser.this, CustomerLogin.class));
                finish();
            }
        });

    }
}