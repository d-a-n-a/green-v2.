package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.GetImageTask;
import eco.org.greenapp.eco.org.greenapp.activities.UserInfo;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

/**
 * Created by danan on 4/29/2018.
 */

public class SecondUsersAdapter extends ArrayAdapter<HashMap<String, String>> {
    Context context;
    int idLayout;
    List<HashMap<String, String>> listaUsers;
    public SecondUsersAdapter( Context context, int resource, List<HashMap<String, String>> objects) {
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
        TextView review = (TextView)view.findViewById(R.id.reviewScore);
        ImageView img = (ImageView)view.findViewById(R.id.userAvatar);
        TextView dd = (TextView)view.findViewById(R.id.distdur);

        final HashMap<String,String> user = this.listaUsers.get(position);
        username.setText(user.get("username"));
        adresa.setText(user.get("adresa"));
dd.setText("{"+user.get("distanta")+" - "+user.get("durata"));

        new SecondUsersAdapter.CalculateReview(img, review).execute(user.get("username"));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfo.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
                intent.putExtra("username", user.get("username"));
                context.startActivity(intent);
            }
        });

        return view;
    }
    public class CalculateReview extends AsyncTask<String, Void, String> {
        ImageView imageView;
        TextView txtView;

        public  CalculateReview(ImageView imageView, TextView txtReview){
            this.imageView = imageView;
            this.txtView = txtReview;
        }
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

                    if(jsonObject.getString("review").equals("0")) {
                        nota = "" + 0;
                        txtView.setText("0/5");
                    }
                    else {
                        nota = jsonObject.getString("review");
                        nota = String.format("%.2f", Float.parseFloat(nota));
                        txtView.setText(nota + "/5");
                    }
                    //todo aici sa pun direct poza default ca la utilizatori oricum raman sigur aceleasi, ma rog aceeasi poza default user
                    String imgUrl = GeneralConstants.Url+jsonObject.getString("foto");
                    if(!jsonObject.getString("foto").isEmpty() && !(jsonObject.getString("foto")==null) )
                        new GetImageTask((ImageView) imageView, context).execute(imgUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
