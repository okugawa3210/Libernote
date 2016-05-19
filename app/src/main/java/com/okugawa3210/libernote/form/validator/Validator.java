package com.okugawa3210.libernote.form.validator;

import android.view.View;

public interface Validator<T extends View> {
    boolean isValid(T item);
}
