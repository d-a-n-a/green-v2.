package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.GetImageTask;
import eco.org.greenapp.eco.org.greenapp.activities.AdForProduct;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.User;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

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


    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(this.idResource,parent, false);

        TextView titlu = (TextView)view.findViewById(R.id.adTitle);
        TextView tip = (TextView)view.findViewById(R.id.adType);
        TextView dataIntroducerii = (TextView)view.findViewById(R.id.dataIntroducerii);
        TextView status = (TextView)view.findViewById(R.id.statusInfo);
        LinearLayout optiuni = (LinearLayout)view.findViewById(R.id.adOptions);
        ImageView imgv = (ImageView)view.findViewById(R.id.productPhoto);

        final Advertisement ad = this.ads.get(position);
        titlu.setText(ad.getProdus().getDenumireProdus());
        tip.setText(ad.getTip());
        dataIntroducerii.setText(ad.getDataPostarii());
        status.setText(ad.getStatusAnunt().getTip());
        if(ad.getTip().equals("doresc"))
            imgv.setImageDrawable(getContext().getDrawable(R.drawable.wanted));
        if(!ad.getProdus().getUrl().isEmpty()){
            GetImageTask getImageTask = new GetImageTask(imgv, getContext());
            getImageTask.execute(GeneralConstants.Url+ad.getProdus().getUrl());
        }
        //daca nu e al meu atunci ii dau clickc atre adforproduct
       /* String me = getContext().getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                .getString(GeneralConstants.TOKEN, null);
        if(!ad.getUsername().equals(me))
        {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AdForProduct.class);
                    intent.putExtra("selectedAd", ad);
                    getContext().startActivity(intent);
                }
            });
        }*/
        return view;
    }
}
