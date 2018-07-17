package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.text.DecimalFormat;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeAboutMe;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeEmail;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeFirstName;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeLastName;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeLocation;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangePassword;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangePhoneNumber;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeProfilePhoto;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeUsername;

public class ProfileSettings extends AppCompatActivity {

    TextView rating;
    LinearLayout layoutLougout;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if(resultCode == RESULT_OK && requestCode == GeneralConstants.ABOUT_RESULT_CODE)
        {
             ((TextView)findViewById(R.id.aboutUserDescription))
                    .setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                            .getString(SharedPreferencesConstants.ABOUT,null));
        }
        if(resultCode == RESULT_OK && requestCode == GeneralConstants.LAST_NAME_RESULT_CODE)
        {
            ((TextView)findViewById(R.id.userLastName))
                    .setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                            .getString(SharedPreferencesConstants.LAST_NAME,null));
        }
        if(resultCode == RESULT_OK && requestCode == GeneralConstants.FIRST_NAME_RESULT_CODE){
                ((TextView)findViewById(R.id.userFirstName)).setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.FIRST_NAME, null));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        rating  = (TextView) findViewById(R.id.userScore);
        layoutLougout = (LinearLayout)findViewById(R.id.logOutLayout);
        layoutLougout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
        });
        ((LinearLayout)findViewById(R.id.userAvatarLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangeProfilePhoto.class));
            }
        });
        ((LinearLayout)findViewById(R.id.userAboutLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), ChangeAboutMe.class),GeneralConstants.ABOUT_RESULT_CODE);
            }
        });

        ((LinearLayout)findViewById(R.id.userLastNameLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), ChangeLastName.class), GeneralConstants.LAST_NAME_RESULT_CODE);
            }
        });

        ((LinearLayout)findViewById(R.id.userFirstNameLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), ChangeFirstName.class), GeneralConstants.FIRST_NAME_RESULT_CODE);
            }
        });

        ((LinearLayout)findViewById(R.id.userLocationLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent( getApplicationContext(), ChangeLocation.class),
                        GeneralConstants.LOCATION_RESULT_CODE);
            }
        });

        ((LinearLayout)findViewById(R.id.userPhoneLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), ChangePhoneNumber.class), GeneralConstants.PHONE_RESULT_CODE);
            }
        });
        ((LinearLayout)findViewById(R.id.userEmailLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), ChangeEmail.class), GeneralConstants.EMAIL_RESULT_CODE);
            }
        });
        ((LinearLayout)findViewById(R.id.usernameLAyout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChangeUsername.class));
            }
        });
        
        ((LinearLayout)findViewById(R.id.userPasswordLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePassword.class));

            }
        });
        ((TextView)findViewById(R.id.aboutUserDescription)).setText(getSharedPreferences(GeneralConstants.SESSION,Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.ABOUT,null));
        ((TextView)findViewById(R.id.userLastName)).setText(getSharedPreferences(GeneralConstants.SESSION,Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.LAST_NAME,null));
        ((TextView)findViewById(R.id.userFirstName)).setText(getSharedPreferences(GeneralConstants.SESSION,Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.FIRST_NAME,null));

        ((TextView)findViewById(R.id.signUpDate)).setText(getSharedPreferences(GeneralConstants.SESSION,Context.MODE_PRIVATE)
        .getString(SharedPreferencesConstants.REGISTER_DATE, null));

        ((TextView)findViewById(R.id.userLocation)).setText(getSharedPreferences(GeneralConstants.SESSION,Context.MODE_PRIVATE)
        .getString(SharedPreferencesConstants.STREET, null));

        ((TextView)findViewById(R.id.userEmail)).setText(getSharedPreferences(GeneralConstants.SESSION,Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.EMAIL,null));

        ((TextView)findViewById(R.id.userPhone)).setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(SharedPreferencesConstants.PHONE_NUMBER, null));
        ((TextView)findViewById(R.id.username)).setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(GeneralConstants.TOKEN, null));


        ((TextView)findViewById(R.id.userScore)).setText("/5");
        ((LinearLayout)findViewById(R.id.aboutAppLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutApp.class));
            }
        });

        new CalculateMyReview().execute(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(
                GeneralConstants.TOKEN,null
        ));
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        SharedPreferences sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
        String location = sharedPreferences.getString(SharedPreferencesConstants.STREET, null);
        String aboutMe = sharedPreferences.getString(SharedPreferencesConstants.ABOUT, null);


        if(location.equals("") || aboutMe.equals(""))
        {
            builder.setMessage("Pentru a putea utiliza functionalitatile trebuie sa completati toate locatia si descrierea personala!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                this.finalize();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            SharedPreferences.Editor editor =  getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).edit();
            editor.putString(SharedPreferencesConstants.COMPLETE_REGISTER, "complet");
            editor.putString(SharedPreferencesConstants.STREET,((TextView)findViewById(R.id.userLocation)).getText().toString());
            editor.putString(SharedPreferencesConstants.ABOUT,((TextView)findViewById(R.id.aboutUserDescription)).getText().toString());
            editor.apply();
        }
    }

    public class CalculateMyReview extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String username;
            try {
                username = strings[0];
                URL url = new URL(GeneralConstants.URL+"/calcul_rating.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();


                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream outputStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String usernamepost = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                bufferedWriter.write(usernamepost);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"));
                String result;

                StringBuilder sb = new StringBuilder();

                while ((result = bufferedReader.readLine()) != null) {
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
            String nota;

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(2);
            if (s != null)
            {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if(jsonObject.getString("review").equals("nu are reviews"))
                        nota = ""+0;
                    else
                        nota = jsonObject.getString("review");

                    nota = String.format("%.2f", Float.parseFloat(nota));
                    rating.setText("" + nota + "/5");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
