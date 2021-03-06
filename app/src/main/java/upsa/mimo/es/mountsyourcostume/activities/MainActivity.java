package upsa.mimo.es.mountsyourcostume.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;

import butterknife.BindView;
import butterknife.ButterKnife;
import upsa.mimo.es.mountsyourcostume.BuildConfig;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.fragments.FavouriteCostumeFragment;
import upsa.mimo.es.mountsyourcostume.fragments.SaveCostumeFragment;
import upsa.mimo.es.mountsyourcostume.fragments.SearchCostumeFragment;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MAIN_ACTIVITY";

    private int flagLoggin;

    //For login with google+
    private GoogleApiClient googleApiClient;

    //For savedInstance
    private String title = "Favourite";
    private static final String KEY_TITLE = "title";

    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    //For drawer layout
    private TextView textViewNavigationHeaderName;
    private TextView textViewNavigationHeaderEmail;

    private ImageView imageViewNavigationHeaderLoggin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initGooglePlus();
        loadUI();

        initFragmentAndTitle(savedInstanceState);

    }

    private void loadUI(){
        loadToolBar();

        if(navigationView!=null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        }
    }

    private void initFragmentAndTitle(Bundle savedInstanceState){
        if(savedInstanceState==null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FavouriteCostumeFragment mainFragment = FavouriteCostumeFragment.newInstance();
            ft.replace(R.id.main_frame, mainFragment);
            ft.commit();

        }
        else{
            if(savedInstanceState.getString(KEY_TITLE)!=null){
                title =savedInstanceState.getString(KEY_TITLE);
            }
        }
        setTitle(title);
    }

    private void loadNavigationHeader() {

        textViewNavigationHeaderName = (TextView) findViewById(R.id.text_view_navigation_header_name);
        textViewNavigationHeaderEmail = (TextView) findViewById(R.id.text_view_navigation_header_email);

        imageViewNavigationHeaderLoggin = (ImageView) findViewById(R.id.image_view_social_loggin_navigation_header);

        String name = MyApplication.getUser().getName();

        String email = MyApplication.getUser().getTokenForBD();

        String url_image = MyApplication.getUser().getPhotoURL();

        this.flagLoggin = MyApplication.getUser().getSocialNetwork();

        if(name!=null){
            textViewNavigationHeaderName.setText(name);
        }
        if(email!=null){
            textViewNavigationHeaderEmail.setText(email);
        }

        if(url_image!=null){
            setupImageProfile(url_image);
        }
        if(this.flagLoggin==LogginActivity.FLAG_TWITTER){
            imageViewNavigationHeaderLoggin.setImageResource(R.drawable.twitter_logo);
        }
        else if(this.flagLoggin==LogginActivity.FLAG_GOGGLEPLUS){
            imageViewNavigationHeaderLoggin.setImageResource(R.drawable.googleplus_logo);
        }

    }
    private void setupImageProfile(String imageUrl) {
        Picasso.with(this).load(imageUrl).fit().centerCrop().into((ImageView)findViewById(R.id.image_view_navigation_header));
    }

    private void loadToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(new DrawerArrowDrawable(toolbar.getContext()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings,menu);
        MenuItem itemPremium = menu.findItem(R.id.info_settings);
        itemPremium.setVisible(BuildConfig.FLAVOR.equals("premium"));
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                loadNavigationHeader();
                return true;
            case R.id.item1:
                fragment = FavouriteCostumeFragment.newInstance();
                break;
            case R.id.item2:
                fragment = SaveCostumeFragment.newInstance();
                break;
            case R.id.item3:
                fragment = SearchCostumeFragment.newInstance();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                intent.putExtra(SettingsActivity.FLAG_FRAGMENT,SettingsActivity.FLAG_SETTINGS_FRAGMENT);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                ActivityCompat.startActivity(MainActivity.this,intent,options.toBundle());
                return true;
            case R.id.info_settings:
                Intent intent1 = new Intent(MainActivity.this,SettingsActivity.class);
                intent1.putExtra(SettingsActivity.FLAG_FRAGMENT,SettingsActivity.FLAG_INFO_FRAGMENT);
                ActivityOptionsCompat options1 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                ActivityCompat.startActivity(MainActivity.this,intent1,options1.toBundle());
                return true;

        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Se pone primero el setcustomanimation
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
        setTitle(item.getTitle());
        title= item.getTitle().toString();
        drawer.closeDrawers();
        return true;

    }

    //For login with google+
    private void initGooglePlus() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    //Method for button unloggin
    public void logOut(View view){
        createAlertDialogForLogOut();

    }

    private void revokeAccessGooglePlus() {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        signOut();
                    }
                });
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent login = new Intent(MainActivity.this, LogginActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    private void revokeAccessTwitter(){

        Twitter.getInstance();
        Twitter.logOut();
        Intent login = new Intent(MainActivity.this, LogginActivity.class);
        startActivity(login);
        finish();
    }

    private void createAlertDialogForLogOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false)
                .setTitle(R.string.log_out)
                .setMessage(R.string.would_you_cancel_session)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(flagLoggin==LogginActivity.FLAG_GOGGLEPLUS) {
                            revokeAccessGooglePlus();
                        }
                        else if(flagLoggin==LogginActivity.FLAG_TWITTER){
                            revokeAccessTwitter();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawers();
        }
        else {
            super.onBackPressed();
        }

    }

}
