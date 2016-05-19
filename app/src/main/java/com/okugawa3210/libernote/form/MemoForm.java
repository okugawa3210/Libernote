package com.okugawa3210.libernote.form;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.common.Constants;
import com.okugawa3210.libernote.di.scope.FragmentScope;
import com.okugawa3210.libernote.form.base.BaseForm;
import com.okugawa3210.libernote.form.base.FormItem;
import com.okugawa3210.libernote.form.validator.EmptyValidator;
import com.okugawa3210.libernote.form.validator.LengthValidator;
import com.okugawa3210.libernote.model.Tag;
import com.okugawa3210.libernote.view.FlowLayout;
import com.okugawa3210.libernote.view.TagItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

@FragmentScope
public class MemoForm extends BaseForm {

    private FormItem<EditText> note;
    private FormItem<EditText> title;
    private FormItem<EditText> expiredOn;
    private FormItem<RadioGroup> priority;
    private FormItem<FlowLayout> tagArea;

    private boolean isInvalidAdvance = false;

    @Inject
    public MemoForm(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void setup() {
        note = new FormItem<>((EditText) findViewById(R.id.text_note));
        note.addValidator(new EmptyValidator<>());

        title = new FormItem<>((EditText) findViewById(R.id.text_title));
        title.addValidator(new LengthValidator<>(20));

        expiredOn = new FormItem<>((EditText) findViewById(R.id.text_expired_on));

        priority = new FormItem<>((RadioGroup) findViewById(R.id.radio_priority));

        tagArea = new FormItem<>((FlowLayout) findViewById(R.id.tag_area));
    }

    @Override
    public void clearErrors() {
        note.getView().setError(null);
        title.getView().setError(null);
        expiredOn.getView().setError(null);
    }

    @Override
    public boolean isValid() {
        boolean isValid = validateForm(note);
        isInvalidAdvance = !validateForms(title, expiredOn, priority);

        return isValid && !isInvalidAdvance();
    }

    public boolean isInvalidAdvance() {
        return isInvalidAdvance;
    }

    public void setNote(String note) {
        this.note.getView().setText(note);
    }

    public String getNote() {
        return note.getView().getText().toString();
    }

    public void setTitle(String title) {
        this.title.getView().setText(title);
    }

    public String getTitle() {
        return title.getView().getText().toString();
    }

    public void setExpiredOn(Date expiredOn) {
        if (expiredOn != null) {
            this.expiredOn.getView().setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(expiredOn));
        }
    }

    public Date getExpiredOn() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(expiredOn.getView().getText().toString());
        } catch (ParseException e) {
            return null;
        }
    }

    public void setPriority(int priority) {
        RadioButton selected;
        switch (priority) {
            case Constants.MEMO_PRIORITY_HIGH:
                selected = (RadioButton) this.priority.getView().findViewById(R.id.memo_priority_high);
                break;
            case Constants.MEMO_PRIORITY_LOWER:
                selected = (RadioButton) this.priority.getView().findViewById(R.id.memo_priority_lower);
                break;
            default:
                selected = (RadioButton) this.priority.getView().findViewById(R.id.memo_priority_none);
                break;
        }

        selected.setChecked(true);
    }

    public int getPriority() {
        switch (priority.getView().getCheckedRadioButtonId()) {
            case R.id.memo_priority_high:
                return Constants.MEMO_PRIORITY_HIGH;
            case R.id.memo_priority_lower:
                return Constants.MEMO_PRIORITY_LOWER;
            default:
                return Constants.MEMO_PRIORITY_NONE;
        }
    }

    public void setTag(Tag tag) {
        TagItem item = new TagItem(getContext(), tagArea.getView(), true);
        item.setText(tag.getName());
        item.setTag(tag);

        tagArea.getView().addView(item);
    }

    public void setTag(List<Tag> tagList) {
        tagArea.getView().removeAllViews();
        for (Tag tag : tagList) {
            setTag(tag);
        }
    }

    public List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < tagArea.getView().getChildCount(); i++) {
            View child = tagArea.getView().getChildAt(i);
            if (child instanceof TagItem) {
                TagItem tagItem = (TagItem) child;
                tags.add((Tag) tagItem.getTag());
            }
        }
        return tags;
    }
}
