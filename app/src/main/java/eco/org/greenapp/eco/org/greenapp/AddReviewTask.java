package eco.org.greenapp.eco.org.greenapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;

import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;


public class AddReviewTask  extends AsyncTask<JSONObject, Void, String>{
    Context context;

    public  AddReviewTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(JSONObject... jsons) {
        JSONObject json = jsons[0];
        String expeditor;
        String destinatar;
        double nota;
        int tranzactie;
        String detalii;
        Date date = Calendar.getInstance().getTime();
        String data = GeneralConstants.SDF.format(date);
        try {
             expeditor = json.getString("autor");
             destinatar = json.getString("user");
             detalii  = json.getString("detalii");
             nota = json.getDouble("nota");
             tranzactie = json.getInt("tranzactie");

            try {
                URL url = new URL(GeneralConstants.URL+"/inserare_evaluare.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream outputStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String reviewData = URLEncoder.encode("autor", "UTF-8") + "=" + URLEncoder.encode(expeditor, "UTF-8") + "&"
                        + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(destinatar, "UTF-8") + "&"
                        + URLEncoder.encode("nota", "UTF-8") + "=" + URLEncoder.encode(""+nota, "UTF-8") + "&"
                        + URLEncoder.encode("detalii", "UTF-8") + "=" + URLEncoder.encode(detalii, "UTF-8") + "&"
                        + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8") + "&"
                        + URLEncoder.encode("tranzactie", "UTF-8") + "=" + URLEncoder.encode(""+tranzactie, "UTF-8");
                bufferedWriter.write(reviewData);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"));
                String result;

                StringBuilder stringBuilder = new StringBuilder();

                while((result = bufferedReader.readLine())!=null){
                    stringBuilder.append(result);
                }
                con.disconnect();
                return stringBuilder.toString();
            } catch (Exception e) {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null)
        {
           // Toast.makeText(context.getApplicationContext(), s+"Adăugare cu succes!", Toast.LENGTH_LONG).show();
        }
        else
        {
            //Toast.makeText(context.getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
        }
    }
}
