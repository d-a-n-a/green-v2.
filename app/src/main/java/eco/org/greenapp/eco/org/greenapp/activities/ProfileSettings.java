package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import eco.org.greenapp.eco.org.greenapp.profile_activities.ChangeUsername;

public class ProfileSettings extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //todo asta ar parea ca nu prea merge??
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

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_settings);


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
    }
}
