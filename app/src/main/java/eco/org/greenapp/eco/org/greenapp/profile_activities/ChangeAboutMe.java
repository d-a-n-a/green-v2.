package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

public class ChangeAboutMe extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    String about;
    EditText etAbout;

    Dialog  infoDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_about_me);

        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
        about = sharedPreferences.getString(SharedPreferencesConstants.ABOUT, null);

        etAbout = ((EditText)findViewById(R.id.etAboutMeInput));
        etAbout.setText(about);
        ((Button)findViewById(R.id.btnUpdateAboutMe)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkUpdate()){
                    //aici fac not clickable buttoul
                    ((Button)findViewById(R.id.btnUpdateAboutMe)).setClickable(false);
                    //eventual il fac faded
                   // Toast.makeText(getApplicationContext(), "Descrierea trebuie sa aiba peste 20 caractere si trebuie sa fie diferita de cea anterioara", Toast.LENGTH_LONG).show();
                    showInfoDialog(getWindow().getDecorView().findViewById(android.R.id.content));
                }
                else{
                    sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString(SharedPreferencesConstants.ABOUT, etAbout.getText().toString().trim());
                    sharedPreferencesEditor.apply();
                   // finishActivity(GeneralConstants.ABOUT_RESULT_CODE);
                    //finishActivity(RESULT_OK);
                 //   onBackPressed();
                    ExecuteUpdatesTask executeUpdatesTask = new ExecuteUpdatesTask(getApplicationContext());
                    executeUpdatesTask.execute("5", sharedPreferences.getString(SharedPreferencesConstants.EMAIL,null), etAbout.getText().toString().trim());

                 }
            }
        });


        ((FloatingActionButton)findViewById(R.id.floatingActionButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showInfoDialog(view);
            }
        });

        infoDialog = new Dialog(this);
    }

    public void showInfoDialog(View v){
        infoDialog.setContentView(R.layout.info_pop_up);
        infoDialog.show();

        ((Button)infoDialog.findViewById(R.id.btnClosePopUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });

    }
    public boolean checkUpdate(){
        String update = etAbout.getText().toString().trim();
        if(update.length() < 20 || update.equals(about))
            return  false;
        return true;
        }
}
