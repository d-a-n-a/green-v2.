package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.icu.lang.UScript;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.GetImageTask;
import eco.org.greenapp.eco.org.greenapp.classes.Review;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

/**
 * Created by danan on 4/15/2018.
 */

public class UsersAdapter extends ArrayAdapter<User> {
    ImageView img;
    TextView review;

    Context context;
    int idLayout;
    List<User> listaUsers;
    public UsersAdapter( Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.idLayout = resource;
        listaUsers = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(this.idLayout,parent, false);

        TextView username = (TextView)view.findViewById(R.id.username);
        TextView adresa = (TextView)view.findViewById(R.id.userAddress);
        review = (TextView)view.findViewById(R.id.reviewScore);
        img = (ImageView)view.findViewById(R.id.userAvatar);

        User user = this.listaUsers.get(position);
        username.setText(user.getUsername());
        adresa.setText(user.getLocatie());


       new CalculateReview().execute(user.getUsername());

        return view;
    }
    public class CalculateReview extends AsyncTask<String, Void, String> {
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
                    review.setText(Float.parseFloat(nota)+"/10");
                    String imgUrl = GeneralConstants.Url+jsonObject.getString("foto");
                    if(!jsonObject.getString("foto").isEmpty() && !(jsonObject.getString("foto")==null) )
                        new GetImageTask((ImageView) img, context).execute(imgUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
