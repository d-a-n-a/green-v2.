package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.RequestHttp;
import eco.org.greenapp.eco.org.greenapp.activities.AdForProduct;
import eco.org.greenapp.eco.org.greenapp.adapters.AdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;


public class FragmentOne extends Fragment {

    private View view;
    private ListView lvAnunturi;
    private List<Advertisement> lista;
     SwipeRefreshLayout swipeContainer;
    public FragmentOne() {
        // Required empty public constructor
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
            //lista = parseJson(getResult);
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
            URL url = new URL("http://10.38.31.11:8080/greenapp/select_advertisements.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
                ad.setCategorie(adItem.getString("categorie"));
                ad.setLocatieUser(adItem.getString("strada"));
                ad.setDescriereProdus(adItem.getString("descriereProdus"));
                ad.setDetaliiAnunt(adItem.getString("detaliiAnunt"));
                ad.setValabilitate(adItem.getString("valabilitate"));
                lista.add(ad);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
    public void getData(){
        GetData gd = new GetData();
        gd.execute();
        }


}
