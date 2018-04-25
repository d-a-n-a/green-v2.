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

public class ChangeFirstName extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    EditText etFirstNameInput;
    String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_first_name);

        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
        etFirstNameInput = ((EditText)findViewById(R.id.etPhoneInput));
        firstName = sharedPreferences.getString(SharedPreferencesConstants.FIRST_NAME, null);
        etFirstNameInput.setText(firstName);

        ((Button)findViewById(R.id.btnUpdatePhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkUpdateFirstName()){
                    //todo - dialog box
                    Toast.makeText(getApplicationContext(), "not ok", Toast.LENGTH_LONG).show();
                }
                else{
                    sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString(SharedPreferencesConstants.FIRST_NAME, etFirstNameInput.getText().toString().trim());
                    sharedPreferencesEditor.apply();
                    finishActivity(RESULT_OK);

                    ExecuteUpdatesTask executeUpdatesTask = new ExecuteUpdatesTask(getApplicationContext());
                    executeUpdatesTask.execute("1", sharedPreferences.getString(SharedPreferencesConstants.EMAIL,null), etFirstNameInput.getText().toString().trim());
                    finish();
               }
            }
        });

    }

    public boolean checkUpdateFirstName(){
        String update = etFirstNameInput.getText().toString().trim();
        if(update.length() < 3 || update.equals(firstName))
            return  false;
        return true;
    }
}
