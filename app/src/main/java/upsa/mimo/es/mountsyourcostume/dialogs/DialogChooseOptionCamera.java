package upsa.mimo.es.mountsyourcostume.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.greenrobot.eventbus.EventBus;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.events.MessageOptionCameraEvent;

/**
 * Created by User on 15/07/2016.
 */
public class DialogChooseOptionCamera extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String TAG = DialogChooseOptionCamera.class.getSimpleName();

    public static DialogChooseOptionCamera newInstance(){
        DialogChooseOptionCamera dialogChooseOptionCamera = new DialogChooseOptionCamera();
        Bundle args = new Bundle();

        return dialogChooseOptionCamera;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.photo_options));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.choose_one_option)
                .setSingleChoiceItems(adapter,0,this);

        return dialog.create();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EventBus.getDefault().post(new MessageOptionCameraEvent(getResources().getStringArray(R.array.photo_options)[which]));
        dialog.dismiss();

    }
}
