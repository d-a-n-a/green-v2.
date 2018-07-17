package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.activities.RaportCategorii;

public class AboutApp extends AppCompatActivity {

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

          ((Button)findViewById(R.id.btnRapoarte)).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startActivity(new Intent(getApplicationContext(), RaportCategorii.class));
              }
          });
    }
}
