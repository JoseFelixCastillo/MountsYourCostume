package upsa.mimo.es.mountsyourcostume.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import upsa.mimo.es.mountsyourcostume.R;

/**
 * Created by User on 18/07/2016.
 */


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_screen);
    }
}
