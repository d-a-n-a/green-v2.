package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Transaction;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

/**
 * Created by danan on 4/5/2018.
 */

public class UserTransactionsAdapter extends ArrayAdapter<Transaction> {
    private int idResource;
    private List<Transaction> transactions;

    public UserTransactionsAdapter(Context context, int resource, List<Transaction> objects) {
        super(context, resource, objects);
        this.idResource = resource;
        this.transactions = objects;
    }
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        final View view = li.inflate(this.idResource,parent, false);

        TextView titlu = (TextView)view.findViewById(R.id.denumire);
        TextView user = (TextView)view.findViewById(R.id.user);
        TextView co = (TextView)view.findViewById(R.id.cerereoferta);
        TextView strada = (TextView)view.findViewById(R.id.strada);
        TextView dataora = (TextView)view.findViewById(R.id.data);
        Transaction tr = this.transactions.get(position);

        titlu.setText(tr.getDenumire());
        String me = this.getContext().getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN, null);
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

        ((Button)view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            view.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
            view.setAlpha(0.3f);//dar si cand le preiau din baza de date trebuie sa fac ceva in asa fel incat sa apara cu fundal
            }
        });

        ((Button)view.findViewById(R.id.btnConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }


}
