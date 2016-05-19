package com.okugawa3210.libernote.di;

import android.app.Application;
import android.content.Context;

import com.okugawa3210.libernote.common.Constants;
import com.okugawa3210.libernote.model.OrmaDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context context;

    public AppModule(Application app) {
        this.context = app;
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public OrmaDatabase provideOrmaDatabase(Context context) {
        return OrmaDatabase.builder(context).name(Constants.DB_NAME).build();
    }
}
