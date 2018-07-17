package eco.org.greenapp.eco.org.greenapp.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

public class RaportCategorii extends AppCompatActivity {
PieChart chart;
List<Entry> entries;
    BarChart graficAnunturi;
    List<Integer> valoriCereri;
    List<Integer> valoriOferte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport_categorii);
        chart = (PieChart)findViewById(R.id.pieChart);
        entries = new ArrayList<>();

       graficAnunturi = (BarChart)findViewById(R.id.chartAnunturi);
       PreluareRaportCategorii raport = new PreluareRaportCategorii();
       raport.execute();
       valoriCereri = new ArrayList<>();
       valoriOferte = new ArrayList<>();
       PreluareNumarAnunturi preluareNumarAnunturi = new PreluareNumarAnunturi();
       preluareNumarAnunturi.execute();
    }

    public class PreluareRaportCategorii extends AsyncTask<Void, Void, String> {

         @Override
        protected String doInBackground(Void... voids) {
             try {
                URL url = new URL(GeneralConstants.URL+"/raport_categorii.php");
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
                try {

                    JSONArray vector = new JSONArray(s);
                    JSONObject obiect = vector.getJSONObject(0);

                     int nrAlimente = Integer.parseInt(obiect.getString("alimente"));
                     int nrHaine = Integer.parseInt(obiect.getString("haine"));
                     int nrAltele = Integer.parseInt(obiect.getString("altele"));

                    entries.add(new Entry((float)(nrAlimente*100)/(nrAlimente+nrAltele+nrHaine),1));
                    entries.add(new Entry((float)(nrHaine*100)/(nrAlimente+nrAltele+nrHaine),2));
                    entries.add(new Entry((float)(nrAltele*100)/(nrAlimente+nrAltele+nrHaine),3));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayList<String> xValori = new ArrayList<String>();
                xValori.add("alimente");
                xValori.add("haine");
                xValori.add("altele");

                PieDataSet set = new PieDataSet(entries, "");
                set.setColors(ColorTemplate.LIBERTY_COLORS);
                set.setValueTextSize(15);
                PieData data = new PieData(xValori,set);
                chart.setData(data);
                set.setValueTextSize(17);
                chart.setCenterText("% categorii tranzactii \nfinalizate");
                chart.setCenterTextSize(15);
                chart.setDescription("");
                chart.setDescriptionTextSize(19);
                chart.setDrawSliceText(false);
                chart.getLegend().setTextSize(16);
                chart.getLegend().setXEntrySpace(29);
                chart.getLegend().setFormToTextSpace(2);
                chart.getLegend().setPosition(Legend.LegendPosition.LEFT_OF_CHART_INSIDE);
                chart.invalidate();

        }}
    public class PreluareNumarAnunturi extends  AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(GeneralConstants.URL+"/cerere_anunturi.php");
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
            try {

                JSONArray vector = new JSONArray(s);
                JSONObject obiect = vector.getJSONObject(0);

                int nrCAlimente = Integer.parseInt(obiect.getString("cerere_alimente"));
                int nrCHaine = Integer.parseInt(obiect.getString("cerere_haine"));
                int nrCAltele = Integer.parseInt(obiect.getString("cerere_altele"));
                valoriCereri.add(nrCAlimente);
                valoriCereri.add(nrCAltele);
                valoriCereri.add(nrCHaine);
                int nrOAlimente = Integer.parseInt(obiect.getString("oferta_alimente"));
                int nrOHaine = Integer.parseInt(obiect.getString("oferta_haine"));
                int nrOAltele = Integer.parseInt(obiect.getString("oferta_altele"));
                valoriOferte.add(nrOAlimente);
                valoriOferte.add(nrOAltele);
                valoriOferte.add(nrOHaine);
             }
            catch (JSONException e) {
                e.printStackTrace();
            }

                graficAnunturi.setDrawBarShadow(false);
                graficAnunturi.setDrawValueAboveBar(true);
                graficAnunturi.setDescription("");
                graficAnunturi.setMaxVisibleValueCount(50);
                graficAnunturi.setPinchZoom(false);
                graficAnunturi.setDrawGridBackground(false);

               ArrayList<BarDataSet> barDataSet = null;

                ArrayList<BarEntry> valueSetCereri = new ArrayList<>();
                for(int i=0;i<valoriCereri.size();i++){
                    BarEntry entry = new BarEntry(valoriCereri.get(i), i);
                    valueSetCereri.add(entry);
                }

                ArrayList<BarEntry> valueSetOferte = new ArrayList<>();
                for(int i=0;i<valoriOferte.size();i++){
                    BarEntry entry = new BarEntry(valoriOferte.get(i), i);
                    valueSetOferte.add(entry);
                }

                BarDataSet barDataOferte = new BarDataSet(valueSetOferte, "oferte");
                barDataOferte.setColor(Color.rgb(104, 241, 175));
                BarDataSet barDataCereri = new BarDataSet(valueSetCereri, "cereri");
                barDataCereri.setColor(Color.rgb(249, 162, 180));

                barDataSet = new ArrayList<>();
                barDataSet.add(barDataOferte);
                barDataSet.add(barDataCereri);

            ArrayList<String> xAxis = new ArrayList<>();
            xAxis.add("Alimente");
            xAxis.add("Haine");
            xAxis.add("Altele");

            BarData data = new BarData(xAxis, barDataSet);
            data.setValueTextSize(0);


            graficAnunturi.setData(data);
            graficAnunturi.setDescription("");
            graficAnunturi.animateXY(2000, 2000);
            graficAnunturi.getLegend().setTextSize(15);
            graficAnunturi.getLegend().setXEntrySpace(25);
            graficAnunturi.invalidate();
        }
    }
}
