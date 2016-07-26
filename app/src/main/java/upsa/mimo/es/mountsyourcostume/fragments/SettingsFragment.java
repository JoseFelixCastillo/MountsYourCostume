package upsa.mimo.es.mountsyourcostume.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.util.Log;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.activities.SettingsActivity;

/**
 * Created by User on 18/07/2016.
 */


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = SettingsFragment.class.getSimpleName();
    public static final String KEY_PREF_SYNC_THEME = "KEY_PREF_SYNC_THEME";
    public static final String KEY_PREF_SYNC_ROTATION = "KEY_PREF_SYNC_ROTATION";
    public static final String KEY_PREF_THEME_NORMAL = "Normal";
    public static final String KEY_PREF_THEME_ORANGE = "Orange";

    public SettingsFragment(){

    }

    public static SettingsFragment newInstance(){
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_screen);

        //Esto para el primer activity
      //  PreferenceManager.setDefaultValues(getActivity(),R.xml.preference_screen,false);

        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
         //   initSummary(getPreferenceScreen().getPreference(i));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(KEY_PREF_SYNC_THEME)) {
            updatePreferences(findPreference(key));
            // Set summary to be the user-description for the selected value
          //  Log.d(TAG, connectionPref.setSummary(sharedPreferences.getString(key, "")));
        }
        else if(key.equals(KEY_PREF_SYNC_ROTATION)){
            updatePreferences(findPreference(key));
          //  Log.d(TAG,"entro para ver lo que hay shared");
        }
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory cat = (PreferenceCategory) p;
            for (int i = 0; i < cat.getPreferenceCount(); i++) {
                initSummary(cat.getPreference(i));
            }
        } else {
            updatePreferences(p);
        }
    }

    private void updatePreferences(Preference p) {
        if (p instanceof CheckBoxPreference) {
            Log.d(TAG, "entro check");
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
           /* CheckBoxPreference checkBoxPreference = (CheckBoxPreference) p;
            if(checkBoxPreference.isChecked()){
                Log.d(TAG, "esta check");
                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            else{
                Log.d(TAG, "no esta check");
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
*/

         //   p.setSummary(editTextPref.getText());
        }
        else if(p instanceof ListPreference){

            ListPreference listPreference = (ListPreference) p;
            //listPreference.setSummary("h");
            Log.d(TAG, "lista item: " + listPreference.getValue());
            /*Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);*/
        }

    }
}
