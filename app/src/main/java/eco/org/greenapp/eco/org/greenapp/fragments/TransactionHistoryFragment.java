package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Context;
import android.net.Uri;
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
import eco.org.greenapp.eco.org.greenapp.adapters.UserAdvertisementAdapter;
import eco.org.greenapp.eco.org.greenapp.adapters.UserTransactionsAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Transaction;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

public class TransactionHistoryFragment extends Fragment {

    private View view;
    private ListView lvTransactions;
    private List<Transaction> transactions;
    SwipeRefreshLayout swipeContainer;
    UserTransactionsAdapter adapter;

    public TransactionHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        transactions = new ArrayList<>();
        if(view==null)
        {
            view=inflater.inflate(R.layout.fragment_transaction_history, container,false);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            // parent.removeView(view); //asta e posibil sa faca probleme
        }

        GetUserTransactions getUserTransactions = new GetUserTransactions();
        getUserTransactions.execute();


        adapter = new UserTransactionsAdapter(getActivity(),R.layout.transaction_item,transactions);
        adapter.notifyDataSetChanged();
        lvTransactions=(ListView)view.findViewById(R.id.idLvTransactions);
        lvTransactions.setAdapter(adapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.idSwipeTransactions);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                transactions.clear();
                adapter.notifyDataSetChanged();
                GetUserTransactions getUserTransactions = new GetUserTransactions();
                getUserTransactions.execute();
                swipeContainer.setRefreshing(false);



            }
        });
        return view;
    }
    public  class GetUserTransactions extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(GeneralConstants.URL+"/select_transactions.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();



                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream outputStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String username = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(getActivity().getSharedPreferences(
                        GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN,null), "UTF-8");
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
        JSONArray vectorTransactions = new JSONArray(s);
        for (int i = 0; i < vectorTransactions.length(); i++) {
            JSONObject transaction = vectorTransactions.getJSONObject(i);
            Transaction ad = new Transaction();
            ad.setData(transaction.getString("data_predare"));
            ad.setLocatie(transaction.getString("locatie_predare"));
            ad.setOra(transaction.getString("ora_predare"));
            ad.setExpeditor(transaction.getString("username_expeditor"));
            ad.setDestinatar(transaction.getString("username_destinatar"));
            ad.setDenumire(transaction.getString("denumire"));
            ad.setIdAnunt(Integer.parseInt(transaction.getString("ID_ANUNT")));
            ad.setIdTranzactie(Integer.parseInt(transaction.getString("ID_TRANZACTIE")));
            ad.setStatus(transaction.getString("status"));
            transactions.add(ad);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
}
        }
    }

}
