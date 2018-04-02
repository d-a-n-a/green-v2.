package eco.org.greenapp.eco.org.greenapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;


public class FragmentGeneralUserInfo extends Fragment {
    TextView etBiography;
    TextView etLocation;
    TextView etDate;
SharedPreferences sharedPreferences;
View view;
    public FragmentGeneralUserInfo(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null)
        {
            view=inflater.inflate(R.layout.fragment_fragment_general_user_info, container,false);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            // parent.removeView(view);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        etBiography = (TextView)view.findViewById(R.id.biographyUser);
        etLocation = (TextView) view.findViewById(R.id.userKmDistance);
        etDate = (TextView) view.findViewById(R.id.userSignUpDate);

        String s = sharedPreferences.getString(SharedPreferencesConstants.ABOUT, null);
        etBiography.setText(s);

        etBiography.setText(getActivity()
                   .getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                   .getString(SharedPreferencesConstants.ABOUT, null));
       etLocation.setText(getActivity()
                  .getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                  .getString(SharedPreferencesConstants.STREET, null));
        etDate.setText(getActivity()
              .getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
              .getString(SharedPreferencesConstants.REGISTER_DATE,null));

     //   return inflater.inflate(R.layout.fragment_fragment_general_user_info, container, false);
return view;

    }


}
