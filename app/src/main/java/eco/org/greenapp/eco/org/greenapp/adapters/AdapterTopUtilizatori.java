package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.classes.UserWithReview;

/**
 * Created by danan on 6/17/2018.
 */

public class AdapterTopUtilizatori extends ArrayAdapter<UserWithReview> {

    private int idLayout;
    private List<UserWithReview> lista;
    public AdapterTopUtilizatori(@NonNull Context context, int resource, List<UserWithReview> utilizatori) {
        super(context, resource, utilizatori);
        this.idLayout = resource;
        this.lista = utilizatori;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(this.idLayout,parent, false);

        TextView username = (TextView)view.findViewById(R.id.usernameTop);
        TextView locatie = (TextView)view.findViewById(R.id.locatieTop);
        TextView rating = (TextView)view.findViewById(R.id.noStar);
        TextView nr = (TextView)view.findViewById(R.id.noTop);

        UserWithReview ad = this.lista.get(position);
        username.setText(ad.getUsername());
        locatie.setText(ad.getLocatie().getStrada()+", "+ad.getLocatie().getOras());
        rating.setText(""+ad.getMedieReviews()+"★");
        if(position == 0)
            nr.setText("1");
        else
            if(position == 1)
                nr.setText("2");
            else
                if(position==2)
                    nr.setText("3");
        return view;
    }
}
