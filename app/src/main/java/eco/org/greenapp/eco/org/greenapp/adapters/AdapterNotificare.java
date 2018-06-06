package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.GetImageTask;
import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.Notificare;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;

/**
 * Created by danan on 5/2/2018.
 */

public class AdapterNotificare extends ArrayAdapter<Notificare> {
    Context context;
    int layout;
    List<Notificare> notificari;


    public AdapterNotificare(@NonNull Context context, int resource, @NonNull List<Notificare> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.notificari = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(this.layout, parent, false);

        TextView data = (TextView)view.findViewById(R.id.txtData);
        TextView continut = (TextView)view.findViewById(R.id.textViewContinut);

        Notificare notificare = this.notificari.get(position);
        data.setText(GeneralConstants.SDF.format(notificare.getData()));
        continut.setText(notificare.getDescriere());

        return view;
    }


}
