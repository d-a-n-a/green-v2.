package eco.org.greenapp.eco.org.greenapp.profile_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.view.View;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import java.util.HashMap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.ExecuteRequests;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

public class ChangeProfilePhoto extends AppCompatActivity {

     ImageView imageView;
     Bitmap bitmap;
     String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_photo);
        imageView = (ImageView)findViewById(R.id.selectedImage);
        username = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN,null);


        ((Button)findViewById(R.id.btnSavePhoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfilePhoto();

            }
        });

        ((Button)findViewById(R.id.btnSelectPhoto)).setOnClickListener(new View.OnClickListener() {
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
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }        }
    }
    private void updateProfilePhoto(){
        class UpdateProfile extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog progressDialog;
            ExecuteRequests executeRequests = new ExecuteRequests();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ChangeProfilePhoto.this, "Actualizare...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String imagineProfil = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put(GeneralConstants.UPLOAD_KEY, imagineProfil);
                data.put("username", getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(
                        GeneralConstants.TOKEN,null
                ));
                String rezultat = executeRequests.sendPostRequest(GeneralConstants.URL+"/actualizare_fotografie_profil.php",data);

                return rezultat;
            }
        }

        UpdateProfile ui = new UpdateProfile();
        ui.execute(bitmap);
        finish();
    }
    public String getStringImage(Bitmap bitmapParam){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapParam.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
