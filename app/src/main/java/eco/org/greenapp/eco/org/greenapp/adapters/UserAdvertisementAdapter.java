package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.User;

/**
 * Created by danan on 4/2/2018.
 */

public class UserAdvertisementAdapter extends ArrayAdapter<Advertisement> {
    private int idResource;
    private List<Advertisement> ads;

    public UserAdvertisementAdapter(Context context, int resource, List<Advertisement> objects) {
        super(context, resource, objects);
        this.idResource = resource;
        this.ads = objects;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(this.idResource,parent, false);

        TextView titlu = (TextView)view.findViewById(R.id.adTitle);
        TextView tip = (TextView)view.findViewById(R.id.adType);
        TextView dataIntroducerii = (TextView)view.findViewById(R.id.dataIntroducerii);
        TextView status = (TextView)view.findViewById(R.id.statusInfo);

        Advertisement ad = this.ads.get(position);
        titlu.setText(ad.getDenumireProdus());
        tip.setText(ad.getTip());
        dataIntroducerii.setText(ad.getDataPostarii());
        status.setText(ad.getStatusAnunt());

        return view;
    }
}