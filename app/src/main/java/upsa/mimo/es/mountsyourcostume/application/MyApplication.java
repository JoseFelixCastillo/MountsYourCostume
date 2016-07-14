package upsa.mimo.es.mountsyourcostume.application;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by JoseFelix on 10/07/2016.
 */
public class MyApplication extends Application {

    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
    }

    public static void showMessageInSnackBar(View view, String message){
        Snackbar snackbar = Snackbar.make(view,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
