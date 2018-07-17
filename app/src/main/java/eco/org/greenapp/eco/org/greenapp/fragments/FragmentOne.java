package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.activities.AdForProduct;
import eco.org.greenapp.eco.org.greenapp.adapters.AdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Categorie;
import eco.org.greenapp.eco.org.greenapp.classes.Locatie;
import eco.org.greenapp.eco.org.greenapp.classes.Produs;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;


public class FragmentOne extends Fragment {

    private View view;
    private ListView lvAnunturi;
    private List<Advertisement> lista;
     SwipeRefreshLayout swipeContainer;

    public FragmentOne() {
     }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeContainer!=null) {
            swipeContainer.setRefreshing(false);
            swipeContainer.destroyDrawingCache();
            swipeContainer.clearAnimation();
        }
    }
    @Override
            public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                     final Bundle savedInstanceState) {
                // Inflate the layout for this fragment
                lista = new ArrayList<>();
                if(view==null)
                {
                    view=inflater.inflate(R.layout.fragment_fragment_one, container,false);
                }
                else
                {
                    ViewGroup parent = (ViewGroup) view.getParent();
                   // parent.removeView(view);
                }
                getData();
                final AdvertisementAdapter adapter=new AdvertisementAdapter(getActivity(),R.layout.product_item,lista);
                lvAnunturi=(ListView)view.findViewById(R.id.lvAds);
                lvAnunturi.setAdapter(adapter);

                swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        lista.clear();
                        adapter.notifyDataSetChanged();

                        swipeContainer.setRefreshing(false);

                        GetData gd = new GetData();
                        gd.execute();

                    }
                });

                lvAnunturi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), AdForProduct.class);
                        intent.putExtra("selectedAd", (Serializable)lista.get(position));
                        startActivity(intent);
                    }
                });

                return view;
            }
public  class GetData extends AsyncTask<Void,Void,String> {
    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL(GeneralConstants.URL+"/selectare_anunturi.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String username = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(getActivity()
                    .getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                    .getString(GeneralConstants.TOKEN,null), "UTF-8");
            bufferedWriter.write(username);

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
                    eco.org.greenapp.eco.org.greenapp.classes.Status status = new eco.org.greenapp.eco.org.greenapp.classes.Status();
                    Produs produs = new Produs();
                    Categorie categorie = new Categorie();
                    JSONObject adItem = vectorAds.getJSONObject(i);
                    Advertisement ad = new Advertisement();

                    User user = new User();
                    user.setUsername(adItem.getString("username"));
                    user.setEmail(adItem.getString("email"));

                    Locatie locatie = new Locatie();
                    locatie.setStrada(adItem.getString("strada"));
                    locatie.setLatitudine(Float.parseFloat(adItem.getString("latitudine")));
                    locatie.setLongitudine(Float.parseFloat(adItem.getString("longitudine")));
                    user.setLocatie(locatie);

                    status.setTip(adItem.getString("tipStatus"));
                    ad.setStatusAnunt(status);
                    produs.setDenumireProdus(adItem.getString("denumire"));
                    categorie.setDenumire(adItem.getString("categorie"));
                    produs.setCategorie(categorie);
                    produs.setDetaliiAnunt(adItem.getString("detaliiAnunt"));
                    produs.setUrl(adItem.getString("imagine"));
                    if(adItem.getString("valabilitate").equals("0000-00-00"))
                        produs.setValabilitate("---");
                    else
                        produs.setValabilitate(adItem.getString("valabilitate"));
                    ad.setTip(adItem.getString("tipAnunt"));
                    ad.setDataPostarii(adItem.getString("dataIntroducerii"));

                    ad.setDescriereProdus(adItem.getString("descriereProdus"));
                    ad.setProdus(produs);

                    ad.setDistanta(0.0f);
                    ad.setUser(user);
                    lista.add(ad);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
    public void getData(){
        GetData gd = new GetData();
        gd.execute();

        }


}
