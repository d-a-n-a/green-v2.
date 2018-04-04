package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ExecuteInsertTasks;

public class AddProduct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextInputEditText txtProductName;
    EditText etProductDescription;
    Spinner productCategory;
    EditText etDay, etYear;
    Spinner etMonth;
    EditText etDetails;
    EditText etDurata;
    Intent intent;
    ContentValues contentValues;
    int edit = 0;
    HashMap<String, String> values = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        txtProductName = (TextInputEditText)findViewById(R.id.productNameInput);
        etProductDescription = (EditText)findViewById(R.id.productDescriptionInput);
        productCategory = (Spinner)findViewById(R.id.spinnerProductCategory);
        etDay = (EditText)findViewById(R.id.dayInput);
        etMonth = (Spinner)findViewById(R.id.monthInput);
        etYear = (EditText)findViewById(R.id.yearInput);
        etDetails = (EditText)findViewById(R.id.productDetailsInput);
        etDurata = (EditText)findViewById(R.id.adDurata);
/*
        intent = getIntent();
        if(intent != null){
            edit = 1;
            Advertisement ad = (Advertisement) intent.getSerializableExtra("editAd");
            txtProductName.setText(ad.getDenumireProdus());
        etProductDescription.setText(ad.getDescriereProdus());
        // productCategory.setSe;
        // etDay.setText(ad.getda);
        etDetails.setText(ad.getDetaliiAnunt());
        etDurata.setText(ad.);
    }*/
       /* contentValues = new ContentValues();
        contentValues.put("cod", GeneralConstants.INSERT_ADD);
        contentValues.put("email", getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString("email",null));
        contentValues.put("data_introducerii", "xx.xx.xxxx");
        contentValues.put("durata", etDurata.getText().toString());
        contentValues.put("tip", "cerere");
        contentValues.put("denumire", txtProductName.getText().toString().trim());
        contentValues.put("valabilitate", valabilitate);
        contentValues.put("categorie", productCategory.getSelectedItem().toString());
        contentValues.put("detalii", etDetails.getText().toString().trim());
        contentValues.put("descriere", etProductDescription.getText().toString().trim());
        contentValues.put("titlu", "trebuie adaugat input pentru titlu");*/


        ((Button)findViewById(R.id.btnAddAd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(!checkProductName() || !checkProductDesctiption() || !checkCategory() || !checkDetails())
                    Toast.makeText(getApplicationContext(), "completati campurile corespunzator!", Toast.LENGTH_LONG).show();
                else {

                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                //    String dataIntroducerii = simpleDateFormat.format(date);
                    String dataIntroducerii = GeneralConstants.SDF.format(date);
                    final String valabilitate = etDay.getText()+"-"+etMonth.getSelectedItem().toString()+"-"+etYear.getText();
                    values.put("cod", GeneralConstants.INSERT_ADD);
                    values.put("email", getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString("email",null));
                    values.put("data_introducerii", dataIntroducerii);
                    values.put("durata", etDurata.getText().toString());
                    values.put("tip", "ofer");
                    values.put("denumire", txtProductName.getText().toString().trim());
                    values.put("valabilitate", valabilitate);
                    values.put("categorie", productCategory.getSelectedItem().toString());
                    values.put("detalii", etDetails.getText().toString().trim());
                    values.put("descriere", etProductDescription.getText().toString().trim());

                    //Toast.makeText(getApplicationContext(), values.get("email")+"-"+values.get("valabilitate")+"-"+values.get("tip"), Toast.LENGTH_LONG).show();

                    ExecuteInsertTasks executeInsertTasks = new ExecuteInsertTasks(getApplicationContext());
                    executeInsertTasks.execute(values);
                }
            }
        });
    }

    private boolean checkProductName(){
        if(txtProductName.getText().toString().trim().isEmpty() || txtProductName.getText().toString().trim().length() < 3)
            return false;
        return true;
    }
    private boolean checkProductDesctiption(){
        if(etProductDescription.getText().toString().trim().isEmpty() || etProductDescription.getText().toString().length() < 10)
            return false;
        return true;
    }
    private boolean checkCategory(){
        if(productCategory.getSelectedItem().toString().equals("Selectati"))
            return false;
        return true;
    }
  /*  private boolean checkDate(){
        if(etDay.getText().toString().isEmpty() || etMonth.getText().toString().isEmpty() || etYear.getText().toString().isEmpty())
            return false;
        return true;
    }*/
  private boolean checkDetails(){
      if(etDetails.getText().toString().isEmpty() || etDetails.getText().toString().trim().length() <10)
          return false;
      return true;
  }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action
        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
