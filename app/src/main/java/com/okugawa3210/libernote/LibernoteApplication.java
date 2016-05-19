package com.okugawa3210.libernote;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.okugawa3210.libernote.di.ActivityComponent;
import com.okugawa3210.libernote.di.ActivityModule;
import com.okugawa3210.libernote.di.AppComponent;
import com.okugawa3210.libernote.di.AppModule;
import com.okugawa3210.libernote.di.DaggerAppComponent;
import com.okugawa3210.libernote.di.FragmentComponent;
import com.okugawa3210.libernote.di.FragmentModule;

public class LibernoteApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static Context context;
    private static Activity currentActivity;
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);

        LibernoteApplication.context = getApplicationContext();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @NonNull
    public AppComponent getComponent() {
        return appComponent;
    }

    @NonNull
    public static FragmentComponent getComponent(Fragment fragment) {
        assert fragment.getActivity() != null;
        AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();
        LibernoteApplication application = (LibernoteApplication) fragment.getContext().getApplicationContext();
        return application.appComponent
                .plus(new ActivityModule(activity))
                .plus(new FragmentModule(fragment));
    }

    @NonNull
    public static ActivityComponent getComponent(AppCompatActivity activity) {
        LibernoteApplication application = (LibernoteApplication) activity.getApplicationContext();
        return application.appComponent
                .plus(new ActivityModule(activity));
    }

    public static Context getContext() {
        return LibernoteApplication.context;
    }

    public static Activity getCurrentActivity() {
        return LibernoteApplication.currentActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LibernoteApplication.currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LibernoteApplication.currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static void hideSoftInputFromWindow() {
        View focused = getCurrentActivity().getCurrentFocus();
        if (focused != null) {
            InputMethodManager imm = (InputMethodManager) getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
        }
    }
}
