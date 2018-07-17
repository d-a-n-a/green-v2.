package eco.org.greenapp.eco.org.greenapp.activities;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.design.widget.Snackbar;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.ExecuteRequests;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.enumerations.TipAnunt;
import eco.org.greenapp.eco.org.greenapp.profile_activities.ExecuteInsertTasks;

import static eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants.URL;

public class AddProduct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextInputEditText txtProductName;
    EditText etProductDescription;
    Spinner productCategory;

    EditText etDetails;
    EditText etDurata;
    Bitmap bitmap;
    ImageView img;
    HashMap<String, String> values = new HashMap<String,String>();
    Calendar myCalendar;
    TextView data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        txtProductName = (TextInputEditText)findViewById(R.id.productNameInput);
        etProductDescription = (EditText)findViewById(R.id.productDescriptionInput);
        productCategory = (Spinner)findViewById(R.id.spinnerProductCategory);

        etDetails = (EditText)findViewById(R.id.productDetailsInput);
        etDurata = (EditText)findViewById(R.id.adDurata);
        img = (ImageView)findViewById(R.id.imgUpload);


        data = (TextView) findViewById(R.id.data);
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                data.setText(GeneralConstants.SDF.format(myCalendar.getTime()));

            }

        };
        ((Button)findViewById(R.id.btnSelectData)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddProduct.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        ((Button)findViewById(R.id.btnAddAd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkProductName() || !checkProductDesctiption() || !checkCategory() || !checkDetails())
                    Snackbar.make(findViewById(R.id.btnAddAd), "Trebuie completate toate câmpurile!", Snackbar.LENGTH_LONG).show();
                else {

                    Date date = Calendar.getInstance().getTime();
                    String dataIntroducerii = GeneralConstants.SDF.format(date);
                    final String valabilitate = GeneralConstants.SDF.format(myCalendar.getTime());
                    values.put("cod", GeneralConstants.INSERT_ADD);
                    values.put("email", getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString("email",null));
                    values.put("data_introducerii", dataIntroducerii);
                    values.put("durata", etDurata.getText().toString());
                    values.put("tip", TipAnunt.oferta.toString());
                    values.put("denumire", txtProductName.getText().toString().trim());
                    values.put("valabilitate", valabilitate);
                    values.put("categorie", productCategory.getSelectedItem().toString().toLowerCase());
                    values.put("detalii", etDetails.getText().toString().trim());
                    values.put("descriere", etProductDescription.getText().toString().trim());

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

        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

         if (id == R.id.findUsersByLocation) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
         int id = item.getItemId();

        if (id == R.id.nav_about) {
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

             ExecuteRequests executeRequests = new ExecuteRequests();

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
              }

            @Override
            protected String doInBackground(Bitmap... parametri) {
                Bitmap bitmap = parametri[0];
                String uploadImage = getStringImage(bitmap);
                HashMap<String,String> data = new HashMap<>();
                data.put(GeneralConstants.UPLOAD_KEY, uploadImage);
                String httpRezultat = executeRequests.sendPostRequest(URL+"/inserare_fotografie.php",data);
                return httpRezultat;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
}
