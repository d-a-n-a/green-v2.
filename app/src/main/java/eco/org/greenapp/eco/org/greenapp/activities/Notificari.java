package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.adapters.AdapterNotificare;
import eco.org.greenapp.eco.org.greenapp.classes.Notificare;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.enumerations.TipNotificare;

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

        adapterNotificare = new AdapterNotificare(getApplicationContext(), R.layout.item_notificare, listaNotificari);
        listView.setAdapter(adapterNotificare);
        if(!username.isEmpty() && username != null){
            new PreluareNotificari().execute(username);
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Notificare notificare = listaNotificari.get(position);
                listaNotificari.remove(position);
                if(listaNotificari.size() == 0)
                {
                    ((TextView)findViewById(R.id.zeroNotificari)).setVisibility(View.VISIBLE);
                    ((ImageView)findViewById(R.id.imgZeroNotificari)).setVisibility(View.VISIBLE);
                }
                new EditStatusNotificare().execute(""+notificare.getId(), username);
                adapterNotificare.notifyDataSetChanged();
                return false;
            }
        });


                //todo citit -> true
                //eliminare din lista
                //inserare cu cursor in notificari_useri

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
             if(s == null )
                 Toast.makeText(getApplicationContext(),"Ups... something is wrong", Toast.LENGTH_SHORT).show();
             else
                 if(s.isEmpty())
                     Toast.makeText(getApplicationContext(), "Nu sunt notificari noi.", Toast.LENGTH_SHORT).show();
             else{
                     try {
                         JSONArray arrayNotificari = new JSONArray(s);
                         for (int i = 0; i < arrayNotificari.length(); i++) {
                             Notificare notificare = new Notificare();
                             JSONObject jsonObject = arrayNotificari.getJSONObject(i);
                             notificare.setCitit(Boolean.parseBoolean(jsonObject.getString("citit")));
                             notificare.setData(GeneralConstants.SDF.parse(jsonObject.getString("data")));
                             notificare.setDescriere(jsonObject.getString("descriere"));
                             notificare.setTip(TipNotificare.sistem);
                             notificare.setId(Integer.parseInt(jsonObject.getString("id")));
                             listaNotificari.add(notificare);
                         }
                         if(listaNotificari.size() == 0)
                         {
                             ((TextView)findViewById(R.id.zeroNotificari)).setVisibility(View.VISIBLE);
                             ((ImageView)findViewById(R.id.imgZeroNotificari)).setVisibility(View.VISIBLE);
                         }
                     adapterNotificare.notifyDataSetChanged();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }   catch (ParseException e) {
                         e.printStackTrace();
                     }
                 }
          }
     }
     public class EditStatusNotificare extends AsyncTask<String, Void, String>{

         @Override
         protected String doInBackground(String... strings) {
             String idNotificare = strings[0];
             String username = strings[1];
             try {
                 URL url = new URL(GeneralConstants.URL+"/editare_status_notificare.php");
                 HttpURLConnection con = (HttpURLConnection) url.openConnection();
                 con.setRequestMethod("POST");
                 con.setDoInput(true);
                 con.setDoOutput(true);

                 OutputStream outputStream = con.getOutputStream();
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                 String notificari = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+"&"
                         +URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(idNotificare, "UTF-8");
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
            if(s.equals(GeneralConstants.SUCCESS))
                adapterNotificare.notifyDataSetChanged();
            else
                if(s.equals(GeneralConstants.FAIL))
                    Toast.makeText(getApplicationContext(), "Ups... something went wrong. [Notificari.java(190)]", Toast.LENGTH_LONG).show();
         }
     }
}
