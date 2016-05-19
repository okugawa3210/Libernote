package com.okugawa3210.libernote.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;

public class ConfirmDialog {
    private AlertDialog.Builder builder;
    private String title;
    private String message;

    public ConfirmDialog(Activity activity) {
        builder = new AlertDialog.Builder(activity);
    }

    public static void show(Activity activity, String message, DialogInterface.OnClickListener positiveListener) {
        Context context = LibernoteApplication.getContext();
        show(activity, context.getString(R.string.default_confirm_title), message, context.getString(R.string.default_confirm_positive_text), positiveListener, context.getString(R.string.default_confirm_negative_text));
    }

    public static void show(Activity activity, String title, String message, DialogInterface.OnClickListener positiveListener) {
        Context context = LibernoteApplication.getContext();
        show(activity, title, message, context.getString(R.string.default_confirm_positive_text), positiveListener, context.getString(R.string.default_confirm_negative_text));

    }

    public static void show(Activity activity, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener, String negativeText) {
        ConfirmDialog confirm = new ConfirmDialog(activity);
        confirm.setTitle(title);
        confirm.setMessage(message);
        confirm.setPositiveAction(positiveText, positiveListener);
        confirm.setNegativeAction(negativeText);
        confirm.show();
    }

    public void setTitle(String title) {
        builder.setTitle(title);
    }

    public void setMessage(String message) {
        builder.setMessage(message);
    }

    public void setPositiveAction(String text) {
        setPositiveAction(text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public void setPositiveAction(String text, DialogInterface.OnClickListener listener) {
        builder.setPositiveButton(text, listener);
    }

    public void setNegativeAction(String text) {
        setNegativeAction(text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public void setNegativeAction(String text, DialogInterface.OnClickListener listener) {
        builder.setNegativeButton(text, listener);
    }

    public void show() {
        builder.create().show();
    }
}
