package eco.org.greenapp.eco.org.greenapp.activities;


import android.app.Activity;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.ExecuteRequests;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ExecuteInsertTasks;

import static eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants.URL;

public class AddProduct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextInputEditText txtProductName;
    EditText etProductDescription;
    Spinner productCategory;
    EditText etDay, etYear;
    Spinner etMonth;
    EditText etDetails;
    EditText etDurata;
    Bitmap bitmap;
    ImageView img;
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
        img = (ImageView)findViewById(R.id.imgUpload);
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
                   // Toast.makeText(getApplicationContext(), valabilitate, Toast.LENGTH_LONG).show();
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
                    uploadImage();
                    finish();
                }
            }
        });

        ((Button)findViewById(R.id.btnAddPhoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GeneralConstants.PICK_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == GeneralConstants.PICK_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                img.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }        }
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

  private boolean checkDetails(){
      if(etDetails.getText().toString().isEmpty() || etDetails.getText().toString().trim().length() <10)
          return false;
      return true;
  }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog progressDialog;
            ExecuteRequests executeRequests = new ExecuteRequests();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(AddProduct.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
             }

            @Override
            protected String doInBackground(Bitmap... parametri) {
                Bitmap bitmap = parametri[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put(GeneralConstants.UPLOAD_KEY, uploadImage);
                String httpRezultat = executeRequests.sendPostRequest(URL+"/upload_image_test.php",data);

                return httpRezultat;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
}
