package eco.org.greenapp.eco.org.greenapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Looper;
import android.provider.Settings;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.HashMap;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.adapters.SecondUsersAdapter;
import eco.org.greenapp.eco.org.greenapp.adapters.UsersAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Locatie;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;
import eco.org.greenapp.eco.org.greenapp.maps.JSONMaps;

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
    LatLng latLngME;
    List<User> listaUtilizatoriGasiti = new ArrayList<>();

    SecondUsersAdapter secondUsersAdapter;
    List<HashMap<String, String>> listaMapare;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_by_location);


        lista = new ArrayList<>();

        listaMapare = new ArrayList<>();
        listViewUsersByLocation = (ListView) findViewById(R.id.lvUsersByLocation);
        listViewUsersByLocation.setNestedScrollingEnabled(true);
        spinnerDistanta = (Spinner) findViewById(R.id.spinnerDistanceKm);
        username = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(GeneralConstants.TOKEN, null);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationsMap);
        mapFragment.getMapAsync(this);

        secondUsersAdapter = new SecondUsersAdapter(getApplication(),R.layout.user_item, listaMapare);
        listViewUsersByLocation.setAdapter(secondUsersAdapter);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            avertizareNoGps();

        }
     /*   ((Button) findViewById(R.id.btndo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distanta = spinnerDistanta.getSelectedItem().toString();
                GetUsersByLocation getUsersByLocation = new GetUsersByLocation();
                getUsersByLocation.execute("" + 44.44, "" + 26.08, distanta, "alina");
                adapter.notifyDataSetChanged();
            }
        });*/
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
        public void onLocationResult(LocationResult rezultat) {
            List<Location> listaLocatii = rezultat.getLocations();
            if (listaLocatii.size() > 0) {
                final Location location = listaLocatii.get(listaLocatii.size() - 1);
                 mLastLocation = location;
                if (markerLocatie != null) {
                    markerLocatie.remove();
                }
                latitudine = location.getLatitude();
                longitudine = location.getLongitude();
                latLngME = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLngME);
                markerOptions.title("Locatia mea");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                markerLocatie = googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngME, 12));

                ((Button) findViewById(R.id.btndo)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        distanta = spinnerDistanta.getSelectedItem().toString();
                        GetUsersByLocation getUsersByLocation = new GetUsersByLocation();
                        getUsersByLocation.execute("" + latitudine, "" + longitudine, distanta,
                                getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                                .getString(GeneralConstants.TOKEN,null));
                    }
                });
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
            if(s == null)
                 //Toast.makeText(getApplicationContext(), "Nu s-a primit lista.", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.idScrollUsers), "Nu s-a primit lista.", Snackbar.LENGTH_LONG).show();

            if(s.size() == 0)
               // Toast.makeText(getApplicationContext(), "Nu s-a gasit niciun utilizator.", Toast.LENGTH_LONG).show();
                //Snackbar.make(findViewById(R.id.idScrollUsers), "Nu s-a gasit niciun utilizator conform criteriilor.", Snackbar.LENGTH_LONG).show();
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UsersByLocation.this);
                alertDialog.setMessage("Nu s-a găsit niciun utilizator în apropiere.");

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    } });

                alertDialog.show();
            }
            if(s.equals(GeneralConstants.RESULT_NOT_OK))
                //
                // Toast.makeText(getApplicationContext(), "Ups.. eroare preluare utilizatori dupa locatie. (UsersByLocation)", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.idScrollUsers), "Ups.. eroare preluare utilizatori după locație", Snackbar.LENGTH_LONG).show();

            else {
               /* for (int i = 0; i<s.size(); i++){
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(s.get(i).getLocatie().getLatitudine(), s.get(i).getLocatie().getLongitudine())))
                            .setTitle(s.get(i).getUsername());//si in lista de jos apar detaliile, inclusiv distanta in timp sau km exacti
                }*/
                listaUtilizatoriGasiti = s;
                //adapter.notifyDataSetChanged();
                LatLng origine = new LatLng(latLngME.latitude, latLngME.longitude);

                List<String> urls = new ArrayList<>();
                for(int ii = 0;ii<s.size();ii++) {
                    Log.i("X", ""+s.size());
                    LatLng destinatie = new LatLng(s.get(ii).getLocatie().getLatitudine(), s.get(ii).getLocatie().getLongitudine());
                    googleMap.addMarker(new MarkerOptions().position(destinatie).title(s.get(ii).getUsername()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(destinatie));
                    String str_origin = "origin=" + latLngME.latitude + "," + latLngME.longitude;
                    String str_dest = "destination=" + destinatie.latitude + "," + destinatie.longitude;
                    String sensor = "sensor=false";
                    String parameters = str_origin + "&" + str_dest + "&" + sensor;
                    String output = "json";
                    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                    urls.add(url);
                    Log.i("A","indice "+ii);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(origine));

                }
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                FetchUrl fetchUrl = new FetchUrl();
                fetchUrl.execute(urls);
             }
        }
    }


    protected void avertizareNoGps() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Pentru localizare aplicatia are nevoie de permisiune.")
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
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }



    private class FetchUrl extends  AsyncTask<List<String>, Void, List<String>>{
        @Override
        protected List<String> doInBackground(List<String>[] lists) {
            List<String> lista = lists[0];

            List<String> data = new ArrayList<>();
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            for (int i = 0; i < lista.size(); i++) {
                String strUrl = lista.get(i);
                try {
                    URL url = new URL(strUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    iStream = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                    StringBuffer sb = new StringBuffer();
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    data.add(sb.toString());
                    br.close();
                } catch (Exception e) {

                } finally {
                    try {
                        iStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    urlConnection.disconnect();
                }
            }
            return data;
        }


        @Override
        protected void onPostExecute(List<String> s) {
             new ParserTask().execute(s);
        }
    }
    private List<String> downloadUrl(List<String> lista) throws  IOException{
        List<String> data = new ArrayList<>();
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        for(int i=0;i<lista.size();i++) {
            String strUrl = lista.get(i);
            try {
                URL url = new URL(strUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data.add(sb.toString());
                br.close();
            } catch (Exception e) {

            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
        }
        return data;
    }

    private class ParserTask extends AsyncTask<List<String>, Integer, HashMap<Integer,List<List<HashMap<String,String>>>>>{



        @Override
        protected HashMap<Integer,List<List<HashMap<String, String>>>> doInBackground(List<String>[] lists) {
            List<String> lista = lists[0];//asta e lista de jsoane de la url
            Log.i("CARrrrjsons", ""+lista.size());
            JSONObject jObject;
            HashMap<Integer,List<List<HashMap<String, String>>>> carnat = new HashMap<>();
            JSONArray jsonArray = new JSONArray();
            for(int i=0;i<lista.size();i++) {
                List<List<HashMap<String, String>>> routes = null;
                try {
                    jObject = new JSONObject(lista.get(i));//todo fac jsonobject pentru fiecare string din strings
                    jsonArray.put(jObject);
                   // JSONMaps jsonMaps = new JSONMaps();//todo fac defapt jsonarray si sa returneze direct de tip carnat
                    //routes = jsonMaps.parse(jObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JSONMaps jsonMaps = new JSONMaps();
            carnat = jsonMaps.parse(jsonArray);
         //   Log.i("CARrrrdo", carnat.size()+ " = "+carnat.toString());
           // Log.i("CARrrrdoHash", carnat.get(0).toString());
            return carnat;
        }

        @Override
        protected void onPostExecute(HashMap<Integer,List<List<HashMap<String, String>>>> lists) {
            listaMapare.clear();

            Log.i("CARrrr", lists.toString());
            List<String> distante = new ArrayList<>();
            List<String> durate = new ArrayList<>();
for(int t=0;t< lists.size();t++){
    List<List<HashMap<String, String>>> tempHash = lists.get(t);

    if(!tempHash.isEmpty()) {
        Log.i("CARrrrhash", t+"-"+tempHash.toString()+" === "+tempHash.get(0).get(0).toString().split("=")[1]);
        Log.i("CARrrrhash", tempHash.get(0).get(1).toString().split("=")[1].replace("}",""));

        durate.add(tempHash.get(0).get(0).toString().split("=")[1]);
        distante.add(tempHash.get(0).get(1).toString().split("=")[1].replace("}", ""));
    }
    else
    {

        durate.add("- mins");
        distante.add("- km");

    }
}
            Log.i("listautilizator", listaUtilizatoriGasiti.toString());

            for(int r = 0; r < distante.size(); r++){
                HashMap<String,String> mapare = new HashMap<>();
                mapare.put("durata", durate.get(r));
                mapare.put("distanta", distante.get(r));
                mapare.put("username", listaUtilizatoriGasiti.get(r).getUsername());
                mapare.put("adresa", listaUtilizatoriGasiti.get(r).getLocatie().getStrada());
                mapare.put("imagine", listaUtilizatoriGasiti.get(r).getUrl());
                listaMapare.add(mapare);
            }
            Log.i("CARrrrMAPARE", listaMapare.size()+" - "+listaMapare.toString());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            secondUsersAdapter.notifyDataSetChanged();
        }
    }
}

//todo cred ca asta de fapt a mers asa altfel nu stiu i-am facut alta functionalitate cumva
//update 9 iunie: cred ca "merge" pentru ca l-am pus sa astepte 2 secunde, ca sa vina toate datele      Thread.sleep(2000);
