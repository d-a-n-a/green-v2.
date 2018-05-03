package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
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
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Categorie;
import eco.org.greenapp.eco.org.greenapp.classes.Locatie;
import eco.org.greenapp.eco.org.greenapp.classes.Produs;
import eco.org.greenapp.eco.org.greenapp.enumerations.Selectie;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeLocation;

public class FilterFindUsers extends AppCompatActivity {

    TextView txtViewSchimbareLocatie;
    CheckBox cbCereri, cbOferte;
    SeekBar seekBarDistanta;
    int kmDistanta;
    int selectie = 0;
    Switch swHaine, swAlimente, swAltele;
    Button btnFiltrare;
    List<Advertisement> listaUtilizatori;


String latitudine, longitudine;
Selectie catAlimente, catHaine, catAltele;
Selectie tipCerere, tipOferta;
String maxDistanta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_find_users);

        listaUtilizatori = new ArrayList<>();

        txtViewSchimbareLocatie = (TextView)findViewById(R.id.textViewChangeLocation);
        txtViewSchimbareLocatie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangeLocation.class));
            }
        });

        cbCereri = (CheckBox)findViewById(R.id.checkBoxCereri);
        cbOferte = (CheckBox)findViewById(R.id.checkBoxOferte);


        seekBarDistanta = (SeekBar)findViewById(R.id.seekbarDistanta);
        kmDistanta = seekBarDistanta.getProgress();
        swHaine = (Switch)findViewById(R.id.switchHaine);
        swAlimente = (Switch)findViewById(R.id.switchAlimente);
        swAltele = (Switch)findViewById(R.id.switchAltele);
        btnFiltrare = (Button)findViewById(R.id.btnFiltrare);

        if(cbCereri.isSelected() || cbOferte.isSelected())
            selectie = 1;
        if(swAltele.isChecked() || swAlimente.isChecked() || swHaine.isChecked())
            selectie = 1;


        btnFiltrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(selectie == 0)
                    Toast.makeText(getApplicationContext(), "Alegeti cel putin un criteriu.", Toast.LENGTH_SHORT).show();
                else
                    if(!cbOferte.isSelected() && !cbCereri.isSelected())
                        Toast.makeText(getApplicationContext(), "Selectati Cereri si/sau Oferte", Toast.LENGTH_LONG).show();
                    else
                    {*/

                        latitudine = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                                    .getString(SharedPreferencesConstants.LATITUDINE, null);
                        longitudine = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                                .getString(SharedPreferencesConstants.LONGITUDINE, null);

                        tipCerere = (cbCereri.isChecked()) ? Selectie.cerere : Selectie.NU;
                        tipOferta = (cbOferte.isChecked()) ? Selectie.oferta : Selectie.NU;
                        catHaine = (swHaine.isChecked()) ? Selectie.haine : Selectie.NU;
                        catAlimente = (swAlimente.isChecked()) ? Selectie.alimente : Selectie.NU;
                        catAltele = (swAltele.isChecked()) ? Selectie.altele : Selectie.NU;
                        maxDistanta = ""+seekBarDistanta.getProgress();


                        GetUsersByCriteria getUsersByCriteria = new GetUsersByCriteria();
                        Toast.makeText(getApplicationContext(), latitudine+"   "+longitudine, Toast.LENGTH_LONG).show();
                        getUsersByCriteria.execute(latitudine, longitudine,
                                ""+tipCerere, ""+tipOferta,
                                ""+catHaine, ""+catAlimente, ""+catAltele,
                                maxDistanta);


                    }
          //  }
        });
    }

    public class GetUsersByCriteria extends AsyncTask<String, Void, String>{
        AlertDialog.Builder alertDialog;

        String lat, lng;
        String cerere, oferta;
        String alimente, haine, altele;
        String distanta;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(FilterFindUsers.this);
        }

        @Override
        protected String doInBackground(String... strings) {
             lat = strings[0];
            lng = strings[1];
            cerere = strings[2];
            oferta = strings[3];
            haine = strings[4];
            alimente = strings[5];
            altele = strings[6];
            distanta = strings[7];
            try {
                URL url = new URL(GeneralConstants.URL+"/find_users.php");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestMethod("POST");

                OutputStream outputStream = http.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String findUsers = URLEncoder.encode("latitudine", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8") + "&"
                        + URLEncoder.encode("longitudine", "UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8") + "&"
                        + URLEncoder.encode("cerere", "UTF-8") + "=" + URLEncoder.encode( cerere, "UTF-8") + "&"
                        +URLEncoder.encode("oferta", "UTF-8") + "=" + URLEncoder.encode(oferta, "UTF-8") + "&"
                        +URLEncoder.encode("alimente", "UTF-8") + "=" + URLEncoder.encode(""+alimente, "UTF-8") + "&"
                        +URLEncoder.encode("haine", "UTF-8") + "=" + URLEncoder.encode(""+haine, "UTF-8") +  "&"
                        +URLEncoder.encode("altele", "UTF-8") + "=" + URLEncoder.encode(""+altele, "UTF-8") + "&"
                        +URLEncoder.encode("distanta", "UTF-8") + "=" + URLEncoder.encode(""+(200+distanta), "UTF-8");
                bufferedWriter.write(findUsers);

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

/*if(s!=null){
    try {
        JSONArray jsonArray = new JSONArray(s);
       // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            UserFiltru user  = new UserFiltru();
            user.setMedieReviews(Float.parseFloat(jsonObject.getString("review")));
            user.setUrlProfil(jsonObject.getString("fotografie"));
            user.setUsername(jsonObject.getString("username"));
            user.setLocatie(jsonObject.getString("strada"));
            listaUtilizatori.add(user);
        }
        if(listaUtilizatori.size() > 0) {
            Intent intent = new Intent(getApplicationContext(), UsersFilterList.class);
            intent.putExtra("listaUtilizatori", (Serializable) listaUtilizatori);
            startActivity(intent);
        }
        else
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterFindUsers.this);
            alertDialog.setMessage("Nu s-a gasit niciun utilizator conform criteriilor.");

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
    } catch (JSONException e) {
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        e.printStackTrace();
    }

}*/
if (s != null) {
    try {

        JSONArray vectorAds = new JSONArray(s);
        for (int i = 0; i < vectorAds.length(); i++) {
            JSONObject adItem = vectorAds.getJSONObject(i);
            Advertisement ad = new Advertisement();
            eco.org.greenapp.eco.org.greenapp.classes.Status status = new eco.org.greenapp.eco.org.greenapp.classes.Status();
            Produs produs = new Produs();
            Categorie categorie = new Categorie();

            User user = new User();
            user.setUsername(adItem.getString("username"));
            user.setEmail(adItem.getString("email"));

            Locatie locatie = new Locatie();
            locatie.setLatitudine(Float.parseFloat(adItem.getString("latitudine")));
            locatie.setLongitudine(Float.parseFloat(adItem.getString("longitudine")));
            locatie.setStrada(adItem.getString("strada"));
            user.setLocatie(locatie);


            status.setTip(adItem.getString("tipStatus"));
            ad.setStatusAnunt(status);
            produs.setDenumireProdus(adItem.getString("denumire"));
            produs.setUrl(adItem.getString("imagine"));
            produs.setValabilitate(adItem.getString("valabilitate"));
            produs.setDetaliiAnunt(adItem.getString("detaliiAnunt"));
            categorie.setDenumire(adItem.getString("categorie"));
            produs.setCategorie(categorie);

            ad.setTip(adItem.getString("tipAnunt"));
            ad.setDataPostarii(adItem.getString("dataIntroducerii"));

            ad.setDescriereProdus(adItem.getString("descriereProdus"));
            ad.setProdus(produs);
            ad.setDistanta(Float.parseFloat(adItem.getString("distanta")));
            ad.setUser(user);
            listaUtilizatori.add(ad);
        }
       if(listaUtilizatori.size() > 0) {
//            Toast.makeText(getApplicationContext(), listaUtilizatori.size(), Toast.LENGTH_LONG).show();
             Intent intent = new Intent(getApplicationContext(), UsersFilterList.class);
            intent.putExtra("listaUtilizatori", (Serializable) listaUtilizatori);
            startActivity(intent);
       }
        else
        {
             AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterFindUsers.this);
            alertDialog.setMessage("Nu s-a gasit niciun anunt conform criteriilor.");

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
    } catch (JSONException e) {
        e.printStackTrace();
    }
}
else
{
    Toast.makeText(getApplicationContext(), "Ups...", Toast.LENGTH_LONG).show();

}
         }
    }
}
