package com.okugawa3210.libernote.form.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseForm {
    private AppCompatActivity activity;
    private View dialogView;

    public BaseForm(AppCompatActivity activity) {
        this.activity = activity;
        this.setup();
    }

    public BaseForm(AppCompatActivity activity, View dialogView) {
        this.dialogView = dialogView;
        this.activity = activity;
        setup();
    }

    public Context getContext() {
        return activity.getApplicationContext();
    }

    public View findViewById(int id) {
        if (dialogView != null) {
            return this.dialogView.findViewById(id);
        } else {
            return this.activity.findViewById(id);
        }
    }

    public abstract void setup();

    public abstract void clearErrors();

    public void reset() {

    }

    public abstract boolean isValid();

    public boolean validateForm(FormItem item) {
        return validateForm(item, true);
    }

    public boolean validateForm(FormItem item, boolean isFocus) {
        if (item.validity()) {
            return true;
        } else {
            if (isFocus) {
                item.view.requestFocus();
            }
            return false;
        }
    }

    public boolean validateForms(FormItem... items) {
        return validateForms(items, true);
    }

    public boolean validateForms(FormItem[] items, boolean isFocus) {
        boolean isValid = true;
        for (FormItem item : items) {
            if (!validateForm(item, isFocus)) {
                isValid = false;
            }
        }
        return isValid;
    }
}
