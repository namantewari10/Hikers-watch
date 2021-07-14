package com.example.namanapphikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView textView1Latitude;
    TextView textView2Longitude;
    TextView textView3Accuracy;
    TextView textView4Altitude;
    TextView textView5Address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1Latitude=(TextView) findViewById(R.id.textView1Latitude);
        textView2Longitude=(TextView) findViewById(R.id.textView2Longitude);
        textView3Accuracy=(TextView) findViewById(R.id.textView3Accuracy);
        textView4Altitude=(TextView) findViewById(R.id.textView4Altitude);
        textView5Address=(TextView) findViewById(R.id.textView5Address);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String latitudeString="Latitude: ";
                String longitudeString="Longitude: ";
                String accuracyString="Accuracy: ";
                String altitudeString="Altitude: ";
                String addressString="Address: ";

                latitudeString+=Double.toString(location.getLatitude());
                longitudeString+=Double.toString(location.getLongitude());
                accuracyString+=Double.toString(location.getAccuracy());
                altitudeString+=Double.toString(location.getAltitude());

                textView1Latitude.setText(latitudeString);
                textView2Longitude.setText(longitudeString);
                textView3Accuracy.setText(accuracyString);
                textView4Altitude.setText(altitudeString);

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    if (listAddresses != null && listAddresses.size() > 0) {
                        String address = "";

                        if (listAddresses.get(0).getThoroughfare() != null) {
                            address += listAddresses.get(0).getThoroughfare() + " ";
                        }

                        if (listAddresses.get(0).getLocality() != null) {
                            address += listAddresses.get(0).getLocality() + " ";
                        }

                        if (listAddresses.get(0).getPostalCode() != null) {
                            address += listAddresses.get(0).getPostalCode() + " ";
                        }

                        if (listAddresses.get(0).getAdminArea() != null) {
                            address += listAddresses.get(0).getAdminArea();
                        }
                        addressString+=address;
                        textView5Address.setText(addressString);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}
