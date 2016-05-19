package com.okugawa3210.libernote.fragment.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.form.MemoForm;
import com.okugawa3210.libernote.fragment.DatePickerDialogFragment;
import com.okugawa3210.libernote.fragment.TagSelectDialogFragment;
import com.okugawa3210.libernote.model.OrmaDatabase;
import com.okugawa3210.libernote.model.Tag;
import com.okugawa3210.libernote.view.ConfirmDialog;
import com.okugawa3210.libernote.view.FlowLayout;
import com.okugawa3210.libernote.view.TagItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public abstract class MemoFormFragment extends Fragment {

    @Inject
    protected OrmaDatabase orma;
    @Inject
    protected MemoForm form;

    private View formMemoAdvance;
    private View openAdvancedSettings;
    private View closeAdvancedSettings;
    private FlowLayout tagArea;

    protected abstract DialogInterface.OnClickListener getSubmitCore();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_memo_form, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        LibernoteApplication.getComponent(this).inject(this);

        final EditText expiredOn = (EditText) view.findViewById(R.id.text_expired_on);
        expiredOn.setInputType(InputType.TYPE_NULL);
        expiredOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        expiredOn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LibernoteApplication.hideSoftInputFromWindow();
                    showDatePickerDialog();
                }
            }
        });

        formMemoAdvance = view.findViewById(R.id.form_memo_advance);
        openAdvancedSettings = view.findViewById(R.id.open_advanced_settings);
        closeAdvancedSettings = view.findViewById(R.id.close_advanced_settings);
        openAdvancedSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAdvancedSettingsArea(true);
            }
        });
        closeAdvancedSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAdvancedSettingsArea(false);
            }
        });

        View btnAddTag = view.findViewById(R.id.btn_add_tag);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagSelectDialog();
            }
        });

        tagArea = (FlowLayout) view.findViewById(R.id.tag_area);

        View btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCore();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_memo_form, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save:
                saveCore();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void renderTags(final List<Tag> selectedTagList) {
        form.setTag(selectedTagList);
    }

    protected void saveCore() {
        form.clearErrors();
        if (form.isValid()) {
            LibernoteApplication.hideSoftInputFromWindow();
            ConfirmDialog.show(getActivity(), getString(R.string.message_confirm_register), getSubmitCore());
        } else {
            if (form.isInvalidAdvance() && formMemoAdvance.getVisibility() != View.VISIBLE) {
                toggleAdvancedSettingsArea(true);
            }
        }
    }

    private void toggleAdvancedSettingsArea(boolean isOpen) {
        if (isOpen && formMemoAdvance.getVisibility() != View.VISIBLE) {
            formMemoAdvance.setVisibility(View.VISIBLE);
            openAdvancedSettings.setVisibility(View.GONE);
            closeAdvancedSettings.setVisibility(View.VISIBLE);
        } else if (!isOpen && formMemoAdvance.getVisibility() == View.VISIBLE) {
            formMemoAdvance.setVisibility(View.GONE);
            openAdvancedSettings.setVisibility(View.VISIBLE);
            closeAdvancedSettings.setVisibility(View.GONE);
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerDialogFragment.BUNDLE_KEY_EDIT_TEXT, R.id.text_expired_on);
        datePicker.setArguments(bundle);
        datePicker.show(getActivity().getSupportFragmentManager(), DatePickerDialogFragment.TAG);
    }

    private void showTagSelectDialog() {
        List<Tag> selectedTagList = new ArrayList<>();
        for (int i = 0; i < tagArea.getChildCount(); i++) {
            View child = tagArea.getChildAt(i);
            if (child instanceof TagItem) {
                selectedTagList.add(((Tag) child.getTag()));
            }
        }
        Bundle args = new Bundle();
        args.putSerializable(TagSelectDialogFragment.ARG_KEY_SELECTED_TAG_LIST, selectedTagList.toArray(new Tag[selectedTagList.size()]));
        TagSelectDialogFragment tagSelect = new TagSelectDialogFragment();
        tagSelect.setArguments(args);
        tagSelect.show(getActivity().getSupportFragmentManager(), TagSelectDialogFragment.class.getSimpleName());
    }
}
