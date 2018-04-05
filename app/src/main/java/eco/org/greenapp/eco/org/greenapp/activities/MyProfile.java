 package eco.org.greenapp.eco.org.greenapp.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentMyGeneralUserInfo;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentMyAds;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentMyReviews;
import eco.org.greenapp.eco.org.greenapp.fragments.TransactionHistoryFragment;

 public class MyProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_settings);
        setContentView(R.layout.activity_my_profile);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentArea,new FragmentMyGeneralUserInfo());
        fragmentTransaction.commit();

        ((Button)findViewById(R.id.btnSettings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileSettings.class));
            }
        });

    }
    public void selectionOfFragment(View view){
        ((Button)findViewById(R.id.button3)).setBackgroundResource(R.drawable.custom_button_profile_options);
        ((Button)findViewById(R.id.btnTakeProduct)).setBackgroundResource(R.drawable.custom_button_profile_options);
        ((Button)findViewById(R.id.button5)).setBackgroundResource(R.drawable.custom_button_profile_options);
        ((Button)findViewById(R.id.btnTransactionHistory)).setBackgroundResource(R.drawable.custom_button_profile_options);
         Fragment fragment = new Fragment();
        if(view == findViewById(R.id.button5)){
            fragment =  new FragmentMyGeneralUserInfo();
        }
        else
        if(view == findViewById(R.id.button3)){
            fragment = new FragmentMyAds();

        }
        else if(view == findViewById(R.id.btnTakeProduct))
        {
            fragment = new FragmentMyReviews();
        }
        else
            if(view == findViewById(R.id.btnTransactionHistory)){
            fragment = new TransactionHistoryFragment();
        }
        ((LinearLayout)findViewById(R.id.hiddenLayout)).setVisibility(View.INVISIBLE);
        view.setBackgroundResource(R.drawable.custom_button_profile_options_selected);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentArea,fragment);
        fragmentTransaction.commit();
    }
}