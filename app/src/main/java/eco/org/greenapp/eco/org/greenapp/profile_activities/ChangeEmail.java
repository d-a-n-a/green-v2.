package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

public class ChangeEmail extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        etEmail = (EditText) findViewById(R.id.etPhoneInput);
        etEmail.setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(SharedPreferencesConstants.EMAIL, null));

        ((Button) findViewById(R.id.btnUpdatePhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.getText().toString().trim().isEmpty()) {
                    Snackbar.make(findViewById(R.id.layoutIDEmail), "Completați câmpul.", Snackbar.LENGTH_LONG).show();
                } else {

                    if (!checkEmailUpdate()) {
                        Snackbar.make(findViewById(R.id.layoutIDEmail), "Emailul introdus nu este valid.", Snackbar.LENGTH_LONG).show();

                    } else {
                        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString(SharedPreferencesConstants.EMAIL, etEmail.getText().toString().trim());
                        editor.apply();

                        UpdateEmail updateEmail = new UpdateEmail();
                        updateEmail.execute(etEmail.getText().toString().trim());
                        //finish();
                    }
                }
            }
        });

    }

    public boolean checkEmailUpdate() {
        if (etEmail.getText().toString().isEmpty() || !validEmailAddress())
            return false;
        return true;
    }

    public boolean validEmailAddress() {
        if (Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches())
            return true;
        return false;
    }

    public class UpdateEmail extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                String email = strings[0];
                URL url = new URL(GeneralConstants.URL + "/actualizare_email.php");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream outputStream = http.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String updateValues = URLEncoder.encode("oldEmail", "UTF-8") + "=" + URLEncoder.encode(getSharedPreferences(GeneralConstants.SESSION,
                        Context.MODE_PRIVATE)
                        .getString(SharedPreferencesConstants.EMAIL, null), "UTF-8") + "&" +
                        URLEncoder.encode("newEmail", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), "+" + s, Toast.LENGTH_LONG).show();
            if (!s.equals(GeneralConstants.SUCCESS)) {
                Toast.makeText(getApplicationContext(), "Ups...", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
