package upsa.mimo.es.mountsyourcostume.application;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import upsa.mimo.es.mountsyourcostume.helpers.CostumeDBHelper;
import upsa.mimo.es.mountsyourcostume.interfaces.LocalPersistance;

/**
 * Created by JoseFelix on 10/07/2016.
 */
public class MyApplication extends Application {

    private static Context context;
    private static LocalPersistance localPersistance;


    @Override
    public void onCreate() {
        super.onCreate();
        this.context = MyApplication.this;
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
}
