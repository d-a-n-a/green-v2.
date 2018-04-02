package eco.org.greenapp.eco.org.greenapp.activities;

import android.support.design.widget.FloatingActionButton;
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
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentGeneralUserInfo;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentUserAds;
import eco.org.greenapp.eco.org.greenapp.fragments.FragmentUserReviews;

public class UserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_info);

    }

    public void selectionOfFragment(View view){
        ((Button)findViewById(R.id.button3)).setBackgroundResource(R.drawable.button_user_info_normal);
        ((Button)findViewById(R.id.btnTakeProduct)).setBackgroundResource(R.drawable.button_user_info_normal);
        ((Button)findViewById(R.id.button5)).setBackgroundResource(R.drawable.button_user_info_normal);
        ((FloatingActionButton)findViewById(R.id.floatingMessage)).setVisibility(View.INVISIBLE);
        Fragment fragment = new Fragment();
        if(view == findViewById(R.id.button5)){
            fragment =  new FragmentGeneralUserInfo();
         }
        else
            if(view == findViewById(R.id.button3)){
            fragment = new FragmentUserAds();

             }
            else if(view == findViewById(R.id.btnTakeProduct))
            {
                fragment = new FragmentUserReviews();
             }
        ((LinearLayout)findViewById(R.id.hiddenLayout)).setVisibility(View.INVISIBLE);
        view.setBackgroundResource(R.drawable.button_user_info_accent);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentArea,fragment);
        fragmentTransaction.commit();
    }
}
