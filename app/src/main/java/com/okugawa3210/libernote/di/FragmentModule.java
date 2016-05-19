package com.okugawa3210.libernote.di;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.okugawa3210.libernote.form.MemoForm;
import com.okugawa3210.libernote.form.TagForm;
import com.okugawa3210.libernote.fragment.base.DialogCustomLayout;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    final Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    public Context context() {
        return fragment.getContext();
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return fragment.getFragmentManager();
    }

    @Provides
    public MemoForm provideMemoCreateForm() {
        return new MemoForm((AppCompatActivity) fragment.getActivity());
    }

    @Provides
    public TagForm provideTagCreateForm() {
        if (fragment instanceof DialogCustomLayout) {
            return new TagForm((AppCompatActivity) fragment.getActivity(), ((DialogCustomLayout) fragment).getDialogView());
        } else {
            return new TagForm((AppCompatActivity) fragment.getActivity());
        }
    }
}
