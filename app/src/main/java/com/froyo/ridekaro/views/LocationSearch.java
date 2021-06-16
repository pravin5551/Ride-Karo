package com.froyo.ridekaro.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.froyo.ridekaro.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class LocationSearch extends AppCompatActivity {

    EditText etSearchLocation;
    Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        etSearchLocation = findViewById(R.id.etSearchLocation);
        btnLocation = findViewById(R.id.btnSerchLocation);
//        btnLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("com.rahul.location");
//                intent.putExtra("address", etSearchLocation.getText().toString());
//                sendBroadcast(intent);
//                finish();
//            }
//        });


//        tv1 = findViewById(R.id.tv1);
//
//        Places.initialize(getApplicationContext(), String.valueOf(R.string.google_api_key));
//
//        etSearchLocation.setFocusable(false);
//        etSearchLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
//                        .build(LocationSearch.this);
//                startActivityForResult(intent, 100);
//            }
//        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK) {
//            Place place = Autocomplete.getPlaceFromIntent(data);
//            etSearchLocation.setText(place.getAddress());
//            tv1.setText(String.format("Locality Name : %s", place.getName()));
//        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
//            Status status = Autocomplete.getStatusFromIntent(data);
//            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
}