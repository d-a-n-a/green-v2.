package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.adapters.UsersAdapter;
import eco.org.greenapp.eco.org.greenapp.classes.User;

public class UsersFilterList extends AppCompatActivity {

    ListView listView;
    List<User> lista;
    UsersAdapter adapter;
    Intent intent;
    int ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_filter_list);

        lista = new ArrayList<>();

        listView = (ListView)findViewById(R.id.idListViewUsers);
        intent = getIntent();
        if(intent != null){
            ok = 1;
            lista = (List<User>) intent.getSerializableExtra("listaUtilizatori");
        }

        adapter = new UsersAdapter(getApplicationContext(), R.layout.user_item,lista);
        listView.setAdapter(adapter);
    }
}
