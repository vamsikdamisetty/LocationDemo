package com.example.locationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView tv, tv1,tv2;
    double dlatitude,dlongitude;
    Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }



    public void dothis(View view) {
        checkLocationPermission();

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                lastLocation = location;

                dlatitude = lastLocation.getLatitude();
                dlongitude = lastLocation.getLongitude();

                tv.setText("" + dlatitude +"\n"+ dlongitude);

                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try{
                    List<Address> LocationList = geocoder.getFromLocation(dlatitude,dlongitude,1);
                    if(LocationList.size() > 0)
                    {
                        Address address = LocationList.get(0);
                        ArrayList<String> addressparts = new ArrayList<>();
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            addressparts.add(address.getAddressLine(i));
                        }
                        tv2.setText("\n " + TextUtils.join("\n",addressparts));

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkLocationPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            Toast.makeText(this,"Location Permission Granted",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                checkLocationPermission();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
            }
        }
    }

//    public void openMap(View view) {
//        startActivity(new Intent(MainActivity.this,MarkerSearchMap.class));
//    }
}
