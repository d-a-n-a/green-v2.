package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eco.org.greenapp.eco.org.greenapp.activities.FilterFindAds;


public class FragmentThree extends Fragment {


    public FragmentThree() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startActivity(new Intent(getContext(),FilterFindAds.class));

         return null;
        //return inflater.inflate(R.layout.fragment_fragment_three, container, false);
    }


}
