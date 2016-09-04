package upsa.mimo.es.mountsyourcostume.activities;

import android.Manifest;
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


    private static final String TWITTER_KEY = "LKxwiScIKLCioDgmqogkCRJAu";
    private static final String TWITTER_SECRET = "aOKjp3alg4IhcXtbF4DwILZ8GYQ6fo9gUKMk9Uqvx95TtSE5w6";


    public static final String FLAG_LOGGIN = "flagLoggin";
    public static final int FLAG_TWITTER = 1;
    public static final int FLAG_GOGGLEPLUS = 2;

    private static final int RC_GOOGLE_SIGN_IN = 9001;

    //For google plus
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount account;


    @BindView(R.id.container_activity_lggin)
    ViewGroup container;

    @BindView(R.id.button_google_signin)
    SignInButton buttonGoogleSignIn;

    @OnClick(R.id.button_google_signin)
    void signInGoogle2(){
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
        MyApplication.showProgressDialog(this);
        if(!isSignInTwitter()) {
            checkSignInGoogle();
        }
    }
    private void initGooglePlus(){

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
        MyApplication.hideProgressDialog();

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

        if(signInResult.isSuccess()){
            account = signInResult.getSignInAccount();
            goToMainActivity(FLAG_GOGGLEPLUS);

        }
        else{
            MyApplication.hideProgressDialog();
        }
    }

    private void styleGooglePlusButton(GoogleSignInOptions gso) {
        buttonGoogleSignIn.setSize(SignInButton.SIZE_STANDARD);
        buttonGoogleSignIn.setScopes(gso.getScopeArray());
    }

    //For twitter
    private void initTwitter(){
        buttonTwitterLogin.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                getInfoUserWithTwitter(session);

            }
            @Override
            public void failure(TwitterException exception) {
                if(!exception.getMessage().contains("request was canceled")){
                    MyApplication.showMessageInSnackBar(container,exception.getMessage());
                }
            }
        });
    }

    private void getInfoUserWithTwitter(final TwitterSession session){

        AccountService accountService = Twitter.getApiClient(session).getAccountService();
        accountService.verifyCredentials(true, true, new Callback<User>() {
            @Override
            public void success(final Result<User> result) {

                String name = result.data.name;
                String url_image = result.data.profileImageUrl.replace("_normal","_bigger");
                String nameId = result.data.screenName;

                if (name != null) {
                    MyApplication.getUser().setName(name);

                }
                if (url_image != null) {
                    MyApplication.getUser().setPhotoURL(url_image);

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
                MyApplication.hideProgressDialog();
                if(!exception.getMessage().contains("request was canceled")){
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
            MyApplication.hideProgressDialog();
            return false;
        }
    }

    private void checkSignInGoogle(){

        OptionalPendingResult<GoogleSignInResult> optPenRes = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optPenRes.isDone()) {
            GoogleSignInResult result = optPenRes.get();
            checkGoogleSignInResult(result);
        } else {
            optPenRes.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    checkGoogleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void goToMainActivity(int flag){

        Intent profileIntent = new Intent(LogginActivity.this, MainActivity.class);
        profileIntent.putExtra(LogginActivity.FLAG_LOGGIN, flag);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(profileIntent);
        MyApplication.hideProgressDialog();
        finish();
    }
}
