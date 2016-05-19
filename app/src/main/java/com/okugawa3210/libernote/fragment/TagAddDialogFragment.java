package com.okugawa3210.libernote.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.form.TagForm;
import com.okugawa3210.libernote.fragment.base.DialogCustomLayout;
import com.okugawa3210.libernote.model.OrmaDatabase;
import com.okugawa3210.libernote.model.Tag;

import javax.inject.Inject;

public class TagAddDialogFragment extends DialogFragment implements DialogCustomLayout {

    @Inject
    OrmaDatabase orma;
    @Inject
    TagForm form;

    private View dialogView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_tag_add, (ViewGroup) getView());

        LibernoteApplication.getComponent(this).inject(this);

        dialogView.findViewById(R.id.text_name).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (KeyEvent.KEYCODE_ENTER == keyCode) {
                        createTag();
                    }
                }
                return false;
            }
        });

        View btnAdd = dialogView.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTag();
            }
        });

        dialogView.findViewById(R.id.tag_list).setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(getDialogView());

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        LibernoteApplication.hideSoftInputFromWindow();
    }

    @Override
    public View getDialogView() {
        return dialogView;
    }

    private void createTag() {
        LibernoteApplication.hideSoftInputFromWindow();
        form.clearErrors();
        if (form.isValid()) {
            Tag tag = Tag.getTagByName(orma, form.getName());
            if (tag == null) {
                orma.transactionSync(new Runnable() {
                    @Override
                    public void run() {
                        Tag.create(orma, form.getName());
                    }
                });

                if (getActivity() instanceof OnAddTagListener) {
                    ((OnAddTagListener) getActivity()).onAddTagListener();
                }
                dismiss();
            } else {
                form.setDuplicate();
            }
        }
    }

    public interface OnAddTagListener {
        void onAddTagListener();
    }
}
