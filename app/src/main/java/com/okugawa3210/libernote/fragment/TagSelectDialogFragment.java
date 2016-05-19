package com.okugawa3210.libernote.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.adapter.TagSelectDialogAdapter;
import com.okugawa3210.libernote.form.TagForm;
import com.okugawa3210.libernote.fragment.base.DialogCustomLayout;
import com.okugawa3210.libernote.model.OrmaDatabase;
import com.okugawa3210.libernote.model.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class TagSelectDialogFragment extends DialogFragment implements DialogCustomLayout {

    public static final String ARG_KEY_SELECTED_TAG_LIST = "selected_tag_list";

    @Inject
    OrmaDatabase orma;
    @Inject
    TagForm form;

    private View dialogView;
    private ListView listView;
    private TagSelectDialogAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_tag_add, (ViewGroup) getView());

        LibernoteApplication.getComponent(this).inject(this);

        listView = (ListView) dialogView.findViewById(R.id.tag_list);

        View btnAdd = dialogView.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibernoteApplication.hideSoftInputFromWindow();
                form.clearErrors();
                if (form.isValid()) {
                    final Tag[] tag = new Tag[1];
                    tag[0] = Tag.getTagByName(orma, form.getName());
                    if (tag[0] == null) {
                        orma.transactionSync(new Runnable() {
                            @Override
                            public void run() {
                                tag[0] = Tag.create(orma, form.getName());
                            }
                        });
                        renderTagList(getSelectedTagList());
                    }

                    form.reset();
                    checkedTag(tag[0].getId());
                }
            }
        });

        List<Tag> selectedTagList;
        Tag[] selectedTagArray = (Tag[]) getArguments().getSerializable(ARG_KEY_SELECTED_TAG_LIST);
        if (selectedTagArray != null) {
            selectedTagList = Arrays.asList(selectedTagArray);
        } else {
            selectedTagList = new ArrayList<>();
        }

        renderTagList(selectedTagList);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(getDialogView())
                .setPositiveButton("選択", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity activity = getActivity();
                        if (activity instanceof OnSelectTagsListener) {
                            ((OnSelectTagsListener) activity).onSelectTagsListener(getSelectedTagList());
                        }
                    }
                })
                .setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    @Override
    public View getDialogView() {
        return dialogView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        LibernoteApplication.hideSoftInputFromWindow();
    }

    private void renderTagList(List<Tag> selectedTagList) {
        listView.setAdapter(null);
        adapter = new TagSelectDialogAdapter(getActivity());
        adapter.setList(Tag.getTags(orma));
        listView.setAdapter(adapter);

        for (Tag selectedTag : selectedTagList) {
            checkedTag(selectedTag.getId());
        }
    }

    private void checkedTag(long id) {
        int position = -1;
        Tag tag = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            tag = adapter.getItem(i);
            if (tag.getId() == id) {
                position = i;
                break;
            }
        }
        if (position < 0) {
            return;
        }

        final int targetPosition = position;
        final Tag targetTag = tag;

        listView.post(new Runnable() {
            @Override
            public void run() {
                TagSelectDialogAdapter.TagViewHolder holder = (TagSelectDialogAdapter.TagViewHolder) listView.getChildAt(targetPosition).getTag();
                holder.getName().setChecked(true);
                targetTag.setSelected(true);
            }
        });
    }

    private List<Tag> getSelectedTagList() {
        List<Tag> selectedTagList = new ArrayList<>();

        for (int position = 0; position < adapter.getCount(); position++) {
            Tag tag = adapter.getItem(position);
            if (tag.isSelected()) {
                selectedTagList.add(tag);
            }
        }

        return selectedTagList;
    }

    public interface OnSelectTagsListener {
        void onSelectTagsListener(List<Tag> selectedTagList);
    }
}
