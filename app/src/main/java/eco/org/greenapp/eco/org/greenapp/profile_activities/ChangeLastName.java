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

public class ChangeLastName extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    String lastName;
    EditText etLastNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_last_name);

        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
        lastName = sharedPreferences.getString(SharedPreferencesConstants.LAST_NAME, null);
        etLastNameInput = (EditText)findViewById(R.id.etLastNameInput);
        etLastNameInput.setText(lastName);


        ((Button)findViewById(R.id.btnUpdateLastName)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(!checkUpdateLastName())
            {
                Toast.makeText(getApplicationContext(), "Nume necorespunzator!", Toast.LENGTH_LONG).show();
             }
            else
            {
                sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putString(SharedPreferencesConstants.LAST_NAME, etLastNameInput.getText().toString().trim());
                sharedPreferencesEditor.apply();
                finishActivity(RESULT_OK);

                ExecuteUpdatesTask executeUpdatesTask = new ExecuteUpdatesTask(getApplicationContext());
                executeUpdatesTask.execute("0", sharedPreferences.getString(SharedPreferencesConstants.EMAIL,null), etLastNameInput.getText().toString().trim());
                finish();
            }
            }
        });
    }
    public boolean checkUpdateLastName(){
        String update = etLastNameInput.getText().toString().trim();
        if(update.length() < 3 || update.equals(lastName))
            return  false;
        return true;
    }
}
