package com.okugawa3210.libernote.form.base;

import android.view.View;

import com.okugawa3210.libernote.form.validator.Validator;

import java.util.ArrayList;
import java.util.List;

public class FormItem<T extends View> {

    public T view;
    private List<Validator<T>> validatorList;

    public FormItem(T view) {
        this.view = view;
        this.validatorList = new ArrayList<>();
    }

    public T getView() {
        return view;
    }

    public void addValidator(Validator<T> validator) {
        this.validatorList.add(validator);
    }

    public boolean validity() {
        if (validatorList.size() == 0) {
            return true;
        }
        boolean isValid = false;
        for (Validator<T> validator : validatorList) {
            isValid = validator.isValid(view);
            if (!isValid) {
                break;
            }
        }
        return isValid;
    }
}
