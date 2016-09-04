package upsa.mimo.es.mountsyourcostume.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import upsa.mimo.es.mountsyourcostume.R;

/**
 * Created by User on 18/07/2016.
 */
public class DialogConfirmDeleteCostume extends DialogFragment implements DialogInterface.OnClickListener {

    public static interface DialogConfirmDeleteCostumeInterface{

        public void confirmDelete();
    }
    private DialogConfirmDeleteCostumeInterface listener;

    public static final String TAG = DialogConfirmDeleteCostume.class.getSimpleName();
    public static DialogConfirmDeleteCostume newInstance(){
        DialogConfirmDeleteCostume dialogConfirmDeleteCostume = new DialogConfirmDeleteCostume();

        return dialogConfirmDeleteCostume;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_delete_costume_message)
                .setTitle(R.string.attention)
                .setPositiveButton(android.R.string.ok,this)
        .setNegativeButton(android.R.string.cancel,this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                this.listener.confirmDelete();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.listener = (DialogConfirmDeleteCostumeInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + activity.getString(R.string.should_implement) + DialogConfirmDeleteCostumeInterface.class.getSimpleName());
        }

    }
}
