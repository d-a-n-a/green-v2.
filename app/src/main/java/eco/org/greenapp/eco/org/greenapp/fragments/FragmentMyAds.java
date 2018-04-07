package eco.org.greenapp.eco.org.greenapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
import eco.org.greenapp.eco.org.greenapp.activities.AddProduct;
import eco.org.greenapp.eco.org.greenapp.activities.TransactionDetails;
import eco.org.greenapp.eco.org.greenapp.adapters.AdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.adapters.UserAdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;


public class FragmentMyAds extends Fragment {


    private View view;
    private ListView lvAnunturi;
    private List<Advertisement> lista;
    SwipeRefreshLayout swipeContainer;
    UserAdvertisementAdapter adapter;
    Dialog dialog;
    Spinner statusSpinner;

    public FragmentMyAds() {
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

                            {

/*
                               Intent intent = new Intent(getContext(), AddProduct.class);
                               intent.putExtra("editAd", lista.get(position));
                               startActivity(intent);*/
                            }
                                break;
                            case R.id.idDelete:
                                DeleteAd deleteAd = new DeleteAd();
                                deleteAd.execute(lista.get(position).getId());

                                lista.remove(position);
                                adapter.notifyDataSetChanged();
                                break;

                            case R.id.idEditStaus:
                                //deschidere popup din care sa aleaga categoria - sa corespunda cu idurile din
                                dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.select_status);
                                dialog.show();
                                statusSpinner = (Spinner)dialog.findViewById(R.id.idNewStatus);
                                ((Button)dialog.findViewById(R.id.btnSaveNewStatus)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        String idNewStatus = getResources().getStringArray(R.array.idEditAdStatus)[statusSpinner.getSelectedItemPosition()];
                                        EditStatusAd editStatusAd  = new EditStatusAd();
                                        editStatusAd.execute(""+lista.get(position).getId(), idNewStatus);
                                        if(statusSpinner.getSelectedItem().toString().equals("indisponibil")){
                                            Intent intent = new Intent(getContext(), TransactionDetails.class);
                                            intent.putExtra("idAd", lista.get(position).getId());
                                            startActivity(intent);
                                        }
                                    }
                                });

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
                //URL url = new URL("http://10.38.31.11:8080/greenapp/select_user_advertisements.php");
                URL url = new URL("http://192.168.100.4:8080/greenapp/select_user_advertisements.php");
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
if(s!=null) {
    try {
        JSONArray vectorAds = new JSONArray(s);
        for (int i = 0; i < vectorAds.length(); i++) {
            JSONObject adItem = vectorAds.getJSONObject(i);
            Advertisement ad = new Advertisement();
            ad.setId(Integer.parseInt(adItem.getString("id")));
            ad.setUsername(adItem.getString("username"));
            ad.setStatusAnunt(adItem.getString("tipStatus"));
            ad.setDenumireProdus(adItem.getString("denumire"));
            ad.setTip(adItem.getString("tipAnunt"));
            ad.setDataPostarii(adItem.getString("dataIntroducerii"));
            ad.setUrl(adItem.getString("imagine"));
            lista.add(ad);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
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
               // url = new URL("http://10.38.31.11:8080/greenapp/delete_advertisement.php");
                url = new URL("http://192.168.100.4:8080/greenapp/delete_advertisement.php");
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
            if(aVoid!=null)
               if(aVoid.equals(GeneralConstants.SUCCESS))
                 adapter.notifyDataSetChanged();
        }
    }

    public class EditStatusAd extends AsyncTask<String, Void, String>{
        String idNewStatus;
        String idAd;
        @Override
        protected String doInBackground(String... strings) {
                idAd = strings[0];
                idNewStatus = strings[1];
            try {
                URL url = new URL("http://192.168.100.4:8080/greenapp/update_ad_status.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String updateValues = URLEncoder.encode("idAd", "UTF-8") + "=" + URLEncoder.encode(idAd, "UTF-8") + "&"
                        + URLEncoder.encode("idStatus", "UTF-8") + "=" + URLEncoder.encode(idNewStatus, "UTF-8");
                bufferedWriter.write(updateValues);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String dataLine = "";
                String updateResult = "";
                while ((dataLine = bufferedReader.readLine()) != null) {
                    updateResult += dataLine;
                }
                bufferedReader.close();

                inputStream.close();
                httpURLConnection.disconnect();

                return updateResult;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null)
            if(s.equals(GeneralConstants.SUCCESS)) {

                        lista.clear();
                        adapter.notifyDataSetChanged();
                        GetUserData gd = new GetUserData();
                        gd.execute();

            }
        }
    }

}
