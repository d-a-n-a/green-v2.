 package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.GetImageTask;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentMyGeneralUserInfo;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentMyAds;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentMyReviews;
import eco.org.greenapp.eco.org.greenapp.fragments.TransactionHistoryFragment;

 public class MyProfile extends AppCompatActivity {
RatingBar ratingBar;
String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     //   setContentView(R.layout.activity_profile_settings);
        setContentView(R.layout.activity_my_profile);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentArea,new FragmentMyGeneralUserInfo());
        fragmentTransaction.commit();

        ((Button)findViewById(R.id.btnSettings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileSettings.class));
            }
        });

        ((TextView)findViewById(R.id.username)).setText(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(
                GeneralConstants.TOKEN,null));

        ratingBar = (RatingBar)findViewById(R.id.ratingUser);

        CalculateMyReview calculateMyReview = new CalculateMyReview();
        calculateMyReview.execute(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN,null));

    }
    public void selectionOfFragment(View view){
        ((Button)findViewById(R.id.button3)).setBackgroundResource(R.drawable.custom_button_profile_options);
        ((Button)findViewById(R.id.btnTakeProduct)).setBackgroundResource(R.drawable.custom_button_profile_options);
        ((Button)findViewById(R.id.button5)).setBackgroundResource(R.drawable.custom_button_profile_options_selected);
        ((Button)findViewById(R.id.button5)).setTextColor(getResources().getColor(R.color.white));
        ((Button)findViewById(R.id.btnTransactionHistory)).setBackgroundResource(R.drawable.custom_button_profile_options);
         Fragment fragment = new Fragment();
        if(view == findViewById(R.id.button5)){
           ((Button)findViewById(R.id.button5)).setTextColor(getResources().getColor(R.color.white));
            fragment =  new FragmentMyGeneralUserInfo();
            ((Button)findViewById(R.id.btnTakeProduct)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
            ((Button)findViewById(R.id.btnTransactionHistory)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
            ((Button)findViewById(R.id.button3)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
        }
        else
        if(view == findViewById(R.id.button3)){
            ((Button)findViewById(R.id.button3)).setTextColor(getResources().getColor(R.color.white));
            fragment = new FragmentMyAds();
            ((Button)findViewById(R.id.button5)).setBackgroundResource(R.drawable.custom_button_profile_options);
            ((Button)findViewById(R.id.btnTakeProduct)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
            ((Button)findViewById(R.id.btnTransactionHistory)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
            ((Button)findViewById(R.id.button5)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
        }
        else if(view == findViewById(R.id.btnTakeProduct))
        {((Button)findViewById(R.id.btnTakeProduct)).setTextColor(getResources().getColor(R.color.white));
            fragment = new FragmentMyReviews();
            ((Button)findViewById(R.id.button5)).setBackgroundResource(R.drawable.custom_button_profile_options);
            ((Button)findViewById(R.id.button5)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
            ((Button)findViewById(R.id.btnTransactionHistory)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
            ((Button)findViewById(R.id.button3)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
        }
        else
            if(view == findViewById(R.id.btnTransactionHistory)){
                ((Button)findViewById(R.id.btnTransactionHistory)).setTextColor(getResources().getColor(R.color.white));
            fragment = new TransactionHistoryFragment();
                ((Button)findViewById(R.id.button5)).setBackgroundResource(R.drawable.custom_button_profile_options);
                ((Button)findViewById(R.id.btnTakeProduct)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
                ((Button)findViewById(R.id.button5)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
                ((Button)findViewById(R.id.button3)).setTextColor(getResources().getColor(R.color.colorGreenSheen));
            }
        ((LinearLayout)findViewById(R.id.hiddenLayout)).setVisibility(View.INVISIBLE);
        view.setBackgroundResource(R.drawable.custom_button_profile_options_selected);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentArea,fragment);
        fragmentTransaction.commit();
    }
     public class CalculateMyReview extends AsyncTask<String, Void, String> {
         @Override
         protected String doInBackground(String... strings) {
             String username;
             try {
                 username = strings[0];
                 URL url = new URL(GeneralConstants.URL+"/calculate_review.php");
                 HttpURLConnection con = (HttpURLConnection) url.openConnection();


                 con.setRequestMethod("POST");
                 con.setDoInput(true);
                 con.setDoOutput(true);

                 OutputStream outputStream = con.getOutputStream();
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                 String usernamepost = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                 bufferedWriter.write(usernamepost);

                 bufferedWriter.flush();
                 bufferedWriter.close();
                 outputStream.close();

                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"));
                 String result;

                 StringBuilder sb = new StringBuilder();

                 while ((result = bufferedReader.readLine()) != null) {
                     sb.append(result);
                 }
                 con.disconnect();
                 return sb.toString();
             } catch (Exception e) {
                 return null;
             }
         }

         @Override
         protected void onPostExecute(String s) {
             String nota;
             if (s != null)
             {
                 try {
                     JSONObject jsonObject = new JSONObject(s);

                     if(jsonObject.getString("review").equals("nu are reviews"))
                         nota = ""+0;
                     else
                         nota = jsonObject.getString("review");
                     ratingBar.setRating(Float.parseFloat(nota)*ratingBar.getNumStars()/10);
                     imgUrl = GeneralConstants.Url+jsonObject.getString("foto");
                     if(!jsonObject.getString("foto").isEmpty() && !(jsonObject.getString("foto")==null) )
                         new GetImageTask((ImageView) findViewById(R.id.userProfilePicture), getApplicationContext())
                                 .execute(imgUrl);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

             }
         }

     }
}