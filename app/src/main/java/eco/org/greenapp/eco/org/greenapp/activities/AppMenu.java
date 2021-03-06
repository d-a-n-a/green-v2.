package eco.org.greenapp.eco.org.greenapp.activities;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.GetImageTask;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentOne;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentThree;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentTwo;

public class AppMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;
    private FragmentOne frone;
    private FragmentTwo frtwo;
    private FragmentThree frthree;
    private ImageView imageView;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 55 && resultCode== Activity.RESULT_OK) {
            setFragment(new FragmentOne());
            Toast.makeText(getApplicationContext(), "sdf", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(SharedPreferencesConstants.ABOUT,null).isEmpty() && sharedPreferences.getString(SharedPreferencesConstants.STREET,null).equals("null")){

         AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Pentru a putea utiliza functionalitatile trebuie sa completati toate datele de la profilul personal.")
                    .setCancelable(false)
                    .setPositiveButton("Du-ma la Setari profil", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           startActivity(new Intent(getApplicationContext(), ProfileSettings.class));
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {

        SharedPreferences sharedPreferences = getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesConstants.COMPLETE_REGISTER, "complet");
        editor.apply();

}

        mainNav = (BottomNavigationView)findViewById(R.id.main_nav);
        mainFrame = (FrameLayout)findViewById(R.id.main_frame);
        frone = new FragmentOne();
        frtwo = new FragmentTwo();
        frthree = new FragmentThree();

        setFragment(frone);
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_one:
                        setFragment(frone);
                        return true;

                    case R.id.nav_two:
                         AlertDialog.Builder builder = new AlertDialog.Builder(AppMenu.this);
                        builder.setMessage("Ce tip de anunț doriți să introduceți?")
                                .setPositiveButton("Oferta", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivityForResult(new Intent(getApplicationContext(), AddProduct.class), 55);
                                    }
                                })
                                .setNegativeButton("Cerere", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(getApplicationContext(), AddDemandProduct.class), 55);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;

                    case R.id.nav_three:
                         startActivity(new Intent(getApplicationContext(), FilterFindAds.class));
                        return true;

                    default:
                        return false;
                }
            }
        });




        View navheader = navigationView.getHeaderView(0);
        imageView = (ImageView)navheader.findViewById(R.id.imageViewUser);
         new GetUrlPhoto().execute(getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE).getString(GeneralConstants.TOKEN,null));
        ((TextView)navheader.findViewById(R.id.myusername)).setText(getSharedPreferences(
                GeneralConstants.SESSION, Context.MODE_PRIVATE
        ).getString(GeneralConstants.TOKEN, null));
     }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

         if (id == R.id.findUsersByLocation) {
            startActivity(new Intent(getApplicationContext(), UsersByLocation.class));
            return true;
        }
else
        if (id == R.id.idTopUsers) {
            startActivity(new Intent(getApplicationContext(), TopUtilizatori.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.nav_about: {
startActivity(new Intent(getApplicationContext(), AboutApp.class));
                break;
            }
            case R.id.nav_profile: {
                startActivity(new Intent(getApplicationContext(),MyProfile.class));
                break;
            }
            case R.id.nav_home: {
                 setFragment(frone);
                break;
            }
            case R.id.nav_notifications: {
                startActivity(new Intent(getApplicationContext(), Notificari.class));
                break;
            }
            case R.id.nav_settings: {
                startActivity(new Intent(getApplicationContext(),ProfileSettings.class));
                break;
            }
            case R.id.nav_logout: {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
                break;
            }
            case R.id.nav_help: {
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,"contact@green.app");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact ajutor ");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(),"Aplicatia Gmail nu este instalata pe dispozitiv.",Toast.LENGTH_SHORT).show();
                break;
            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setFragment(Fragment fr){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    public class GetUrlPhoto extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

                String username;
                try {
                    username = strings[0];
                    URL url = new URL(GeneralConstants.URL+"/selectare_fotografie_profil.php");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();


                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String usernamepost = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    bufferedWriter.write(usernamepost);

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"));
                    String result;

                    StringBuilder stringBuilder = new StringBuilder();

                    while ((result = bufferedReader.readLine()) != null) {
                        stringBuilder.append(result);
                    }
                    con.disconnect();
                    return stringBuilder.toString();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                 if (s != null)
                {
                      GetImageTask getImageTask = new GetImageTask(imageView, getApplicationContext());
                      getImageTask.execute(GeneralConstants.Url+s);

                }
            }

        }
    }