package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        etEmail = (EditText)findViewById(R.id.etPhoneInput);
        etEmail.setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(SharedPreferencesConstants.EMAIL,null));

        ((Button)findViewById(R.id.btnUpdatePhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkEmailUpdate()){
                    //todo aici ar trebui sa verific ca nu introduce o adresa existenta
                    Toast.makeText(getApplicationContext(), "Emailul introdus nu este valid",Toast.LENGTH_LONG ).show();
                }
                else{
                    sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString(SharedPreferencesConstants.EMAIL, etEmail.getText().toString().trim());
                    editor.apply();

                    ExecuteUpdatesTask executeUpdatesTask = new ExecuteUpdatesTask(getApplicationContext());
                    executeUpdatesTask.execute("3", sharedPreferences.getString(SharedPreferencesConstants.EMAIL,null), etEmail.getText().toString().trim());
                    finish();
                }
            }
        });

    }

    public  boolean checkEmailUpdate(){
        if(etEmail.getText().toString().isEmpty() || !validEmailAddress())
            return  false;
        return true;
    }
    public boolean validEmailAddress(){
        if(Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches())
            return true;
        return false;
    }

}
