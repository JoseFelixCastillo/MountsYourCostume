package upsa.mimo.es.mountsyourcostume.activities;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import upsa.mimo.es.mountsyourcostume.fragments.SettingsFragment;

/**
 * Created by User on 25/07/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSettingsPreferences();

    }

    private void initSettingsPreferences(){
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);

        boolean orientation = shared.getBoolean(SettingsFragment.KEY_PREF_SYNC_ROTATION,true);
        if(!orientation){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
