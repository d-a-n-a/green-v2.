package eco.org.greenapp.eco.org.greenapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.activities.TransactionDetails;
import eco.org.greenapp.eco.org.greenapp.adapters.UserAdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Produs;
import eco.org.greenapp.eco.org.greenapp.classes.Status;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

/**
 * Created by danan on 4/5/2018.
 */
public class FragmentUserAds extends Fragment {


    private View view;
    private ListView lvAnunturi;
    private List<Advertisement> lista;
    SwipeRefreshLayout swipeContainer;
    UserAdvertisementAdapter adapter;
    String username;
    public FragmentUserAds() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lista = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString("username",null);
        }
        GetUserData gd = new GetUserData();
        gd.execute(username);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fragment_user_ads, container, false);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view); //asta e posibil sa faca probleme
        }




        adapter = new UserAdvertisementAdapter(getActivity(), R.layout.user_ad_item, lista);
        adapter.notifyDataSetChanged();
        lvAnunturi = (ListView) view.findViewById(R.id.idLv);
        lvAnunturi.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.idSwipe);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                lista.clear();
                adapter.notifyDataSetChanged();
                GetUserData gd = new GetUserData();
                gd.execute(username);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();

            }
        });

        return view;
    }

    public class GetUserData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String username;
            try {
                username = strings[0];
                URL url = new URL(GeneralConstants.URL+"/select_user_ads.php");
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
            if (s != null) {
                try {
                    JSONArray vectorAds = new JSONArray(s);
                    for (int i = 0; i < vectorAds.length(); i++) {
                        eco.org.greenapp.eco.org.greenapp.classes.Status status = new eco.org.greenapp.eco.org.greenapp.classes.Status();
                        Produs produs = new Produs();
                        JSONObject adItem = vectorAds.getJSONObject(i);
                        Advertisement ad = new Advertisement();
                        ad.setId(Integer.parseInt(adItem.getString("id")));
                        User user = new User();
                        user.setUsername(adItem.getString("username"));
                        status.setTip(adItem.getString("tipStatus"));
                        ad.setStatusAnunt(status);
                        produs.setDenumireProdus(adItem.getString("denumire"));
                        produs.setUrl(adItem.getString("imagine"));
                        ad.setTip(adItem.getString("tipAnunt"));
                        ad.setDataPostarii(adItem.getString("dataIntroducerii"));
                        ad.setUser(user);
                        ad.setProdus(produs);
                        lista.add(ad);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}