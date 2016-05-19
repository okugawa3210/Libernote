package com.okugawa3210.libernote.di;

import com.okugawa3210.libernote.activity.SplashActivity;
import com.okugawa3210.libernote.di.scope.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    FragmentComponent plus(FragmentModule module);

    void inject(SplashActivity activity);
}
