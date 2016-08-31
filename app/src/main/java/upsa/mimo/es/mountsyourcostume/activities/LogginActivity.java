package upsa.mimo.es.mountsyourcostume.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;

public class LogginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LogginActivity.class.getSimpleName();

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "LKxwiScIKLCioDgmqogkCRJAu";
    private static final String TWITTER_SECRET = "aOKjp3alg4IhcXtbF4DwILZ8GYQ6fo9gUKMk9Uqvx95TtSE5w6";


 //   public static final String LOGGIN_NAME = "displayName";
   // public static final String LOGGIN_EMAIL = "displayEmail";
 //   public static final String LOGGIN_URL_IMAGE = "imageUrl";

    public static final String FLAG_LOGGIN = "flagLoggin";
    public static final int FLAG_TWITTER = 1;
    public static final int FLAG_GOGGLEPLUS = 2;

    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private ProgressDialog mProgressDialog;
   // private SignInButton buttonGoogleSignIn;

    //For google plus
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount account;

    //For twitter
  //  private TwitterLoginButton loginButton;

    @BindView(R.id.container_activity_lggin)
    ViewGroup container;

    @BindView(R.id.button_google_signin)
    SignInButton buttonGoogleSignIn;

    @OnClick(R.id.button_google_signin)
    void signInGoogle2(){
       // Log.d(TAG,"pulsado boton google");
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
    }

    @BindView(R.id.twitter_login_button)
    TwitterLoginButton buttonTwitterLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_loggin);
        ButterKnife.bind(this);

        initGooglePlus();
        initTwitter();
    }

    @Override
    public void onStart() {
        super.onStart();

      //  Log.d(TAG,"LLEGAMOS AL ONSTART");
        showProgressDialog();
        if(!isSignInTwitter()) {
            checkSignInGoogle();
        }

    }

    private void initGooglePlus(){
     //   loadGoogleButton();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 0);
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        styleGooglePlusButton(gso);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        //Mostrar dialog con que ha fallado?¿?
        hideProgressDialog();
      //  Log.d(TAG,"Error en connection failed" + connectionResult.getErrorMessage());
        if(connectionResult.getErrorMessage()!=null) {
            MyApplication.showMessageInSnackBar(container, connectionResult.getErrorMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        buttonTwitterLogin.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_GOOGLE_SIGN_IN){
            GoogleSignInResult signInResultFromIntent = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            checkGoogleSignInResult(signInResultFromIntent);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }

    private void checkGoogleSignInResult(GoogleSignInResult signInResult){

     //   Log.d(TAG, "entramos a checkin google");
        if(signInResult.isSuccess()){
      //      Log.d(TAG, "entramos a checkin google2");
            account = signInResult.getSignInAccount();
            goToMainActivity(FLAG_GOGGLEPLUS);
         //   goToMainActivityWithGooglePlus();
        }
        else{
            hideProgressDialog();
        }
    }

    private void styleGooglePlusButton(GoogleSignInOptions gso) {
        buttonGoogleSignIn.setSize(SignInButton.SIZE_STANDARD);
        buttonGoogleSignIn.setScopes(gso.getScopeArray());
    }

  /*  private void goToMainActivityWithGooglePlus(){

     //   Intent profileIntent = new Intent(this, MainActivity.class);
        if(account.getDisplayName()!=null) {
            MyApplication.user.setName(account.getDisplayName());
           // profileIntent.putExtra(LOGGIN_NAME, account.getDisplayName());
        }
        if(account.getEmail()!=null) {
            MyApplication.user.setEmail(account.getEmail());
      //      profileIntent.putExtra(LOGGIN_EMAIL, account.getEmail());
            Log.d(TAG, "Google email: " + account.getEmail());
        }
        if(account.getPhotoUrl()!=null) {
            Log.d("PHOTO", account.getPhotoUrl().toString());
            MyApplication.user.setPhotoURL(account.getPhotoUrl().toString());
          //  profileIntent.putExtra(LOGGIN_URL_IMAGE, account.getPhotoUrl().toString());
        }
        Log.d(TAG, "entramos a vamos al activity main");
        MyApplication.user.setSocialNetwork(LogginActivity.FLAG_GOGGLEPLUS);
       // profileIntent.putExtra(LogginActivity.FLAG_LOGGIN, LogginActivity.FLAG_GOGGLEPLUS);
     //   startActivity(profileIntent);
       // hideProgressDialog();
      //  finish();*/

 //   }
    //For twitter
    private void initTwitter(){
     //   loginTwitterButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        buttonTwitterLogin.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {

           //     Log.d(TAG, "initTwitterboton quizas");
                TwitterSession session = result.data;
                getInfoUserWithTwitter(session);

            }
            @Override
            public void failure(TwitterException exception) {
               // hideProgressDialog();
                if(!exception.getMessage().contains("request was canceled")){
              //      Log.d("LOGGIN","Error en Twitter init");
                    MyApplication.showMessageInSnackBar(container,exception.getMessage());
                }
            }
        });
    }

    private void getInfoUserWithTwitter(final TwitterSession session){
       //  showProgressDialog();
        AccountService accountService = Twitter.getApiClient(session).getAccountService();
        accountService.verifyCredentials(true, true, new Callback<User>() {
            @Override
            public void success(final Result<User> result) {

                //Crearlo bien
                String name = result.data.name;
                String url_image = result.data.profileImageUrl.replace("_normal","_bigger");
                String nameId = result.data.screenName;
                //   String email = result.data.email;
                if (name != null) {
                    MyApplication.getUser().setName(name);
                  //  profileIntent.putExtra(LogginActivity.LOGGIN_NAME, name);
                }
                if (url_image != null) {
                    MyApplication.getUser().setPhotoURL(url_image);
                 //   profileIntent.putExtra(LogginActivity.LOGGIN_URL_IMAGE, url_image);
                }
                if(nameId !=null){
                    MyApplication.getUser().setTokenForBD(nameId);
                }
                MyApplication.getUser().setSocialNetwork(LogginActivity.FLAG_TWITTER);
                goToMainActivity(FLAG_TWITTER);
              /*  if (email != null) {
                    //  profileIntent.putExtra(LogginActivity.LOGGIN_EMAIL, email);
                    Log.d(TAG, "Twitter email: " + email);
                }*/
                /*TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {

                        String email = result.data.toString();
                        Log.d(TAG, "twitter email: " + email);
                        MyApplication.user.setEmail(email);
                        goToMainActivityWithTwitter();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d(TAG, "error con email");
                        goToMainActivityWithTwitter();
                    }
                });*/
            }

            @Override
            public void failure(TwitterException exception) {
                hideProgressDialog();
                if(!exception.getMessage().contains("request was canceled")){
                //    Log.d("LOGGIN","Error en twitter wiht Twitter");
                    MyApplication.showMessageInSnackBar(container,exception.getMessage());
                }

            }
        });
    }

    private boolean isSignInTwitter(){
        Twitter.getInstance();
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if(session!=null){
            getInfoUserWithTwitter(session);
            return true;
        }
        else{
            hideProgressDialog();
            return false;
        }
    }

    private void checkSignInGoogle(){

        OptionalPendingResult<GoogleSignInResult> optPenRes = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optPenRes.isDone()) {
            GoogleSignInResult result = optPenRes.get();
            checkGoogleSignInResult(result);
        } else {
           // showProgressDialog();
            optPenRes.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                 //   hideProgressDialog();
                    checkGoogleSignInResult(googleSignInResult);
                }
            });
        }
    }

    //For progress dialog
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

     //   Log.d(TAG, "enseño dialog");
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
       //     Log.d(TAG, "escondo DIalog");
            mProgressDialog.dismiss();
        }
    }

    private void goToMainActivity(int flag){

        Intent profileIntent = new Intent(LogginActivity.this, MainActivity.class);

      //  Log.d(TAG, "iniciando con twitter");
        profileIntent.putExtra(LogginActivity.FLAG_LOGGIN, flag);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(profileIntent);
        hideProgressDialog();
        finish();
    }
}
