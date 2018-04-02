package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.adapters.AdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.adapters.UserAdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;


public class FragmentUserAds extends Fragment {


    private View view;
    private ListView lvAnunturi;
    private List<Advertisement> lista;
    SwipeRefreshLayout swipeContainer;

    public FragmentUserAds() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lista = new ArrayList<>();
        if(view==null)
        {
            view=inflater.inflate(R.layout.fragment_fragment_user_ads, container,false);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
           // parent.removeView(view); //asta e posibil sa faca probleme
        }


        GetUserData gd = new GetUserData();
        gd.execute();

        final UserAdvertisementAdapter adapter=new UserAdvertisementAdapter(getActivity(),R.layout.my_adds_item,lista);
        lvAnunturi=(ListView)view.findViewById(R.id.idLv);
        lvAnunturi.setAdapter(adapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.idSwipe);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                lista.clear();
                adapter.notifyDataSetChanged();
                GetUserData gd = new GetUserData();
                gd.execute();
                swipeContainer.setRefreshing(false);



            }
        });

        return view;
    }

    public  class GetUserData extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://10.38.31.11:8080/greenapp/select_user_advertisements.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();



                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream outputStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String getByEmail = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(getActivity().getSharedPreferences(
                        GeneralConstants.SESSION, Context.MODE_PRIVATE).getString("email",null), "UTF-8");
                bufferedWriter.write(getByEmail);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"));
                String result;

                StringBuilder sb = new StringBuilder();

                while((result = bufferedReader.readLine())!=null){
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

            try {
                JSONArray vectorAds = new JSONArray(s);
                for(int i=0; i<vectorAds.length(); i++){
                    JSONObject adItem = vectorAds.getJSONObject(i);
                    Advertisement ad = new Advertisement();
                    ad.setUsername(adItem.getString("username"));
                    ad.setStatusAnunt(adItem.getString("tipStatus"));
                    ad.setDenumireProdus(adItem.getString("denumire"));
                    ad.setTip(adItem.getString("tipAnunt"));
                    ad.setDataPostarii(adItem.getString("dataIntroducerii"));
                    lista.add(ad);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
