package com.myapplicationdev.android.p09_gettingmylocations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {
    TextView tv1;
    Button b1, b2, b3;
    FusedLocationProviderClient client;
    String folderLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.TextView);
        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        client = LocationServices.getFusedLocationProviderClient(this);

        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/P09Folder";

        File folder = new File(folderLocation);
        if (folder.exists() == false){
            boolean result = folder.mkdir();
            if (result == true) {
                Log.d("File Read/Write", "Folder created");
            }
        }

        //Task 1
        if (checkPermission()) {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    String msg = "";
                    if (location != null) {
                        msg = "Last Known Location when this Activity Started:\n" +
                                "Latitude: " + location.getLatitude() +
                                "\nLongitude: " + location.getLongitude();
                    } else {
                        msg = "No Location Found";
                    }
                    tv1.setText(msg);
                }
            });
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                startService(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                stopService(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String folderLocation= Environment.getExternalStorageDirectory().getAbsolutePath() + "/P09Folder";
                File targetFile = new File(folderLocation, "data.txt");
                if (targetFile.exists()){
                    String data ="";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br= new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            data += line + "\n";
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            Log.d("permissions", "yes");
            return true;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return false;
        }
    }
}