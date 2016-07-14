package upsa.mimo.es.mountsyourcostume.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;

public class LogginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "LKxwiScIKLCioDgmqogkCRJAu";
    private static final String TWITTER_SECRET = "aOKjp3alg4IhcXtbF4DwILZ8GYQ6fo9gUKMk9Uqvx95TtSE5w6";


    public static final String LOGGIN_NAME = "displayName";
   // public static final String LOGGIN_EMAIL = "displayEmail";
    public static final String LOGGIN_URL_IMAGE = "imageUrl";

    public static final String FLAG_LOGGIN = "flagLoggin";
    public static final int FLAG_TWITTER = 1;
    public static final int FLAG_GOGGLEPLUS = 2;

    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private ProgressDialog mProgressDialog;
    private SignInButton buttonGoogleSignIn;

    //For google plus
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount account;

    //For twitter
    private TwitterLoginButton loginButton;

    @BindView(R.id.container_activity_lggin)
    ViewGroup container;

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

        showProgressDialog();
        checkSignInTwitter();

        checkSignInGoogle();
    }


    private void loadGoogleButton(){
        buttonGoogleSignIn = (SignInButton) findViewById(R.id.button_google_signin);
        buttonGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

    }

    private void signInGoogle(){
      //  Log.d("LOGGIN","logeado con google");
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
    }

    private void initGooglePlus(){
        loadGoogleButton();

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
        Log.d("LOGGIN","Error en connection failed" + connectionResult.getErrorMessage());
        if(connectionResult.getErrorMessage()!=null) {
            MyApplication.showMessageInSnackBar(container, connectionResult.getErrorMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_GOOGLE_SIGN_IN){
            GoogleSignInResult signInResultFromIntent = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            checkSignInResult(signInResultFromIntent);
        }

    }

    private void checkSignInResult(GoogleSignInResult signInResult){

        if(signInResult.isSuccess()){
            account = signInResult.getSignInAccount();
            goToMainActivityWithGooglePlus();
        }
        //hideProgressDialog();
    }

    /**
     * El botón se puede customizar, tanto en tamaños como en colores y estilos.
     */
    private void styleGooglePlusButton(GoogleSignInOptions gso) {
        buttonGoogleSignIn.setSize(SignInButton.SIZE_STANDARD);
        buttonGoogleSignIn.setScopes(gso.getScopeArray());
    }

    private void goToMainActivityWithGooglePlus(){

        Intent profileIntent = new Intent(this, MainActivity.class);
        if(account.getDisplayName()!=null) {
            profileIntent.putExtra(LOGGIN_NAME, account.getDisplayName());
        }
     //   if(account.getEmail()!=null) {
      //      profileIntent.putExtra(LOGGIN_EMAIL, account.getEmail());
     //   }
        if(account.getPhotoUrl()!=null) {
            Log.d("PHOTO", account.getPhotoUrl().toString());
            profileIntent.putExtra(LOGGIN_URL_IMAGE, account.getPhotoUrl().toString());
        }
        profileIntent.putExtra(LogginActivity.FLAG_LOGGIN, LogginActivity.FLAG_GOGGLEPLUS);
        startActivity(profileIntent);
        finish();
    }

    //For progress dialog
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    //For twitter
    private void initTwitter(){
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = result.data;
                goToMainActivityWithTwitter(session);

            }
            @Override
            public void failure(TwitterException exception) {
               // hideProgressDialog();
                if(!exception.getMessage().contains("request was canceled")){
                    Log.d("LOGGIN","Error en Twitter init");
                    MyApplication.showMessageInSnackBar(container,exception.getMessage());
                }
            }
        });
    }

    private void goToMainActivityWithTwitter(TwitterSession session){

       //  showProgressDialog();
        AccountService accountService = Twitter.getApiClient(session).getAccountService();
        accountService.verifyCredentials(true, true, new Callback<User>() {
            @Override
            public void success(Result<User> result) {

                Intent profileIntent = new Intent(LogginActivity.this, MainActivity.class);
                String name = result.data.name;
                String url_image = result.data.profileImageUrl.replace("_normal","_bigger");
              //  String email = result.data.email;
                if (name != null) {
                    profileIntent.putExtra(LogginActivity.LOGGIN_NAME, name);
                }
                if (url_image != null) {
                    profileIntent.putExtra(LogginActivity.LOGGIN_URL_IMAGE, url_image);
                }
             //   if (email != null) {
             //       profileIntent.putExtra(LogginActivity.LOGGIN_EMAIL, email);
             //   }

                profileIntent.putExtra(LogginActivity.FLAG_LOGGIN, LogginActivity.FLAG_TWITTER);
                startActivity(profileIntent);
           //     hideProgressDialog();
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
              //  hideProgressDialog();
                if(!exception.getMessage().contains("request was canceled")){
                    Log.d("LOGGIN","Error en twitter wiht Twitter");
                    MyApplication.showMessageInSnackBar(container,exception.getMessage());
                }

            }
        });
    }

    private void checkSignInTwitter(){
        Twitter.getInstance();
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if(session!=null){
            goToMainActivityWithTwitter(session);
        }
        else{
          //  hideProgressDialog();
        }
    }

    private void checkSignInGoogle(){

        OptionalPendingResult<GoogleSignInResult> optPenRes = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optPenRes.isDone()) {
            GoogleSignInResult result = optPenRes.get();
            checkSignInResult(result);
        } else {
//            showProgressDialog();
            optPenRes.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //         hideProgressDialog();
                    checkSignInResult(googleSignInResult);
                }
            });
        }
    }
}
