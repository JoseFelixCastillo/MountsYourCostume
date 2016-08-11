package upsa.mimo.es.mountsyourcostume.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by User on 28/07/2016.
 */
public class MyTextInputEditText extends TextInputEditText {

    private static final String TAG = MyTextInputEditText.class.getSimpleName();
    private static final String INDEX_FORMAT = "%d. ";
    private static final String KEY_STATE_PARCELABLE = "KEY_STATE_PARCELABLE";
    private static final String KEY_STATE_INDEX = "KEY_STATE_INDEX";

    private boolean append= false;
    private int index=1;
    private char myChar;
    private int beforeLength=0;

    public MyTextInputEditText(Context context) {
        super(context);

    }

    public MyTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        initTextWatcher();

    }

    public MyTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initTextWatcher(){

        this.setText(String.format(INDEX_FORMAT,index));

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(myChar=='\n'){
                    if(index>1){
                        index--;
                    }
                }

                if(charSequence.length()>0) {
                    myChar = charSequence.charAt(charSequence.length() - 1);
                }
                beforeLength = charSequence.length();

            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                String string = s.toString();

                if(s.length()>beforeLength) {
                    if (string.length() > 0 && string.charAt(string.length() - 1) == '\n') {
                        append = true;
                        index++;
                    } else {
                        append = false;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()==0){
                    index=1;
                    editable.append(String.format(INDEX_FORMAT,index));
                }

                else if(append) {
                    editable.append(String.format(INDEX_FORMAT,index));
                }

                if(editable.length()>0) {
                    myChar = editable.charAt(editable.length() - 1);
                }
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_STATE_PARCELABLE, super.onSaveInstanceState());

        bundle.putInt(KEY_STATE_INDEX,index);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(state!=null && state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            index = bundle.getInt(KEY_STATE_INDEX);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_STATE_PARCELABLE));
            return;
        }

        super.onRestoreInstanceState(state);
    }
}
