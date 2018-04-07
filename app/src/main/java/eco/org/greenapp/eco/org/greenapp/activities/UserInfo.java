package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.GetImageTask;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentGeneralUserInfo;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentUserAds;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentMyReviews;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentUserReviews;

public class UserInfo extends AppCompatActivity {
    Intent intent;
    String username;
    RatingBar rating;
    String imgUrl;
    ImageView imgV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_info);

        rating = (RatingBar) findViewById(R.id.ratingUser);
        imgV = (ImageView)findViewById(R.id.userProfilePicture);
        intent = getIntent();
        if (intent != null)
            username = intent.getStringExtra("username");

        CalculateReview calculateReview = new CalculateReview();
        calculateReview.execute(username);
    }

    public void selectionOfFragment(View view) {
        ((Button) findViewById(R.id.button3)).setBackgroundResource(R.drawable.button_user_info_normal);
        ((Button) findViewById(R.id.btnTakeProduct)).setBackgroundResource(R.drawable.button_user_info_normal);
        ((Button) findViewById(R.id.button5)).setBackgroundResource(R.drawable.button_user_info_normal);
        ((FloatingActionButton) findViewById(R.id.floatingMessage)).setVisibility(View.INVISIBLE);
        Fragment fragment = new Fragment();
        if (view == findViewById(R.id.button5)) {
            fragment = new FragmentGeneralUserInfo();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            fragment.setArguments(bundle);
        } else if (view == findViewById(R.id.button3)) {
            fragment = new FragmentUserAds();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            fragment.setArguments(bundle);

        } else if (view == findViewById(R.id.btnTakeProduct)) {
            fragment = new FragmentUserReviews();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            fragment.setArguments(bundle);
        }
        ((LinearLayout) findViewById(R.id.hiddenLayout)).setVisibility(View.INVISIBLE);
        view.setBackgroundResource(R.drawable.button_user_info_accent);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentArea, fragment);
        fragmentTransaction.commit();
    }


    public class CalculateReview extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String username;
            try {
                username = strings[0];
                URL url = new URL("http://192.168.100.4:8080/greenapp/calculate_review.php");
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
                         rating.setRating(Float.parseFloat(nota)*rating.getNumStars()/10);
                         imgUrl = "http://192.168.100.4:8080"+jsonObject.getString("foto");
                         if(!jsonObject.getString("foto").isEmpty() && !(jsonObject.getString("foto")==null) )
                       new GetImageTask((ImageView) findViewById(R.id.userProfilePicture))
                               .execute(imgUrl);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }
        }

    }
}
