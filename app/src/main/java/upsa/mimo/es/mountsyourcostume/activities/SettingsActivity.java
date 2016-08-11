package upsa.mimo.es.mountsyourcostume.activities;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.fragments.InfoFragment;
import upsa.mimo.es.mountsyourcostume.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    public static final String FLAG_FRAGMENT = "FLAG_FRAGMENT";
    public static final int FLAG_SETTINGS_FRAGMENT = 1;
    public static final int FLAG_INFO_FRAGMENT = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        // Display the fragment as the main content.
        initToolbar();
        int flag = getIntent().getIntExtra(FLAG_FRAGMENT,0);

        if(flag==FLAG_SETTINGS_FRAGMENT) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame_settings, SettingsFragment.newInstance())
                    .commit();
        }
        else if(flag==FLAG_INFO_FRAGMENT){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame_settings, InfoFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.transition.custom_slide_right,R.transition.custom_slide_left);
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
        setTitle(getString(R.string.settings));
    }
}

