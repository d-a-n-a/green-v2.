package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

/**
 * Created by danan on 4/5/2018.
 */

public class FragmentGeneralUserInfo extends Fragment {
    TextView etBiography;
    TextView etLocation;
    TextView etDate;
    SharedPreferences sharedPreferences;
    View view;
    String username;
    String urlImagine;
    public FragmentGeneralUserInfo(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null)
        {
            view=inflater.inflate(R.layout.fragment_fragment_general_user_info, container,false);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            // parent.removeView(view);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString("username",null);
        }
        SelectUserInfo selectUserInfo = new SelectUserInfo();
        selectUserInfo.execute(username);

        return view;

    }
public class SelectUserInfo extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... strings) {
        String username;
        try {
            username = strings[0];
            //URL url = new URL("http://10.38.31.11:8080/greenapp/select_user_advertisements.php");
            URL url = new URL("http://192.168.100.4:8080/greenapp/select_user_general_info.php");
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
        super.onPostExecute(s);
        if (s != null) {
            try {
                JSONObject detalii = new JSONObject(s);

                etBiography = (TextView)view.findViewById(R.id.biographyUser);
                etLocation = (TextView)view.findViewById(R.id.userKmDistance);
                etDate = (TextView)view.findViewById(R.id.userSignUpDate);

                etBiography.setText(detalii.getString("bio"));
                etDate.setText(detalii.getString("data"));
                etLocation.setText(detalii.getString("locatie"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
 }
}
