package eco.org.greenapp.eco.org.greenapp.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
 import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.activities.UserInfo;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;


/**
 * Created by danan on 4/5/2018.
 */

public class FragmentGeneralUserInfo extends Fragment {
    TextView etBiography;
    TextView etLocation;
    TextView etDate;
    List<Integer> valori = new ArrayList<>();
    View view;
    String username;
     HorizontalBarChart barChart;
    public FragmentGeneralUserInfo(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       Log.i("ww", ((UserInfo)this.getActivity()).nrReviews.toString() );
        if(view==null)
        {
            view=inflater.inflate(R.layout.fragment_fragment_general_user_info, container,false);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
         }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString("username",null);
        }
        SelectUserInfo selectUserInfo = new SelectUserInfo();
        selectUserInfo.execute(username);

    valori = ((UserInfo)this.getActivity()).nrReviews;

        // ----- grafic -----
        barChart = (HorizontalBarChart)view.findViewById(R.id.barChartH);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        barChart.getXAxis().setXOffset(-60);
        barChart.setData(data);
        barChart.setDescription("Număr de evaluări");
        barChart.setDescriptionTextSize(16);
        barChart.setDescriptionColor(Color.GREEN);
        barChart.setDescriptionPosition(0, 0);
        barChart.animateXY(2000, 2000);
        barChart.setDrawValueAboveBar(false);
        barChart.invalidate();
        return view;

    }
    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;


        ArrayList<BarEntry> valoriEvaluari = new ArrayList<>();
        BarEntry valori5Stele = new BarEntry(valori.get(4), 0);
        if(valori.get(4)>0)
        valoriEvaluari.add(valori5Stele);
        BarEntry valori4Stele = new BarEntry(valori.get(3), 1);
        if(valori.get(3)>0)
            valoriEvaluari.add(valori4Stele);
        BarEntry valori3Stele = new BarEntry(valori.get(2), 2);
        if(valori.get(2)>0)
            valoriEvaluari.add(valori3Stele);
        BarEntry valori2Stele = new BarEntry(valori.get(1), 3);
        if(valori.get(1)>0)
            valoriEvaluari.add(valori2Stele);
        BarEntry valori1Stele = new BarEntry(valori.get(0), 4);
        if(valori.get(0)>0)
            valoriEvaluari.add(valori1Stele);


        BarDataSet barDataSet2 = new BarDataSet(valoriEvaluari, "5 - 1");
        barDataSet2.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet2.setValueFormatter(new ValueFormatter() {
            private DecimalFormat mFormat = new DecimalFormat("###,###,##0");

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return mFormat.format(value);
            }
        });
        barDataSet2.setDrawValues(true);
        barDataSet2.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet2.setValueTextSize(16f);
         dataSets = new ArrayList<>();
        dataSets.add(barDataSet2);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("★5");
        xAxis.add("★4");
        xAxis.add("★3");
        xAxis.add("★2");
        xAxis.add("★1");
        return xAxis;
    }


    public class SelectUserInfo extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... strings) {
        String username;
        try {
            username = strings[0];
            URL url = new URL(GeneralConstants.URL+"/selectare_info_utilizator.php");
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
        super.onPostExecute(s);
        if (s != null) {
            try {
                JSONObject detalii = new JSONObject(s);

                etBiography = (TextView)view.findViewById(R.id.biographyUser);
                etLocation = (TextView)view.findViewById(R.id.userKmDistance);
                etDate = (TextView)view.findViewById(R.id.userSignUpDate);

                etBiography.setText(detalii.getString("bio"));
                etDate.setText(detalii.getString("data"));
                etLocation.setText(detalii.getString("locatie"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
 }
}
