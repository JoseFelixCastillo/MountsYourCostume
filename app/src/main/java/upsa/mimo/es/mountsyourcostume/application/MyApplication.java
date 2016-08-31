package upsa.mimo.es.mountsyourcostume.application;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.helpers.CloudDBHelper;
import upsa.mimo.es.mountsyourcostume.helpers.CostumeDBHelper;
import upsa.mimo.es.mountsyourcostume.interfaces.CloudPersistance;
import upsa.mimo.es.mountsyourcostume.interfaces.LocalPersistance;
import upsa.mimo.es.mountsyourcostume.model.User;

/**
 * Created by JoseFelix on 10/07/2016.
 */
public class MyApplication extends Application {

    private static Context context;
    private static LocalPersistance localPersistance;
    private static CloudPersistance cloudPersistance;
    private static User user;

    private static ProgressDialog mProgressDialog;


    @Override
    public void onCreate() {
        super.onCreate();
        this.context = MyApplication.this;
        this.user = new User();
    }

    public static void showMessageInSnackBar(View view, String message){
        Snackbar snackbar = Snackbar.make(view,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static LocalPersistance getLocalPersistance(){

        if(localPersistance==null){
            localPersistance = CostumeDBHelper.newInstance(context);
        }
        return localPersistance;
    }

    public static CloudPersistance getCloudPersistance(){

        if(cloudPersistance==null){
           cloudPersistance  = CloudDBHelper.newInstance(context);
        }

        return cloudPersistance;
    }

    public static User getUser(){
        return user;
    }


    //For progress dialog
    public static void showProgressDialog(FragmentActivity activity) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage(context.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        //   Log.d(TAG, "enseño dialog");
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            //     Log.d(TAG, "escondo DIalog");
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
