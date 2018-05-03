package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.adapters.AdapterNotificare;
import eco.org.greenapp.eco.org.greenapp.classes.Notificare;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

public class Notificari extends AppCompatActivity {

    ListView listView;
    AdapterNotificare adapterNotificare;
    List<Notificare> listaNotificari;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificari);

        listaNotificari = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listViewNotificari);

        username = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN, null);

        if(!username.isEmpty() && username != null){
            new PreluareNotificari().execute(username);
        }


        adapterNotificare = new AdapterNotificare(getApplicationContext(), R.layout.item_notificare, listaNotificari);
        listView.setAdapter(adapterNotificare);

        //new PreluareNotificari().execute(username);
        Notificare not = new Notificare();
        not.setDescriere("Dadaa");
        not.setData(new Date());
        listaNotificari.add(not);

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //todo citit -> true
                //eliminare din lista
                //inserare cu cursor in notificari_useri
                return false;
            }
        });
     }

     public  class  PreluareNotificari extends AsyncTask<String,Void,String>{

         @Override
         protected String doInBackground(String... strings) {
             String user = strings[0];
             try {
                 URL url = new URL(GeneralConstants.URL+"/preluare_notificari.php");
                 HttpURLConnection con = (HttpURLConnection) url.openConnection();
                 con.setRequestMethod("POST");
                 con.setDoInput(true);
                 con.setDoOutput(true);

                 OutputStream outputStream = con.getOutputStream();
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                 String notificari = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
                 bufferedWriter.write(notificari);

                 bufferedWriter.flush();
                 bufferedWriter.close();
                 outputStream.close();

                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"));
                 String result;

                 StringBuilder sb = new StringBuilder();

                 while((result = bufferedReader.readLine())!=null){
                     sb.append(result);
                 }
                 con.disconnect();
                 return sb.toString();
             } catch (Exception e) {
                 return null;
             }

         }

         @Override
         protected void onPostExecute(String s) {
             Log.i("xx", "s "+s.toString());
             if(s == null )
                 Toast.makeText(getApplicationContext(),"Ups... something is wrong", Toast.LENGTH_SHORT).show();
             else
                 if(s.isEmpty())
                     Toast.makeText(getApplicationContext(), "Nu sunt notificari noi.", Toast.LENGTH_SHORT).show();
             else{
                     try {
                         JSONArray arrayNotificari = new JSONArray(s);
                         for (int i = 0; i < arrayNotificari.length(); i++) {
                             Log.i("xx", "length array "+arrayNotificari.length());
                             Notificare notificare = new Notificare();
                             JSONObject jsonObject = arrayNotificari.getJSONObject(i);
                             Log.i("xx", "josnobj "+jsonObject.toString());
                             notificare.setCitit(Boolean.parseBoolean(jsonObject.getString("citit")));
                             notificare.setData(GeneralConstants.SDF.parse(jsonObject.getString("data")));
                             notificare.setDescriere(jsonObject.getString("descriere"));
                             Log.i("xx", "notif "+notificare.toString());
                             listaNotificari.add(notificare);
                         }
                             adapterNotificare.notifyDataSetChanged();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }
                     //Toast.makeText(getApplicationContext(), "xx lista notificari "+listaNotificari.size(), Toast.LENGTH_LONG).show();
                     adapterNotificare.notifyDataSetChanged();
                 }
          }
     }
}
