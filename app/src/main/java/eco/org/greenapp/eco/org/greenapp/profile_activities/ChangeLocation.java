package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

public class ChangeLocation extends AppCompatActivity implements OnMapReadyCallback {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    Spinner spinnerCountries;
    AutoCompleteTextView eCity;
    EditText eStreet;
    double latitudine, longitudine;
    PlaceAutocompleteFragment placeAutocompleteFragment;
    private  GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        spinnerCountries = (Spinner)findViewById(R.id.spinnerCountries);
        eCity = (AutoCompleteTextView) findViewById(R.id.etCity);
        eStreet = (EditText)findViewById(R.id.etStreet);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.idMapFragmentLocation);
        mapFragment.getMapAsync(this);

        placeAutocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.placeAutocompleteFragment);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                eStreet.setText(place.getName());
                latitudine = place.getLatLng().latitude;
                longitudine = place.getLatLng().longitude;
                sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putString(SharedPreferencesConstants.LATITUDINE, ""+latitudine);
                sharedPreferencesEditor.putString(SharedPreferencesConstants.LONGITUDINE, ""+longitudine);
                sharedPreferencesEditor.apply();
                googleMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getName().toString()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),15));

            }

            @Override
            public void onError(Status status) {
                Log.d("GMaps Locatie - aprox.", "A aparut o eroare: " + status);
            }
        });

        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);

        eStreet.setText(sharedPreferences.getString(SharedPreferencesConstants.STREET, null));
        eCity.setText(sharedPreferences.getString(SharedPreferencesConstants.CITY, null));

        ((Button)findViewById(R.id.btnUpdateLocation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkLocationUpdate()){
                    //todo dialog
                    Toast.makeText(getApplicationContext(), "Trebuie completate toate campurile", Toast.LENGTH_LONG).show();
                }
                else
                {
                    sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString(SharedPreferencesConstants.STREET, eStreet.getText().toString().trim());
                    sharedPreferencesEditor.putString(SharedPreferencesConstants.CITY, eCity.getText().toString().trim());
                    sharedPreferencesEditor.putString(SharedPreferencesConstants.COUNTRY, spinnerCountries.getSelectedItem().toString());
                    sharedPreferencesEditor.apply();

                    UpdateLocationTask updateLocationTask = new UpdateLocationTask(getApplicationContext());
                    updateLocationTask.execute(sharedPreferences.getString(SharedPreferencesConstants.COUNTRY, null),
                            sharedPreferences.getString(SharedPreferencesConstants.CITY, null),
                            sharedPreferences.getString(SharedPreferencesConstants.STREET, null), ""+latitudine,""+longitudine);

                }
            }
        });

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, countries);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countryAdapter);
        spinnerCountries.setSelection(179);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.cityList));
        eCity.setAdapter(cityAdapter);


    }

    public boolean checkLocationUpdate(){
        if(eCity.getText().toString().trim().isEmpty()
                || eStreet.getText().toString().trim().isEmpty()
                || spinnerCountries.getSelectedItem().toString().isEmpty())
            return false;
        return  true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

    }

    public  class UpdateLocationTask extends AsyncTask<String, Void, String> {
        Context context;

        UpdateLocationTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String country = strings[0];
                String city = strings[1];
                String address = strings[2];
                String email = sharedPreferences.getString(SharedPreferencesConstants.EMAIL, null);
                String latitudine = strings[3];
                String longitudine = strings[4];

                URL url = new URL(GeneralConstants.URL+"/updateLocation.php");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream outputStream = http.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String updateValues = URLEncoder.encode("country", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8") + "&"
                        + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8") + "&"
                        + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode( address, "UTF-8") + "&"
                        +URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        +URLEncoder.encode("latitudine", "UTF-8") + "=" + URLEncoder.encode(""+latitudine, "UTF-8") + "&"
                        +URLEncoder.encode("longitudine", "UTF-8") + "=" + URLEncoder.encode(""+longitudine, "UTF-8") ;
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

                return updateResult;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("fail"))
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            finish();
          }

    }
    }
