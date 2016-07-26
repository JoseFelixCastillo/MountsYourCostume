package upsa.mimo.es.mountsyourcostume.activities;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
        Log.d(TAG, "entro initPreferences");
     /*   String theme = shared.getString(SettingsFragment.KEY_PREF_SYNC_THEME,"nothing");
       // getResources().getStringArray(R.array.)
        if(theme.equals(SettingsFragment.KEY_PREF_THEME_NORMAL)){
            Log.d(TAG, "style normal");
            setTheme(R.style.AppTheme);
        }
        else if(theme.equals(SettingsFragment.KEY_PREF_THEME_ORANGE)){
            Log.d(TAG, "style naranja");
            setTheme(R.style.AppTheme2);
        }*/

        boolean orientation = shared.getBoolean(SettingsFragment.KEY_PREF_SYNC_ROTATION,true);
        if(!orientation){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
