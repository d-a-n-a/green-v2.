package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

public class CreateAccount extends AppCompatActivity {

    EditText etFirstName;
    EditText etLastName;
    EditText etPassword;
    EditText etEmail;
    EditText etUsername;
    EditText etPhone;

    String firstName;
    String lastName;
    String password;
    String email;
    String username;
    String phone;

    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    String registerDate = simpleDateFormat.format(date);

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_account);



        Button btnAuthenticate = (Button) findViewById(R.id.btnAuthenticate);
        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
        });

        etFirstName = (EditText) findViewById(R.id.userFirstName);
        etLastName = (EditText) findViewById(R.id.userLastName);
        etPassword = (EditText) findViewById(R.id.userPassword);
        etEmail = (EditText) findViewById(R.id.userEmail);
        etUsername = (EditText) findViewById(R.id.userUsername);
        etPhone = (EditText) findViewById(R.id.userPhone);

        ((Button)findViewById(R.id.btnCreateAccount)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLastNameNotNull() || !isFirstNameNotNull() || !isEmailNotNull() || !isPasswordNotNull() || !isPhoneNotNull() || !isUsernameNotNull())
                    Toast.makeText(getApplicationContext(), "Toate campurile sunt obligatorii!", Toast.LENGTH_SHORT).show();
                else {
                    firstName = etFirstName.getText().toString();
                    lastName = etLastName.getText().toString();
                    password = etPassword.getText().toString();
                    email = etEmail.getText().toString();
                    username = etUsername.getText().toString();
                    phone = etPhone.getText().toString();

                    //inregistrarea
                    if(!isPhoneValid())
                        etPhone.setError("Numarul de telefon trebuie sa fie valid.");
                    if(!validFirstLastName())
                    {
                        etFirstName.setError("Numele si prenumele trebuie sa fie valide.");
                    }
                    if(!validEmailAddress()){
                        etEmail.setError("Adresa de email nu este valida.");

                    }
                    if(!isPhoneValid() || !validFirstLastName() || !validEmailAddress()){
                        Snackbar.make(findViewById(R.id.btnAuthenticate), "inputuri invalide", Snackbar.LENGTH_SHORT).show();

                    }
                    else {
                        CreateAccountTask createAccountTask = new CreateAccountTask(getApplicationContext());
                        createAccountTask.execute(firstName, lastName, email, password, username, phone);
                    }
                }
            }
        });

    }


    public boolean isLastNameNotNull(){
        if(etLastName.getText().toString().trim().isEmpty())
            return false;
        return true;
    }

    public boolean isFirstNameNotNull(){
        if(etFirstName.getText().toString().trim().isEmpty())
            return false;
        return true;
    }

    public boolean isPasswordNotNull(){
        if(etPassword.getText().toString().trim().isEmpty() || etPassword.getText().toString().trim().length() < 8)
            return false;
        return true;
    }

    public boolean isEmailNotNull(){
        if(etEmail.getText().toString().trim().isEmpty())
            return false;
        return true;
    }

    public boolean isPhoneNotNull(){
        if(etPhone.getText().toString().trim().isEmpty())
            return false;
        return true;
    }

    public boolean isUsernameNotNull(){
        if(etUsername.getText().toString().trim().isEmpty())
            return false;
        return true;
    }

    public boolean validFirstLastName(){
        if(etFirstName.getText().toString().matches("[0-9]+") || etLastName.getText().toString().matches("[0-9]+") ||
                etFirstName.getText().toString().trim().length() < 3 || etLastName.getText().toString().trim().length() < 3)
            return false;
        return true;
    }

    public boolean validEmailAddress(){
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        return false;
    }

    public boolean isPhoneValid(){
        String regexForm = "^[0-9]{10,14}$";
        if(phone.matches(regexForm))
             return true;
        return false;
    }
public class CreateAccountTask extends AsyncTask<String, Void, String> {
        Context context;
        CreateAccountTask(Context context){
            this.context = context;
         }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String lastName = strings[1];
                String firstName = strings[0];
                String email = strings[2];
                String username = strings[4];
                String pass = strings[3];
                String phone = strings[5];

                //192.168.43.191

                URL url = new URL("http://10.38.31.11:8080/greenapp/create_account.php");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream outputStream = http.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String insertPOST = URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8") + "&"
                        + URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8") + "&"
                        + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8") + "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode( registerDate, "UTF-8");
                bufferedWriter.write(insertPOST);

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
                bufferedReader.close();;
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
    protected void onPostExecute(String s) {


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        String registerDate = simpleDateFormat.format(date);

        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(SharedPreferencesConstants.COMPLETE_REGISTER, "incomplet");
        sharedPreferencesEditor.putString(SharedPreferencesConstants.LAST_NAME, lastName);
        sharedPreferencesEditor.putString(SharedPreferencesConstants.FIRST_NAME, firstName);
        sharedPreferencesEditor.putString(SharedPreferencesConstants.REGISTER_DATE, registerDate);
        sharedPreferencesEditor.putString(SharedPreferencesConstants.PASSWORD,  password);
        sharedPreferencesEditor.putString(SharedPreferencesConstants.PHONE_NUMBER, phone);
        sharedPreferencesEditor.putString(SharedPreferencesConstants.STREET, "");
        sharedPreferencesEditor.putString(SharedPreferencesConstants.ABOUT, "");
        sharedPreferencesEditor.putString(SharedPreferencesConstants.CITY, "");

        sharedPreferencesEditor.putString(SharedPreferencesConstants.EMAIL, email);
        sharedPreferencesEditor.apply();

        Intent intent = new Intent(getApplicationContext(),SignIn.class);
        intent.putExtra(GeneralConstants.TOKEN, username);
        intent.putExtra(GeneralConstants.PASSWORD, password);
        startActivity(intent);
        finish();
    }
}
}