package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

/**
 * Created by danan on 3/28/2018.
 */

public class ExecuteInsertTasks extends AsyncTask<HashMap<String, String>, Void, String> {
    Context context;
    HashMap<String,String> valori;

    public ExecuteInsertTasks(Context context){
        this.context = context;
    }
    @Override
    protected String doInBackground(HashMap<String,String>... params) {

      valori = params[0];


        OutputStream outputStream = null;
        try {
            String queryData = "";
            URL url = new URL(GeneralConstants.URL+"/operatii.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String codPost =  valori.get("cod");

            switch(codPost){
                case GeneralConstants.INSERT_ADD:
                {
                    queryData = URLEncoder.encode("cod","UTF-8") + "=" + URLEncoder.encode(valori.get("cod"),"UTF-8") + "&"
                            + URLEncoder.encode("email","UTF-8") + "=" + URLEncoder.encode(context.getSharedPreferences(
                            GeneralConstants.SESSION, Context.MODE_PRIVATE
                    ).getString(SharedPreferencesConstants.EMAIL,null),"UTF-8") + "&"
                            + URLEncoder.encode("data_introducerii", "UTF-8") + "=" + URLEncoder.encode(valori.get("data_introducerii"), "UTF-8") + "&"
                            + URLEncoder.encode("durata", "UTF-8") + "=" + URLEncoder.encode(valori.get("durata"), "UTF-8") + "&"
                            + URLEncoder.encode("tip", "UTF-8") + "=" + URLEncoder.encode(valori.get("tip"), "UTF-8") + "&"
                            + URLEncoder.encode("denumire", "UTF-8") + "=" + URLEncoder.encode(valori.get("denumire"), "UTF-8") + "&"
                            + URLEncoder.encode("valabilitate", "UTF-8") + "=" +URLEncoder.encode(valori.get("valabilitate"), "UTF-8") + "&"
                            + URLEncoder.encode("categorie", "UTF-8") + "=" + URLEncoder.encode(valori.get("categorie"), "UTF-8") +"&"
                            + URLEncoder.encode("detalii", "UTF-8") + "=" + URLEncoder.encode(valori.get("detalii"), "UTF-8") + "&"
                            + URLEncoder.encode("descriere", "UTF-8") + "=" + URLEncoder.encode(valori.get("descriere"), "UTF-8");

                    break;
                }
                case GeneralConstants.INSERT_DEMAND:
                {
                    queryData = URLEncoder.encode("cod","UTF-8") + "=" + URLEncoder.encode(valori.get("cod"),"UTF-8") + "&"
                            + URLEncoder.encode("email","UTF-8") + "=" + URLEncoder.encode(context.getSharedPreferences(
                                    GeneralConstants.SESSION, Context.MODE_PRIVATE
                    ).getString(SharedPreferencesConstants.EMAIL,null),"UTF-8") + "&"
                            + URLEncoder.encode("data_introducerii", "UTF-8") + "=" + URLEncoder.encode(valori.get("data_introducerii"), "UTF-8") + "&"
                            + URLEncoder.encode("durata", "UTF-8") + "=" + URLEncoder.encode(valori.get("durata"), "UTF-8") + "&"
                            + URLEncoder.encode("tip", "UTF-8") + "=" + URLEncoder.encode(valori.get("tip"), "UTF-8") + "&"
                            + URLEncoder.encode("denumire", "UTF-8") + "=" + URLEncoder.encode(valori.get("denumire"), "UTF-8") + "&"
                            + URLEncoder.encode("valabilitate", "UTF-8") + "=" +URLEncoder.encode(valori.get("valabilitate"), "UTF-8") + "&"
                            + URLEncoder.encode("categorie", "UTF-8") + "=" + URLEncoder.encode(valori.get("categorie"), "UTF-8") +"&"
                            + URLEncoder.encode("detalii", "UTF-8") + "=" + URLEncoder.encode(valori.get("detalii"), "UTF-8") + "&"
                            + URLEncoder.encode("descriere", "UTF-8") + "=" + URLEncoder.encode(valori.get("descriere"), "UTF-8");

                    break;
                }

                default:{
                        queryData = "ups... error";
                }
            }
            bufferedWriter.write(queryData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String dataLine = "";
            String updateResult = "";
            while ((dataLine = bufferedReader.readLine()) != null) {
                updateResult += dataLine;
            }
            bufferedReader.close();

            inputStream.close();
            httpURLConnection.disconnect();
            return updateResult;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
    if(s!=null)
    {

    }
         // Toast.makeText(this.context, s, Toast.LENGTH_LONG).show();
     }
}
