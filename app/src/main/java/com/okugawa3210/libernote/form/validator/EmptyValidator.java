package com.okugawa3210.libernote.form.validator;

import android.text.TextUtils;
import android.widget.EditText;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;

public class EmptyValidator<T extends EditText> implements Validator<EditText> {
    @Override
    public boolean isValid(EditText item) {
        boolean isValid = !TextUtils.isEmpty(item.getText());
        if (!isValid) {
            item.setError(LibernoteApplication.getContext().getString(R.string.message_required));
        }
        return isValid;
    }
}
