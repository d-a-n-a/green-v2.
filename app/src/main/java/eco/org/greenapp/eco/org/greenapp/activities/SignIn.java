package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import eco.org.greenapp.AppMenu;
import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

public class SignIn extends AppCompatActivity {

    EditText etPassword;
    EditText etUsername;
    String sPassword;
    String sUsername;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        etPassword = (EditText)findViewById(R.id.userPassword);
        etUsername = (EditText)findViewById(R.id.username);




        /*etUsername.setText(sharedPreferences.getString(SharedPreferencesConstants.EMAIL, ""));
        etPassword.setText(sharedPreferences.getString(SharedPreferencesConstants.PASSWORD, ""));*/


        Intent intent = getIntent();
        if(intent.hasExtra("token") && intent.hasExtra("password")) {
            etUsername.setText(intent.getStringExtra("token"));
            etPassword.setText(intent.getStringExtra("password"));

        }



    ((Button) findViewById(R.id.btnAuthenticate)).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!checkIsNullInputs()) {
                //Toast.makeText(getApplicationContext(),R.string.nullInputs,Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.signInConstraint), R.string.nullInputs, Snackbar.LENGTH_SHORT).show();
            }
            else
            {
                sPassword = etPassword.getText().toString();
                sUsername = etUsername.getText().toString();
                LoginTask login = new LoginTask(getApplicationContext());
                login.execute(sUsername, sPassword);            }
        }
    });

        ((Button)findViewById(R.id.btnCreate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateAccount.class));
                finish();
            }
        });
    }

    public boolean checkIsNullInputs(){
        sPassword = etPassword.getText().toString().trim();
        sUsername = etUsername.getText().toString().trim();

        if(sPassword.isEmpty() || sUsername.isEmpty())
            return false;
        return true;
    }
    public  class LoginTask extends AsyncTask<String, Void, String> {
        Context context;

        LoginTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... parameters) {
            try {
                String token = parameters[0];
                String password = parameters[1];
                // 192.168.43.191
              //  URL url = new URL("http://10.38.31.11:8080/greenapp/login.php");
                URL url = new URL("http://192.168.100.4:8080/greenapp/login.php");

                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream outputStream = http.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String passwordTokenPOST = URLEncoder.encode("token", "UTF-8") + "=" +
                        URLEncoder.encode(token, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(passwordTokenPOST);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = http.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String dataLine = "";
                String jsonResult = "";
                while((dataLine = bufferedReader.readLine())!=null){
                    jsonResult += dataLine;
                }
                bufferedReader.close();
                inputStream.close();
                http.disconnect();

                return jsonResult;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.equals(GeneralConstants.INVALID)) {
                    Toast.makeText(getApplicationContext(), "invalid  " + result, Toast.LENGTH_LONG).show();

                } else if (result.equals(GeneralConstants.RESULT_NOT_OK)) {
                    Toast.makeText(getApplicationContext(), "combinatie incorecta", Toast.LENGTH_LONG).show();
                } else {

                    try {
                        ArrayList<String> objects = new ArrayList<>();
                        JSONArray resultArray = new JSONArray(result);
                        for (int i = 0; i < resultArray.length(); i++) {
                            objects.add(resultArray.getString(i));
                        }
                        //    Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();

                        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
                        sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(SharedPreferencesConstants.LAST_NAME, objects.get(0));
                        sharedPreferencesEditor.putString(SharedPreferencesConstants.FIRST_NAME, objects.get(1));
                        sharedPreferencesEditor.putString(SharedPreferencesConstants.EMAIL, objects.get(2));
                        sharedPreferencesEditor.putString(GeneralConstants.TOKEN, objects.get(3));
                        //chestia e ca daca imi creez cont si intru de pe alt telefon - o sa crape pentru ca nu o sa am parola salvata pe undeva... sau nu.. nu stiu... emailul e important oricum
                        //sharedPreferencesEditor.putString(GeneralConstants.PASSWORD, objects.get(4));
                        sharedPreferencesEditor.putString(SharedPreferencesConstants.ABOUT, objects.get(6));
                        sharedPreferencesEditor.putString(SharedPreferencesConstants.PHONE_NUMBER, objects.get(7));
                        sharedPreferencesEditor.putString(SharedPreferencesConstants.REGISTER_DATE, objects.get(8));
                        sharedPreferencesEditor.putString(SharedPreferencesConstants.STREET, objects.get(9));
                        if (resultArray.get(7).toString().isEmpty() || objects.get(9).equals("0"))
                            sharedPreferencesEditor.putString(SharedPreferencesConstants.COMPLETE_REGISTER, "incomplet");
                        else
                            sharedPreferencesEditor.putString(SharedPreferencesConstants.COMPLETE_REGISTER, "complet");
                        sharedPreferencesEditor.apply();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent(getApplicationContext(), AppMenu.class));
                    finish();
                }
            }
        }
        }
    }


