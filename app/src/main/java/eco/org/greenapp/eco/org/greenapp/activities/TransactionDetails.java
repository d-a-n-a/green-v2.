package eco.org.greenapp.eco.org.greenapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Review;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

/**
 * Created by danan on 4/4/2018.
 */

public class TransactionDetails extends AppCompatActivity {
    TextInputEditText locatie;
    AutoCompleteTextView user;
    EditText data;
    EditText ora;
    Calendar myCalendar;
    int ad;
    Intent intent;
    int intentAd = 0;
    List<String> listaUsers   = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);

        myCalendar = Calendar.getInstance();
        data = (EditText)findViewById(R.id.dataIntalnire);
        ora = (EditText)findViewById(R.id.oraIntalnire);
        locatie = (TextInputEditText)findViewById(R.id.locatieIntalnire);
        user = (AutoCompleteTextView) findViewById(R.id.userIntalnire);

        user.setThreshold(1);

        GetUsers getUsers = new GetUsers();
        getUsers.execute();



        intent = getIntent();
        if(intent != null)
        {
            ad =  intent.getIntExtra("idAd",0);
            Toast.makeText(getApplicationContext(), ""+ad, Toast.LENGTH_LONG).show();
            intentAd = 1;
         }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                data.setText(GeneralConstants.SDF.format(myCalendar.getTime()));

            }

        };

        ((Button)findViewById(R.id.btnPickDate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TransactionDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ((Button)findViewById(R.id.btnPickHour)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TransactionDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ora.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        ((Button)findViewById(R.id.btnSaveTransactionDetails)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString().trim().isEmpty() || locatie.getText().toString().isEmpty()
                        || ora.getText().toString().isEmpty() || data.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Completati toate campurile!", Toast.LENGTH_LONG).show();
                else{
                    InsertTransaction insertTransaction = new InsertTransaction();
                    insertTransaction.execute(locatie.getText().toString().trim(),
                            data.getText().toString().trim(), ora.getText().toString().trim(),
                            getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN,null),
                            user.getText().toString().trim(),
                            ""+ad);
                    TransactionDetails.this.finish();

                }
            }
        });
    }
    public class GetUsers extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(GeneralConstants.URL+"/select_users.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String dataLine = "";
                String jsonResult = "";
                while((dataLine = bufferedReader.readLine())!=null){
                    jsonResult += dataLine;
                }
                bufferedReader.close();
                httpURLConnection.disconnect();

                return jsonResult;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String myUsername = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN, null);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject user = jsonArray.getJSONObject(i);
                      if(!user.getString("username").equals(myUsername)) {
                          listaUsers.add(user.getString("username"));
                     }
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, listaUsers.toArray(new String[0]));
                user.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class InsertTransaction extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String locatie = strings[0];
            String data = strings[1];
            String ora = strings[2];
            String expeditor = strings[3];
            String destinatar = strings[4];
            String idAnunt = strings[5];
            String queryData;
            try {
                URL url = new URL(GeneralConstants.URL+"/insert_transaction.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                queryData = URLEncoder.encode("expeditor","UTF-8") + "=" + URLEncoder.encode(expeditor,"UTF-8") + "&"
                        +  URLEncoder.encode("destinatar","UTF-8") + "=" + URLEncoder.encode(destinatar,"UTF-8") + "&"
                        +  URLEncoder.encode("locatie","UTF-8") + "=" + URLEncoder.encode(locatie,"UTF-8") + "&"
                        +  URLEncoder.encode("data","UTF-8") + "=" + URLEncoder.encode(data,"UTF-8") + "&"
                        +  URLEncoder.encode("ora","UTF-8") + "=" + URLEncoder.encode(ora,"UTF-8") +"&"
                        + URLEncoder.encode("idAnunt","UTF-8") + "=" + URLEncoder.encode(idAnunt,"UTF-8");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(queryData);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "iso-8859-1"));
                String result;

                StringBuilder sb = new StringBuilder();

                while((result = bufferedReader.readLine())!=null){
                    sb.append(result);
                }
                httpURLConnection.disconnect();
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals(GeneralConstants.SUCCESS)){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
