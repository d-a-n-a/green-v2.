package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

public class ChangeUsername extends AppCompatActivity {

    EditText etUsername;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        etUsername = (EditText)findViewById(R.id.etPhoneInput);
        etUsername.setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN,null));

        ((Button)findViewById(R.id.btnUpdatePhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkUsernameUpdate()){
                    Toast.makeText(getApplicationContext(), "Introduceti usernameul dorit.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (!checkUsernameAvailability(etUsername.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Acest username exista deja.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString(GeneralConstants.TOKEN, etUsername.getText().toString().trim());
                        editor.apply();

                        ExecuteUpdatesTask executeUpdatesTask = new ExecuteUpdatesTask(getApplicationContext());
                        executeUpdatesTask.execute("2", sharedPreferences.getString(SharedPreferencesConstants.EMAIL,null), etUsername.getText().toString().trim());
                        finish();
                    }

                }
            }
        });
    }

    public  boolean checkUsernameUpdate(){
        if(etUsername.getText().toString().trim().isEmpty())
            return  false;
        return true;
    }
    public boolean checkUsernameAvailability(String usernameNou){
        return true;
    }
}
