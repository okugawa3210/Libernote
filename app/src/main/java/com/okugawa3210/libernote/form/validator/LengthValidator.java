package com.okugawa3210.libernote.form.validator;

import android.widget.EditText;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;

public class LengthValidator<T extends EditText> implements Validator<EditText> {

    private final int INVALID_NUMBER = 0;
    private int min = INVALID_NUMBER;
    private int max = INVALID_NUMBER;

    public LengthValidator() {
    }

    public LengthValidator(int max) {
        this.max = max;
    }

    public LengthValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isValid(EditText item) {
        String text = item.getText().toString();
        int invalidCount = 0;
        if (min != INVALID_NUMBER && min > text.length()) {
            invalidCount++;
            item.setError(LibernoteApplication.getContext().getString(R.string.message_too_short, min));
        }
        if (max != INVALID_NUMBER && max < text.length()) {
            invalidCount++;
            item.setError(LibernoteApplication.getContext().getString(R.string.message_too_long, max + 1));
        }

        if (min != INVALID_NUMBER && max != INVALID_NUMBER && invalidCount > 0) {
            item.setError(LibernoteApplication.getContext().getString(R.string.message_invalid_length, min, max + 1));
        }

        return invalidCount == 0;
    }
}
