package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import eco.org.greenapp.eco.org.greenapp.adapters.AdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.adapters.UserAdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;


public class FragmentUserAds extends Fragment {


    private View view;
    private ListView lvAnunturi;
    private List<Advertisement> lista;
    SwipeRefreshLayout swipeContainer;
    UserAdvertisementAdapter adapter;
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

        adapter =new UserAdvertisementAdapter(getActivity(),R.layout.my_adds_item,lista);
        adapter.notifyDataSetChanged();
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

        lvAnunturi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popup = new PopupMenu(getContext(), view);
                popup.getMenuInflater().inflate(R.menu.my_adds_options, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.idEdit:

                                //TODO
                                break;
                            case R.id.idDelete:
                                DeleteAd deleteAd = new DeleteAd();
                                Toast.makeText(getContext(),""+lista.get(position).getId(), Toast.LENGTH_LONG).show();
                                deleteAd.execute(lista.get(position).getId());

                                lista.remove(position);
                                adapter.notifyDataSetChanged();
                                //delete from database

                                Toast.makeText(getContext(), ""+position, Toast.LENGTH_LONG).show();
                                break;

                            default:
                                break;
                        }

                        return true;
                    }
                });
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
                    ad.setId(Integer.parseInt(adItem.getString("id")));
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

    public class DeleteAd extends AsyncTask<Integer, Void, String>{

        int idAnunt;
        @Override
        protected String doInBackground(Integer... integers) {
            idAnunt = integers[0];
            URL url = null;
            try {
                url = new URL("http://10.38.31.11:8080/greenapp/delete_advertisement.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream outputStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String deleteById = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(""+idAnunt, "UTF-8");
                bufferedWriter.write(deleteById);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String dataLine = "";
                String updateResult = "";
                while ((dataLine = bufferedReader.readLine()) != null) {
                    updateResult += dataLine;
                }
                bufferedReader.close();

                inputStream.close();


                con.disconnect();
return updateResult;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid.equals(GeneralConstants.SUCCESS))
                adapter.notifyDataSetChanged();
        }
    }

}
