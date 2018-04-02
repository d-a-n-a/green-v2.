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
import android.widget.Toast;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

public class ChangePassword extends AppCompatActivity {

    EditText etNewPassword, etConfirmNewPassword;
    Dialog infoDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etNewPassword = (EditText)findViewById(R.id.newPassword);
        etConfirmNewPassword = (EditText)findViewById(R.id.confirmNewPassword);
        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);

        ((Button)findViewById(R.id.btnUpdatePassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkEmpty() || ! checkValidity())
                    showInfoDialog(getWindow().getDecorView().findViewById(android.R.id.content));
                else
                    if(!checkEquals())
                        Toast.makeText(getApplicationContext(), "Nu corespund!", Toast.LENGTH_LONG).show();
                else
                    {
                        sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(SharedPreferencesConstants.PASSWORD, etNewPassword.getText().toString().trim());
                        sharedPreferencesEditor.apply();

                        ExecuteUpdatesTask executeUpdatesTask = new ExecuteUpdatesTask(getApplicationContext());
                        executeUpdatesTask.execute("7", sharedPreferences.getString(SharedPreferencesConstants.EMAIL,null), etNewPassword.getText().toString().trim());

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
    public boolean checkEquals() {
        if(!etConfirmNewPassword.getText().toString().trim().equals(etNewPassword.getText().toString().trim()))
            return false;
        return true;
    }
    public boolean checkValidity() {
        if(etConfirmNewPassword.getText().toString().trim().length() < 8)
            return false;
        return true;
    }
    public boolean checkEmpty(){
         if(etConfirmNewPassword.getText().toString().trim().isEmpty() || etNewPassword.getText().toString().isEmpty())
            return  false;
        return true;
    }
}
