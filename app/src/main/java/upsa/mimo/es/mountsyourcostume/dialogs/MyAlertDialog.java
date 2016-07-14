package upsa.mimo.es.mountsyourcostume.dialogs;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by JoseFelix on 27/06/2016.
 */
public class MyAlertDialog extends DialogFragment {

    public static final String TAG = MyAlertDialog.class.getSimpleName();

    public static interface OnClickButtons{
        public void onPossitiveButton();
        public void onNegativeButton();
    }

    private String title;
    private String positiveButton;
    private String negativeButton;
    private String message;
    private OnClickButtons onClickButtons;

   // private DialogInterface.OnClickListener onClickListener;

    public static MyAlertDialog newInstance(String title,String positiveButton, String negativeButton, OnClickButtons onClickButtons ){
        MyAlertDialog myAlertDialog = new MyAlertDialog();
        Bundle args =  new Bundle();
        //  args.putParcelable("hola", onClickButtons);
        return myAlertDialog;
    }


}
