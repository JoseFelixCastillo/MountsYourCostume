package upsa.mimo.es.mountsyourcostume.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.fragments.FavouriteCostume;
import upsa.mimo.es.mountsyourcostume.fragments.MainFragment;
import upsa.mimo.es.mountsyourcostume.fragments.SaveCostume;
import upsa.mimo.es.mountsyourcostume.fragments.SearchCostume;
import upsa.mimo.es.mountsyourcostume.helpers.Pages;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MAIN_ACTIVITY";

    private int flagLoggin;

    private ViewGroup container;
    private DrawerLayout drawer;

    //For drawer layout
   // private ImageView imageViewNavigationHeader;
    private TextView textViewNavigationHeaderName;
   // private TextView textViewNavigationHeaderEmail;
    private ImageView imageViewNavigationHeaderLoggin;

    //For login with google+
    private GoogleApiClient googleApiClient;
    private static final String IS_DRAWER = "isDrawer";

    //For savedInstance
    private String title = "Favourite";
    private static final String KEY_TITLE = "title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGooglePlus();
        loadUI();

        setFragmentAndTitle(savedInstanceState);

    }

    private void loadUI(){

        container = (ViewGroup) findViewById(R.id.container);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        setListenerToDrawer(drawer);

        loadToolBar();



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if(navigationView!=null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        }

       // loadNavigationHeaderWithIntent();

    }

    private void setFragmentAndTitle(Bundle savedInstanceState){
        //MainFragment mainFragment = MainFragment.newInstance();
        if(savedInstanceState==null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FavouriteCostume mainFragment = FavouriteCostume.newInstance();
            ft.replace(R.id.main_frame, mainFragment);
            ft.commit();
            //setTitle("Favourites");
            title = "Favourite";
        }
        else{
            if(savedInstanceState.getString(KEY_TITLE)!=null){
                title =savedInstanceState.getString(KEY_TITLE);
            }
        }
        setTitle(title);
    }

    private void loadNavigationHeaderWithIntent(Intent intent) {

        Log.d(TAG,"LLEGOOO al loadNavigationWithIntent");
        textViewNavigationHeaderName = (TextView) findViewById(R.id.text_view_navigation_header_name);
       // textViewNavigationHeaderEmail = (TextView) findViewById(R.id.text_view_navigation_header_email);

        imageViewNavigationHeaderLoggin = (ImageView) findViewById(R.id.image_view_social_loggin_navigation_header);

        String name = intent.getStringExtra(LogginActivity.LOGGIN_NAME);
       // String email = intent.getStringExtra(LogginActivity.LOGGIN_EMAIL);
        String url_image = intent.getStringExtra(LogginActivity.LOGGIN_URL_IMAGE);
        this.flagLoggin = intent.getIntExtra(LogginActivity.FLAG_LOGGIN,0);

       if(name!=null){
           Log.d(TAG, name);
           textViewNavigationHeaderName.setText(name);
       }
      //  if(email!=null){
      //      textViewNavigationHeaderEmail.setText(email);
      //  }
        if(url_image!=null){
            Log.d(TAG,url_image);
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

        //Ver
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(new DrawerArrowDrawable(toolbar.getContext()));
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                loadNavigationHeaderWithIntent(getIntent());
                return true;
            case R.id.item1:
                fragment = FavouriteCostume.newInstance();
                break;
            case R.id.item2:
                fragment = SaveCostume.newInstance();
                break;
            case R.id.item3:
                fragment = SearchCostume.newInstance();
                break;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        /// fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //fragmentTransaction.addToBackStack(null);
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
                .setTitle("Log Out")
                .setMessage("Would you cancel session")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(drawer.isDrawerOpen(GravityCompat.START)){
            Log.d(TAG,"cierro drawer");
            drawer.closeDrawers();
            outState.putBoolean(IS_DRAWER,true);
        }
        else{
            outState.putBoolean(IS_DRAWER,false);
        }

        outState.putString(KEY_TITLE,title);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        /*if(savedInstanceState!=null && savedInstanceState.getBoolean(IS_DRAWER)){
            drawer.openDrawer(GravityCompat.START);
            loadNavigationHeaderWithIntent(getIntent());
        }*/
        Log.d(TAG,"onRestore");
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){

        }

    }

    private void setListenerToDrawer(DrawerLayout drawer) {
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
               // Log.d(TAG, "llego al onDrawerSlide");
            }

            @Override
            public void onDrawerOpened(View drawerView) {

             //   Log.d(TAG, "llego al onDrawerOpened");
                loadNavigationHeaderWithIntent(getIntent());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
              //  Log.d(TAG, "llego al onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
             //   Log.d(TAG, "llego al onDrawerStateChanged");
            }
        });
    }
}
