package eco.org.greenapp.eco.org.greenapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.adapters.UsersAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Locatie;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

public class UsersByLocation extends AppCompatActivity implements OnMapReadyCallback {


    ListView listViewUsersByLocation;
    UsersAdapter adapter;
    List<User> lista;
    float latitudine, longitudine;
    String distanta, username;
    Spinner spinnerDistanta;


    SupportMapFragment mapFragment;
    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    LocationManager locationManager;
    LocationListener locationListener;
String lattitude, longitude;
    List<Double> coordonate;
    List<Double> listaCoordonate;

GeoDataApi mGeoDataClient;
PlaceDetectionApi mPlaceDetectionClient;
FusedLocationProviderClient mFusedLocationClient;
LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_by_location);
        lista = new ArrayList<>();
        listViewUsersByLocation = (ListView) findViewById(R.id.lvUsersByLocation);


        spinnerDistanta = (Spinner) findViewById(R.id.spinnerDistanceKm);


        username = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(GeneralConstants.TOKEN, null);

/*
        latitudine = Float.parseFloat(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.LATITUDINE, null));
        longitudine = Float.parseFloat(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.LONGITUDINE, null));*/

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.locationsMap);
        mapFragment.getMapAsync(this);

        adapter = new UsersAdapter(getApplicationContext(), R.layout.user_item, lista);
        listViewUsersByLocation.setAdapter(adapter);

        ((Button) findViewById(R.id.btndo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                 //   coordonate = getLocation();
                 //   Toast.makeText(getApplicationContext(), coordonate.get(0)+"="+ coordonate.get(1),Toast.LENGTH_LONG).show();
                   // googleMap.addMarker(new MarkerOptions().position(new LatLng(coordonate.get(0), coordonate.get(1))));
                   // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordonate.get(0), coordonate.get(1)),12));
                    //ok = 1;
                }
              /*  distanta = spinnerDistanta.getSelectedItem().toString();
                GetUsersByLocation getUsersByLocation = new GetUsersByLocation();
                getUsersByLocation.execute("" + latitudine, "" + longitudine, distanta, username);
                adapter.notifyDataSetChanged();*/
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                googleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            googleMap.setMyLocationEnabled(true);
        }
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(UsersByLocation.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /*
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (markerLocatieCurenta != null) {
            markerLocatieCurenta.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Locatia mea");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        markerLocatieCurenta = googleMap.addMarker(markerOptions);

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }
  */
LocationCallback mLocationCallback = new LocationCallback() {
    @Override
    public void onLocationResult(LocationResult locationResult) {
        List<Location> locationList = locationResult.getLocations();
        if (locationList.size() > 0) {
            //The last location in the list is the newest
            Location location = locationList.get(locationList.size() - 1);
            Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = googleMap.addMarker(markerOptions);

            //move map camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        }
    }
};
    public class GetUsersByLocation extends AsyncTask<String, Void, List<User>>{

        String latitudine, longitudine, distanta, username;
        @Override
        protected List<User> doInBackground(String... strings) {
            latitudine = strings[0];
            longitudine = strings[1];
            distanta = strings[2];
            username = strings[3];

            URL url = null;
            try {

            url = new URL(GeneralConstants.URL+"/get_users_by_location.php");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream outputStream = http.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String updateValues = URLEncoder.encode("latitudine", "UTF-8") + "=" + URLEncoder.encode(latitudine, "UTF-8") + "&"
                    + URLEncoder.encode("longitudine", "UTF-8") + "=" + URLEncoder.encode(longitudine, "UTF-8")+ "&"
                    +  URLEncoder.encode("distanta", "UTF-8") + "=" + URLEncoder.encode(distanta, "UTF-8") + "&"
                    + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            bufferedWriter.write(updateValues);

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = http.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String dataLine = "";
            String updateResult = "";
            while ((dataLine = bufferedReader.readLine()) != null) {
                updateResult += dataLine;
            }
            bufferedReader.close();

            inputStream.close();
            http.disconnect();

                JSONArray users = null;
                try {
                    users = new JSONArray(updateResult);
                    for (int i = 0; i < users.length(); i++) {
                        Locatie locatie = new Locatie();
                        User user = new User();

                        JSONObject userItem = users.getJSONObject(i);
                        locatie.setStrada(userItem.getString("strada"));
                        locatie.setLatitudine(Float.parseFloat(userItem.getString("latitudine")));
                        locatie.setLongitudine(Float.parseFloat(userItem.getString("longitudine")));
                        user.setUsername(userItem.getString("username"));
                        user.setEmail(userItem.getString("email"));
                        user.setLocatie(locatie);
                        user.setUrl(userItem.getString("fotografie"));
                        lista.add(user);

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            return lista;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<User> s) {
            if(s.size() == 0)
                Toast.makeText(getApplicationContext(), "Nu s-a gasit niciun utilizator.", Toast.LENGTH_LONG).show();

            if(s.equals(GeneralConstants.RESULT_NOT_OK))
                Toast.makeText(getApplicationContext(), "Ups.. eroare preluare utilizatori dupa locatie. (UsersByLocation)", Toast.LENGTH_LONG).show();
            else {
                adapter.notifyDataSetChanged();
             }
        }
    }


    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pentru localizare aplicatia are nevoie de permisiune.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }
}
