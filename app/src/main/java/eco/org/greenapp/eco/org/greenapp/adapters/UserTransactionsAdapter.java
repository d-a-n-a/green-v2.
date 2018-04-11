package eco.org.greenapp.eco.org.greenapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.DeleteTask;
import eco.org.greenapp.eco.org.greenapp.activities.Feedback;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Review;
import eco.org.greenapp.eco.org.greenapp.classes.Transaction;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;

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

        titlu.setText(tr.getDenumire());
        final String me = this.getContext().getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN, null);
        if(me.equals(tr.getDestinatar()))
            user.setText(tr.getExpeditor());
        else
            user.setText(tr.getDestinatar());

        if(me.equals(tr.getDestinatar()))
            co.setText("cerere");
        else
            co.setText("oferta");
        strada.setText(tr.getLocatie());
        dataora.setText(tr.getData().toString()+" - "+tr.getOra());

        if(tr.getStatus().equals("incurs"))
        {
            ((Button)view.findViewById(R.id.btnConfirm)).setVisibility(View.INVISIBLE);
            ((Button)view.findViewById(R.id.btnCancel)).setVisibility(View.INVISIBLE);
            ((Button)view.findViewById(R.id.btnEliminare)).setVisibility(View.VISIBLE);

            view.setBackgroundColor(view.getResources().getColor(R.color.colorBackgound));
            ((Button)view.findViewById(R.id.btnEliminare)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    alertDialog.setMessage("Sunteti sigur ca vreti sa eliminati definitiv aceasta tranzactie?");
                    alertDialog.setTitle("Eliminare anunt");

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
        ((Button)view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //  view.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
           // view.setAlpha(0.3f);//dar si cand le preiau din baza de date trebuie sa fac ceva in asa fel incat sa apara cu fundal
            }
        });

        ((Button)view.findViewById(R.id.btnConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//de aici pornesc review pe OK
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                dialogBuilder.setView( R.layout.layout_confirmare_tranzactie);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                ((Button)alertDialog.findViewById(R.id.btnAnulare)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //aici doar se inchide fereastra
                        alertDialog.dismiss();
                    }
                });
                ((Button)alertDialog.findViewById(R.id.btnNu)).setOnClickListener(new View.OnClickListener() {
                    //pe cazul asta, tranzactia este trecuta ca finalizata
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                ((Button)alertDialog.findViewById(R.id.btnDa)).setOnClickListener(new View.OnClickListener() {
                   //tranzactia se schimba pe finalizata si se porneste intent pentru activitatea de review
                    @Override
                    public void onClick(View v) {

                        Intent intent =  new Intent(getContext(), Feedback.class);
                        if(tr.getExpeditor().equals(me))
                            intent.putExtra("user",tr.getDestinatar());
                        else
                            intent.putExtra("user", tr.getExpeditor());
                        context.startActivity(intent);
                     }
                });
            }
        });
        return view;
    }


}
