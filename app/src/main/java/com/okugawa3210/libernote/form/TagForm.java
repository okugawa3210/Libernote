package com.okugawa3210.libernote.form;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.form.base.BaseForm;
import com.okugawa3210.libernote.form.base.FormItem;
import com.okugawa3210.libernote.form.validator.EmptyValidator;
import com.okugawa3210.libernote.form.validator.LengthValidator;

public class TagForm extends BaseForm {

    private FormItem<EditText> name;

    public TagForm(AppCompatActivity activity, View dialogView) {
        super(activity, dialogView);
    }

    public TagForm(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void setup() {
        name = new FormItem<>((EditText) findViewById(R.id.text_name));
        name.addValidator(new EmptyValidator<>());
        name.addValidator(new LengthValidator<>(10));
    }

    @Override
    public void clearErrors() {
        name.getView().setError(null);
    }

    @Override
    public boolean isValid() {
        return validateForm(name);
    }

    @Override
    public void reset() {
        name.getView().setText("");
    }

    public String getName() {
        return name.getView().getText().toString();
    }

    public void setDuplicate() {
        name.getView().setError(getContext().getString(R.string.message_tag_duplicated));
        name.getView().requestFocus();
    }
}
