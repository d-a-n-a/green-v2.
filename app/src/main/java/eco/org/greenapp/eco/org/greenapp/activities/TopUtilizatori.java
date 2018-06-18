package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.adapters.AdapterTopUtilizatori;
import eco.org.greenapp.eco.org.greenapp.classes.Locatie;
import eco.org.greenapp.eco.org.greenapp.classes.UserWithReview;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

public class TopUtilizatori extends AppCompatActivity {
List<UserWithReview> utilizatori;
AdapterTopUtilizatori adapterTopUtilizatori;
ListView listView;
List<Integer> tranzactiiFinalizate;
List<Integer> tranzactiiAnulate;
    BarChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_utilizatori);
        listView = (ListView)findViewById(R.id.listviewTop);
        utilizatori = new ArrayList<UserWithReview>();
        adapterTopUtilizatori = new AdapterTopUtilizatori(getApplicationContext(), R.layout.item_top_utilizator, utilizatori);
        listView.setAdapter(adapterTopUtilizatori);
        chart = (BarChart) findViewById(R.id.chart);
        tranzactiiAnulate = new ArrayList<>();
        tranzactiiFinalizate = new ArrayList<>();
        PreluareTopUtilizatori top = new PreluareTopUtilizatori();
        top.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserWithReview user = utilizatori.get(position);
                Intent intent = new Intent(getApplicationContext(), UserInfo.class);
                intent.putExtra("username", user.getUsername());
                startActivity(intent);
            }
        });
    }




    public class PreluareTopUtilizatori extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(GeneralConstants.URL+"/raport_top_utilizatori.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);

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
            super.onPostExecute(s);
            if (s != null) {
                ArrayList<String> xAxis = new ArrayList<>();
                try {

                    JSONArray vector = new JSONArray(s);
                    for (int i = 0; i < vector.length(); i++) {
                        JSONObject obiect = vector.getJSONObject(i);
                        Locatie locatie = new Locatie();
                        locatie.setStrada(obiect.getString("strada"));
                        locatie.setOras(obiect.getString("oras"));
                        UserWithReview user = new UserWithReview();
                        user.setMedieReviews((float)obiect.getInt("nota"));
                        user.setUsername(obiect.getString("username"));
                        user.setLocatie(locatie);
                        tranzactiiFinalizate.add(Integer.parseInt(obiect.getString("totalfinalizate")));
                        tranzactiiAnulate.add(obiect.getInt("totalanulate"));
                        utilizatori.add(user);
                        xAxis.add(user.getUsername());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterTopUtilizatori.notifyDataSetChanged();

                BarData data = new BarData(xAxis, getDataSet());
                data.setValueTextSize(14);


                chart.setData(data);
                chart.setDescription("");
                chart.animateXY(2000, 2000);
                chart.invalidate();
            }
        }
        }
    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> barDataSet = null;

        ArrayList<BarEntry> valueSetFinalizate = new ArrayList<>();
        for(int i=0;i<tranzactiiFinalizate.size();i++){
            BarEntry entry = new BarEntry(tranzactiiFinalizate.get(i), i);
            valueSetFinalizate.add(entry);
        }

        ArrayList<BarEntry> valueSetAnulate = new ArrayList<>();
        for(int i=0;i<tranzactiiAnulate.size();i++){
            BarEntry entry = new BarEntry(tranzactiiAnulate.get(i), i);
            valueSetAnulate.add(entry);
        }

        BarDataSet barDataFinalizate = new BarDataSet(valueSetFinalizate, "finalizate");
        barDataFinalizate.setColor(getResources().getColor(R.color.bluefm));
        BarDataSet barDataAnulate = new BarDataSet(valueSetAnulate, "anulate");
        barDataAnulate.setColor(getResources().getColor(R.color.redfm));

        barDataSet = new ArrayList<>();
        barDataSet.add(barDataFinalizate);
        barDataSet.add(barDataAnulate);
        return barDataSet;
    }

}
