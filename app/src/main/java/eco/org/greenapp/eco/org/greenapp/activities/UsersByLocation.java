package eco.org.greenapp.eco.org.greenapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
    String distanta, username;
    Spinner spinnerDistanta;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    public static final int PERMISIUNE_LOCATIE = 27;
    Location mLastLocation;
    Marker markerLocatie;
    Double latitudine, longitudine;
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

/* //de astea nu am nevoie, pentru ca eu fac pe baza locatiei curente, nu cea din profil
        latitudine = Float.parseFloat(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.LATITUDINE, null));
        longitudine = Float.parseFloat(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.LONGITUDINE, null));*/

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationsMap);
        mapFragment.getMapAsync(this);

        adapter = new UsersAdapter(getApplicationContext(), R.layout.user_item, lista);
        listViewUsersByLocation.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
        ((Button) findViewById(R.id.btndo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distanta = spinnerDistanta.getSelectedItem().toString();
                GetUsersByLocation getUsersByLocation = new GetUsersByLocation();
                getUsersByLocation.execute("" + 44.44, "" + 26.08, distanta, "alina");
                adapter.notifyDataSetChanged();
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UsersByLocation.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                googleMap.setMyLocationEnabled(true);
            } else {
                permisiuneLocatie();
            }
        }
        else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            googleMap.setMyLocationEnabled(true);
        }
    }
    private void permisiuneLocatie() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Permisiune locatie")
                        .setMessage("Aplicatia are nevoie de permisiune pentru aceasta functionalitate.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(UsersByLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISIUNE_LOCATIE);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISIUNE_LOCATIE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISIUNE_LOCATIE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        googleMap.setMyLocationEnabled(true);

                        googleMap.moveCamera(CameraUpdateFactory.zoomIn());
                    }

                } else {
                    Toast.makeText(this, "Nu exista permisiune.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                 mLastLocation = location;
                if (markerLocatie != null) {
                    markerLocatie.remove();
                }
                latitudine = location.getLatitude();
                longitudine = location.getLongitude();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Locatia mea");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                markerLocatie = googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
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
                for (int i = 0; i<s.size(); i++){
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(s.get(i).getLocatie().getLatitudine(), s.get(i).getLocatie().getLongitudine())))
                            .setTitle(s.get(i).getUsername());//si in lista de jos apar detaliile, inclusiv distanta in timp sau km exacti
                }
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
                        //cred ca aici ar trebui sa fac cu onactivityresult
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
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
}
