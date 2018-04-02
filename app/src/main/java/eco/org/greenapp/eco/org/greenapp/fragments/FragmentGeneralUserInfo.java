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

import eco.org.greenapp.R;
import eco.org.greenapp.eco.org.greenapp.constants.GeneralConstants;
import eco.org.greenapp.eco.org.greenapp.constants.SharedPreferencesConstants;


public class FragmentGeneralUserInfo extends Fragment {
EditText etBiography;
EditText etLocation;
EditText etDate;
SharedPreferences sharedPreferences;

    public FragmentGeneralUserInfo(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        etBiography = (EditText)getView().findViewById(R.id.biographyUser);
        etLocation = (EditText) getView().findViewById(R.id.userKmDistance);
        etDate = (EditText) getView().findViewById(R.id.signUpDate);
        String s = sharedPreferences.getString(SharedPreferencesConstants.ABOUT, null);
        etBiography.setText(s);
    */
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


     /*   etBiography.setText(getActivity()
                   .getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                   .getString(SharedPreferencesConstants.ABOUT, null));*/
      /*  etLocation.setText(getActivity()
                  .getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
                  .getString(SharedPreferencesConstants.STREET, null));
        etDate.setText(getActivity()
              .getSharedPreferences(GeneralConstants.SESSION, Context.MODE_PRIVATE)
              .getString(SharedPreferencesConstants.REGISTER_DATE,null));*/

        return inflater.inflate(R.layout.fragment_fragment_general_user_info, container, false);


    }


}
