package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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
import eco.org.greenapp.eco.org.greenapp.adapters.ReviewAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Review;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

public class RaportCategorii extends AppCompatActivity {
PieChart chart;
List<Entry> entries;
int nrAlimente, nrHaine, nrAltele;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport_categorii);
        chart = (PieChart)findViewById(R.id.pieChart);
        entries = new ArrayList<>();

       PreluareRaportCategorii raport = new PreluareRaportCategorii();
       raport.execute();

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
                set.setColors(ColorTemplate.JOYFUL_COLORS);
                set.setValueTextSize(15);
                PieData data = new PieData(xValori,set);
                chart.setData(data);
                set.setValueTextSize(17);
                chart.setCenterText("%");
                chart.setCenterTextSize(30);
                chart.setDescription("");
                chart.setDescriptionTextSize(19);
                chart.setDrawSliceText(false);
                chart.invalidate();

        }}

}
