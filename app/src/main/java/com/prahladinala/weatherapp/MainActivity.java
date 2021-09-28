package com.prahladinala.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String APP_ID = "a15b56882b9144b320224990f036604c";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000; // FIVE SECONDS = 5000
    final float MIN_DISTANCE = 1000; // ONE METER = 1000
    final int REQUEST_CODE = 101;

    // VARIABLES TO STORE XML ID's
    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temperature;
    ImageView mweatherIcon;
    RelativeLayout mCityFinder;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weatherCondition);
        NameofCity = findViewById(R.id.cityName);
        Temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weatherIcon);
        mCityFinder = findViewById(R.id.cityFinder);

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityFinderActivity.class);
                startActivity(intent);
            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getWeatherForCurrentLocation();
//    }


    @Override
    protected void onResume() {
        super.onResume();

        // GET CITY NAME FROM INTENT
        Intent mIntent = getIntent();
        String city = mIntent.getStringExtra("City");
        if(city!=null){
            getWeatherForNewCity(city);
        } else {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);
        letsdoSomeNetworking(params);
    }
    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // FETCH LONGITUTE AND LATITUTE OF USER
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                // PASS THE LONGITUTE AND LATITUDE TO API
                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", APP_ID);
                letsdoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                // FETCH WEATHER
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                // POPUP TO ALLOW LOCATION PERMISSION

                // not able to get location - Toast
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // CHECKS WEATHER THE USER ALLOWED THE PERMISSION OR NOT
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Location get Successfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            } else {
                // USER DENIED THE PERMISSION
                Toast.makeText(MainActivity.this, "Failed to Get Location", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void letsdoSomeNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler(){
           // FETCH DATA FROM API

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();

                weatherData weatherD = weatherData.fromJson(response);
                updateUI(weatherD);
                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void updateUI(weatherData weather){
        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getmCity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getmIcon(), "drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager != null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}