package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;

import java.io.BufferedOutputStream;
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

/**
 * Created by danan on 3/28/2018.
 */

public class ExecuteInsertTasks extends AsyncTask<HashMap<String, String>, Void, String> {
    Context context;
    HashMap<String,String> values;

    public ExecuteInsertTasks(Context context){
        this.context = context;
    }
    @Override
    protected String doInBackground(HashMap<String,String>... params) {

      values = params[0];


        OutputStream outputStream = null;
        try {
            String queryData = "";
            URL url = new URL("http://192.168.100.4:8080/greenapp/test.php");

            //URL url = new URL("http://10.38.31.11:8080/greenapp/test.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);


             outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));


            String codPost =  values.get("cod");


            switch(codPost){
                case GeneralConstants.INSERT_ADD:
                {
                    queryData = URLEncoder.encode("cod","UTF-8") + "=" + URLEncoder.encode(values.get("cod"),"UTF-8") + "&"
                            + URLEncoder.encode("email","UTF-8") + "=" + URLEncoder.encode("dana.neagu1@gmail.com","UTF-8") + "&"
                            + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(values.get("data_introducerii"), "UTF-8") + "&"
                            + URLEncoder.encode("durata", "UTF-8") + "=" + URLEncoder.encode(values.get("durata"), "UTF-8") + "&"
                            + URLEncoder.encode("tip", "UTF-8") + "=" + URLEncoder.encode(values.get("tip"), "UTF-8") + "&"
                            + URLEncoder.encode("denumire", "UTF-8") + "=" + URLEncoder.encode(values.get("denumire"), "UTF-8") + "&"
                            + URLEncoder.encode("valabilitate", "UTF-8") + "=" +URLEncoder.encode(values.get("valabilitate"), "UTF-8") + "&"
                            + URLEncoder.encode("categorie", "UTF-8") + "=" + URLEncoder.encode(values.get("categorie"), "UTF-8") +"&"
                            + URLEncoder.encode("detalii", "UTF-8") + "=" + URLEncoder.encode(values.get("detalii"), "UTF-8") + "&"
                            + URLEncoder.encode("descriere", "UTF-8") + "=" + URLEncoder.encode(values.get("descriere"), "UTF-8");

                    break;
                }
                case GeneralConstants.INSERT_DEMAND:
                {
                    queryData = URLEncoder.encode("cod","UTF-8") + "=" + URLEncoder.encode(values.get("cod"),"UTF-8") + "&"
                            + URLEncoder.encode("email","UTF-8") + "=" + URLEncoder.encode("dana.neagu1@gmail.com","UTF-8") + "&"
                            + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(values.get("data_introducerii"), "UTF-8") + "&"
                            + URLEncoder.encode("durata", "UTF-8") + "=" + URLEncoder.encode(values.get("durata"), "UTF-8") + "&"
                            + URLEncoder.encode("tip", "UTF-8") + "=" + URLEncoder.encode(values.get("tip"), "UTF-8") + "&"
                            + URLEncoder.encode("denumire", "UTF-8") + "=" + URLEncoder.encode(values.get("denumire"), "UTF-8") + "&"
                            + URLEncoder.encode("valabilitate", "UTF-8") + "=" +URLEncoder.encode(values.get("valabilitate"), "UTF-8") + "&"
                            + URLEncoder.encode("categorie", "UTF-8") + "=" + URLEncoder.encode(values.get("categorie"), "UTF-8") +"&"
                            + URLEncoder.encode("detalii", "UTF-8") + "=" + URLEncoder.encode(values.get("detalii"), "UTF-8") + "&"
                            + URLEncoder.encode("descriere", "UTF-8") + "=" + URLEncoder.encode(values.get("descriere"), "UTF-8");

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
     //   Toast.makeText(this.context, contentValues.get("email")+"-"+contentValues.get("valabilitate"), Toast.LENGTH_LONG).show();
//
      Toast.makeText(this.context, s, Toast.LENGTH_LONG).show();
     }
}
