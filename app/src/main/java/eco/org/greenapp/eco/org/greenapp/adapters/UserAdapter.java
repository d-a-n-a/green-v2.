package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

import eco.org.greenapp.eco.org.greenapp.classes.Advertisement;
import eco.org.greenapp.eco.org.greenapp.classes.User;

/**
 * Created by danan on 4/2/2018.
 */

public class UserAdapter extends ArrayAdapter<User> {
    private int idResource;
    private List<User> users;
    public UserAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.idResource = resource;
        this.users = objects;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(this.idResource,parent, false);

        TextView denumire;


        User user = this.users.get(position);
        denumire.setText(user.getDenumireProdus());

        return view;
    }
}
