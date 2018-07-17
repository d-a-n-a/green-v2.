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

public class ChangePhoneNumber extends AppCompatActivity {

    EditText etNrTelefon;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);

        etNrTelefon = (EditText)findViewById(R.id.etPhoneInput);
        etNrTelefon.setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(SharedPreferencesConstants.PHONE_NUMBER,null));

        ((Button)findViewById(R.id.btnUpdatePhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkPhoneUpdate()){
                    Toast.makeText(getApplicationContext(), "Numarul de telefon nu respecta formatul valid", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor  = sharedPreferences.edit();
                    editor.putString(SharedPreferencesConstants.PHONE_NUMBER, etNrTelefon.getText().toString().trim());
                    editor.apply();

                    ExecuteUpdatesTask executeUpdatesTask = new ExecuteUpdatesTask(getApplicationContext());
                    executeUpdatesTask.execute("4", sharedPreferences.getString(SharedPreferencesConstants.EMAIL,null), etNrTelefon.getText().toString().trim());
                    finish();
                }
            }
        });

    }

    public boolean checkPhoneUpdate(){
        String regexForm = "^[0-9]{10,14}$";
        if(etNrTelefon.getText().toString().isEmpty() || !etNrTelefon.getText().toString().matches(regexForm))
            return false;
        return true;
    }
}
