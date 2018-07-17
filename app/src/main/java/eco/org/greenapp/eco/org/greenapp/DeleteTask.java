package eco.org.greenapp.eco.org.greenapp;

import android.content.Context;
import android.os.AsyncTask;

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

import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

/**
 * Created by danan on 4/11/2018.
 */

public class DeleteTask extends AsyncTask <Integer, Void, String>{
    Context context;

    public DeleteTask(Context context){
        this.context = context;
    }
    @Override
    protected String doInBackground(Integer... ints) {
        URL url = null;
        int id = ints[0];
        try {
            url = new URL(GeneralConstants.URL+"/stergere_tranzactie.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String updateValues = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(""+id, "UTF-8");
            bufferedWriter.write(updateValues);

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
        } catch (IOException e) {
            e.printStackTrace();
        }

return null;

    }

    @Override
    protected void onPostExecute(String s) {
        if(s != null)
        {
            return;
        }
    }
}
