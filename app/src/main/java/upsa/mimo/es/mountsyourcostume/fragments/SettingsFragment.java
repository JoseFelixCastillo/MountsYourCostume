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
    public static final String KEY_PREF_SYNC_ROTATION = "KEY_PREF_SYNC_ROTATION";

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

      if(key.equals(KEY_PREF_SYNC_ROTATION)){
            updatePreferences(findPreference(key));
          //  Log.d(TAG,"entro para ver lo que hay shared");
        }
    }

    private void updatePreferences(Preference p) {
        if (p instanceof CheckBoxPreference) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }

    }
}
