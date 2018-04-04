package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

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

import eco.org.greenapp.R;

/**
 * Created by danan on 3/25/2018.
 */

public class ExecuteUpdatesTask extends AsyncTask<String, Void, String> {
    Context context;

    ExecuteUpdatesTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            int code = Integer.parseInt(strings[0]);
            String email = strings[1];
            String newValue = strings[2];

//192.168.43.191
            //URL url = new URL("http://10.38.31.11:8080/greenapp/update_last_name.php");
            URL url = new URL("http://192.168.100.4:8080/greenapp/update_last_name.php");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream outputStream = http.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String updateValues = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                    + URLEncoder.encode("value", "UTF-8") + "=" + URLEncoder.encode(newValue, "UTF-8") + "&"
                    + URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode("" + code, "UTF-8");
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
    protected void onPostExecute(String s)  {
Toast.makeText(this.context, s, Toast.LENGTH_LONG).show();
    if(s.equals("fail"))
    {

   }
    }
}


