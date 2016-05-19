package com.okugawa3210.libernote.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.okugawa3210.libernote.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "DatePickerFragment";
    public static final String BUNDLE_KEY_EDIT_TEXT = "edit_text";

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private EditText editor;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        editor = (EditText) getActivity().findViewById(getArguments().getInt(BUNDLE_KEY_EDIT_TEXT));

        Calendar calendar = Calendar.getInstance();
        String selectDateStr = editor.getText().toString();

        if (!TextUtils.isEmpty(selectDateStr)) {
            try {
                calendar.setTime(formatter.parse(selectDateStr));
            } catch (ParseException e) {
                Log.e(MemoCreateFragment.class.getSimpleName(), e.toString());
            }
        }

        return new CustomDatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (year == 0 && monthOfYear == 0 && dayOfMonth == 0) {
            editor.setText("");
        } else {
            editor.setText(formatter.format(calendar.getTime()));
        }
    }

    private class CustomDatePickerDialog extends DatePickerDialog {

        private OnDateSetListener callBack;

        public CustomDatePickerDialog(Context context, final OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            this.callBack = callBack;
            setButton(DatePickerDialog.BUTTON_NEUTRAL, context.getString(R.string.date_picker_dialog_today), this);
            setButton(DatePickerDialog.BUTTON_NEGATIVE, context.getString(R.string.date_picker_dialog_clear), this);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_NEUTRAL:
                    Calendar calendar = Calendar.getInstance();
                    getDatePicker().clearFocus();
                    callBack.onDateSet(getDatePicker(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case BUTTON_NEGATIVE:
                    getDatePicker().clearFocus();
                    callBack.onDateSet(getDatePicker(), 0, 0, 0);
                    break;
                default:
                    super.onClick(dialog, which);
                    break;
            }
        }
    }
}
