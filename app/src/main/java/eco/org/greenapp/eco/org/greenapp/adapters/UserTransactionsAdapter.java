package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.DeleteTask;
import eco.org.greenapp.eco.org.greenapp.activities.Feedback;
import eco.org.greenapp.eco.org.greenapp.classes.Transaction;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;
import eco.org.greenapp.eco.org.greenapp.enumerations.TipAnunt;

/**
 * Created by danan on 4/5/2018.
 */

public class UserTransactionsAdapter extends ArrayAdapter<Transaction> {
    private int idResource;
    private List<Transaction> transactions;
    private Context context;

    public UserTransactionsAdapter(Context context, int resource, List<Transaction> objects) {
        super(context, resource, objects);
        this.context = context;
        this.idResource = resource;
        this.transactions = objects;
    }
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        final View view = li.inflate(this.idResource,parent, false);

        TextView titlu = (TextView)view.findViewById(R.id.denumire);
        TextView user = (TextView)view.findViewById(R.id.user);
        TextView co = (TextView)view.findViewById(R.id.cerereoferta);
        TextView strada = (TextView)view.findViewById(R.id.strada);
        TextView dataora = (TextView)view.findViewById(R.id.data);
        final Transaction tr = this.transactions.get(position);
        TextView tvStatus = (TextView)view.findViewById(R.id.statusTranz);
        tvStatus.setText(tr.getStatus().getTip());

        titlu.setText(tr.getAnunt().getProdus().getDenumireProdus());
        final String me = this.getContext().getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN, null);
        if(me.equals(tr.getDestinatar()))
            user.setText(tr.getExpeditor());
        else
            user.setText(tr.getDestinatar());

        if(me.equals(tr.getExpeditor()))
            co.setText(TipAnunt.oferta.toString());
        else {
            co.setText("");
            ((Button)view.findViewById(R.id.btnConfirm)).setVisibility(View.INVISIBLE);
        }//nu are sens sa pun chiar cerere pentru ca nu e pe anuntul meu de cerere, e pe anuntul altcuiva de oferta
        strada.setText(tr.getLocatie());
        dataora.setText(tr.getData().toString()+" - "+tr.getOra());

        if(tr.getStatus().getTip().equals("anulat"))
        {

            ((Button)view.findViewById(R.id.btnConfirm)).setVisibility(View.INVISIBLE);
            ((Button)view.findViewById(R.id.btnCancel)).setVisibility(View.INVISIBLE);
            ((TextView)view.findViewById(R.id.statusTranz)).setVisibility(View.VISIBLE);

            view.setBackgroundColor(view.getResources().getColor(R.color.colorAccentMildMild));
            ((Button)view.findViewById(R.id.btnReview)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    alertDialog.setMessage("Sunteti sigur ca vreti sa eliminati definitiv aceasta tranzactie?");
                    alertDialog.setTitle("Eliminare tranzactie din istoric");

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            transactions.remove(position);
                            DeleteTask deleteTask = new  DeleteTask(view.getContext());
                            deleteTask.execute(tr.getIdTranzactie());
                        } });


                    alertDialog.setNegativeButton("Renunta", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                finalize();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } });
                    alertDialog.show();

                }
            });
        }
        else
            if(tr.getStatus().getTip().equals("finalizat")){
                ((Button)view.findViewById(R.id.btnConfirm)).setVisibility(View.INVISIBLE);
                ((Button)view.findViewById(R.id.btnCancel)).setVisibility(View.INVISIBLE);
                ((TextView)view.findViewById(R.id.statusTranz)).setVisibility(View.VISIBLE);
                //butonul pentru adaugare review este accesibil doar daca nu mai exista
                //un alt review catre acel user pe baza aceleiasi tranzactii
                CheckReview checkReview = new CheckReview(view);
                checkReview.execute( me, tr.getExpeditor(), ""+tr.getIdTranzactie());

               // view.setBackgroundColor(view.getResources().getColor(R.color.colorBackgound));

                ((Button)view.findViewById(R.id.btnReview)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                        alertDialog.setMessage("Doriti sa adaugati review pentru acest utilizator?");
                        alertDialog.setTitle("Review");

                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getContext(), Feedback.class);
                                intent.putExtra("user", tr.getExpeditor());
                                intent.putExtra("idTranzactie", tr.getIdTranzactie());
                                context.startActivity(intent);
                            } });


                        alertDialog.setNegativeButton("Renunta", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    finalize();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            } });
                        alertDialog.show();

                    }
                });
            }
            else {
                ((Button) view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditStatus editStatus = new EditStatus();
                        editStatus.execute(""+tr.getIdTranzactie(), ""+6, ""+tr.getAnunt().getId(), ""+3);

                    }
                });

                ((Button) view.findViewById(R.id.btnConfirm)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditStatus editStatus = new EditStatus();
                        editStatus.execute(""+tr.getIdTranzactie(), ""+7, ""+tr.getAnunt().getId(), ""+4);

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                        dialogBuilder.setView(R.layout.layout_confirmare_tranzactie);

                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();

                        ((Button) alertDialog.findViewById(R.id.btnAnulare)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                 alertDialog.dismiss();
                            }
                        });
                        ((Button) alertDialog.findViewById(R.id.btnNu)).setOnClickListener(new View.OnClickListener() {
                            //pe cazul asta, tranzactia este trecuta ca finalizata
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        ((Button) alertDialog.findViewById(R.id.btnDa)).setOnClickListener(new View.OnClickListener() {
                            //tranzactia se schimba pe finalizata si se porneste intent pentru activitatea de review
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getContext(), Feedback.class);
                                intent.putExtra("idTranzactie", tr.getIdTranzactie());
                                if (tr.getExpeditor().equals(me))
                                 intent.putExtra("user", tr.getDestinatar());
                                else
                                    intent.putExtra("user", tr.getExpeditor());
                                context.startActivity(intent);
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        return view;
    }
    public class EditStatus extends AsyncTask<String, Void, String> {
        String idTransaction;
        String idAd;
        String idnewStatusTransaction;
        String idNewStatusAd;
        @Override
        protected String doInBackground(String... strings) {
            idTransaction = strings[0];
            idnewStatusTransaction = strings[1];
            idAd = strings[2];
            idNewStatusAd = strings[3];
            try {
                URL url = new URL(GeneralConstants.URL+"/update_transaction_ad.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String updateValues = URLEncoder.encode("idTransaction", "UTF-8") + "=" + URLEncoder.encode(idTransaction, "UTF-8") + "&"
                        + URLEncoder.encode("idStatusTransaction", "UTF-8") + "=" + URLEncoder.encode(idnewStatusTransaction, "UTF-8") + "&"
                        + URLEncoder.encode("idAd", "UTF-8") + "=" + URLEncoder.encode(idAd, "UTF-8") + "&"
                        + URLEncoder.encode("idStatusAd", "UTF-8") + "=" + URLEncoder.encode(idNewStatusAd, "UTF-8");
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
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
        }
    }
    public class CheckReview extends AsyncTask<String, Void, String>{
View context;
CheckReview(View context){
    this.context = context;

}
String autor, user;
String idTranzactie;
        @Override
        protected String doInBackground(String... strings) {
            autor = strings[0];
            user = strings[1];
            idTranzactie = strings[2];
            URL url = null;
            try {
                url = new URL(GeneralConstants.URL+"/existenta_review.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String reviewData = URLEncoder.encode("autor", "UTF-8") + "=" + URLEncoder.encode(autor, "UTF-8") + "&"
                        + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("tranzactie", "UTF-8") + "=" + URLEncoder.encode(idTranzactie, "UTF-8");
                bufferedWriter.write(reviewData);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "iso-8859-1"));
                String result;

                StringBuilder stringBuilder = new StringBuilder();

                while((result = bufferedReader.readLine())!=null){
                    stringBuilder.append(result);
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
          String me = getContext().getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN, null);

           if(s.equals("success") && !me.equals(user))
           {
               ((Button) context.findViewById(R.id.btnReview)).setVisibility(View.VISIBLE);
           }
           else
               ((Button) context.findViewById(R.id.btnReview)).setVisibility(View.INVISIBLE);



        }
    }

}
