package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Locatie;
import eco.org.greenapp.eco.org.greenapp.classes.Produs;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;
import eco.org.greenapp.eco.org.greenapp.fragments.TransactionHistoryFragment;

public class UsersByLocation extends AppCompatActivity {


    ListView listViewUsersByLocation;
    UsersAdapter adapter;
    List<User> lista;
    float latitudine, longitudine;
    String distanta, username;

    Spinner spinnerDistanta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_by_location);
        lista = new ArrayList<>();
        listViewUsersByLocation = (ListView) findViewById(R.id.lvUsersByLocation);



        spinnerDistanta = (Spinner) findViewById(R.id.spinnerDistanceKm);


        username = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(GeneralConstants.TOKEN, null);


        latitudine = Float.parseFloat(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.LATITUDINE, null));
        longitudine = Float.parseFloat(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.LONGITUDINE, null));

        GetUsersByLocation getUsersByLocation = new GetUsersByLocation();
        getUsersByLocation.execute("" + latitudine, "" + longitudine, "0", username);

        adapter = new UsersAdapter(getApplicationContext(), R.layout.user_item, lista);
        adapter.notifyDataSetChanged();
        listViewUsersByLocation.setAdapter(adapter);


            spinnerDistanta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDistanta.getSelectedItem().toString().equals("0")){

                         distanta = spinnerDistanta.getSelectedItem().toString();
                         GetUsersByLocation getUsersByLocation = new GetUsersByLocation();
                         getUsersByLocation.execute("" + latitudine, "" + longitudine, distanta, username);
                         adapter.notifyDataSetChanged();
                     }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

    }


    public class GetUsersByLocation extends AsyncTask<String, Void, String>{

        String latitudine, longitudine, distanta, username;
        @Override
        protected String doInBackground(String... strings) {
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

            return updateResult;
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
        protected void onPostExecute(String s) {
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            if(s.equals(GeneralConstants.RESULT_NOT_OK))
                Toast.makeText(getApplicationContext(), "Ups.. eroare preluare utilizatori dupa locatie. (UsersByLocation)", Toast.LENGTH_LONG).show();
            else {

                JSONArray users = null;
                try {
                    users = new JSONArray(s);
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
                    adapter.notifyDataSetChanged();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
             }
        }
    }
}
