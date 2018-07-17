package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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


public class FragmentMyReviews extends Fragment {


    private View view;
    private ListView lvReviews;
    private List<Review> lista;
    ImageView imageView;
    TextView textView;
     public FragmentMyReviews() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lista = new ArrayList<>();
        if(view==null)
        {
            view=inflater.inflate(R.layout.fragment_fragment_user_reviews, container,false);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            // parent.removeView(view);
        }

         imageView = (ImageView) view.findViewById(R.id.noReview);
         textView = (TextView) view.findViewById(R.id.noReviewText);
        imageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        getData();
        return view;
    }
    public void getData(){
        String username = getContext().getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN,null);
        GetReviews gd = new GetReviews();
        gd.execute(username);
}
public class GetReviews extends AsyncTask<String, Void, String>{

    String username;
    @Override
    protected String doInBackground(String... strings) {
        username = strings[0];
        try {
            URL url = new URL(GeneralConstants.URL+"/selectare_evaluari_utilizator.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String getByEmail = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
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
        super.onPostExecute(s);
        try {
            JSONArray vectorReviews = new JSONArray(s);
            for(int i=0; i<vectorReviews.length(); i++){
                JSONObject adItem = vectorReviews.getJSONObject(i);
                Review review = new Review();
                User user = new User();
                review.setData_adaugare(adItem.getString("data"));
                review.setContinut(adItem.getString("detalii"));
                review.setNota(Float.parseFloat(adItem.getString("nota")));
                user.setUsername(adItem.getString("username"));
                user.setUrl(adItem.getString("fotografie"));
                review.setUser(user);
                lista.add(review);
            }
            if(lista.size() == 0)
            {
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
            else {
                final ReviewAdapter adapter = new ReviewAdapter(getActivity(), R.layout.review_item, lista);
                lvReviews = (ListView) view.findViewById(R.id.idLvReview);
                lvReviews.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
}
