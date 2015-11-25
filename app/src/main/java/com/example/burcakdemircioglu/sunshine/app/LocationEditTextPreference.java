package com.example.burcakdemircioglu.sunshine.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by burcakdemircioglu on 25/11/15.
 */
public class LocationEditTextPreference extends EditTextPreference{
    private int minLength;
    static final private int DEFAULT_MINIMUM_LOCATION_LENGTH=2;

    public LocationEditTextPreference(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a=context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.LocationEditTextPreference, 0, 0);
        try{
            minLength=a.getInteger(R.styleable.LocationEditTextPreference_minLength,
                    DEFAULT_MINIMUM_LOCATION_LENGTH);
            Log.v("minLength for Location",Integer.toString(minLength));
        }finally {
            a.recycle();
        }
    }

    @Override
    protected void showDialog(Bundle state){
        super.showDialog(state);

        EditText et=getEditText();
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Dialog d = getDialog();
                if (d instanceof AlertDialog) {
                    AlertDialog dialog = (AlertDialog) d;
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if (s.length() < minLength) {
                        //Disable ok button
                        positiveButton.setEnabled(false);
                    } else {
                        //Enable ok button
                        positiveButton.setEnabled(true);
                    }
                }
            }
        });
    }
}
