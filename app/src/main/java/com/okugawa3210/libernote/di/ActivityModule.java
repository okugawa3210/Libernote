package com.okugawa3210.libernote.di;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    final AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    public Activity activity() {
        return activity;
    }

    @Provides
    public Context context() {
        return activity;
    }

    @Provides
    public LayoutInflater layoutInflater() {
        return activity.getLayoutInflater();
    }
}
