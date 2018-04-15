package eco.org.greenapp.eco.org.greenapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import eco.org.greenapp.eco.org.greenapp.classes.User;

/**
 * Created by danan on 4/15/2018.
 */

public class UsersAdapter extends ArrayAdapter<User> {

    Context context;
    int idLayout;
    ArrayList<User> listaUsers;
    public UsersAdapter( Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.idLayout = resource;
        listaUsers = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
