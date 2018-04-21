package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentOne;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentThree;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentTwo;

public class Home extends AppCompatActivity {
    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;
    private FragmentOne frone;
    private FragmentTwo frtwo;
    private FragmentThree frthree;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    private static final String SESSION = "Sesiune-User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);
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
                        setFragment(frtwo);
                        return true;

                    case R.id.nav_three:
                        //setFragment(frthree);
                        startActivity(new Intent(getApplicationContext(), FilterFindUsers.class));
                        return false;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fr){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fr);
        fragmentTransaction.commit();

    }
}
