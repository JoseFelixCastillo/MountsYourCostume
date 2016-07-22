package upsa.mimo.es.mountsyourcostume.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        String bol = shared.getString(SettingsFragment.KEY_PREF_SYNC_THEME,"red");
        if(bol.equals("Naranja")){
            setTheme(R.style.AppTheme2);
        }
        //Log.d(TAG,"boolean es: " + bol);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.main_frame_settings, new SettingsFragment())
                .commit();
        initToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            ActivityCompat.finishAfterTransition(SettingsActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(SettingsActivity.this);
        super.onBackPressed();
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Settings");
    }
}

