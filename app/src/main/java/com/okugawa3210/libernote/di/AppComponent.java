package com.okugawa3210.libernote.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    ActivityComponent plus(ActivityModule module);
}
