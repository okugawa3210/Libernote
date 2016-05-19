package com.okugawa3210.libernote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.github.gfx.android.orma.migration.OrmaMigration;
import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.model.OrmaDatabase;
import com.okugawa3210.libernote.task.MigrationTask;
import com.okugawa3210.libernote.task.base.BaseTask;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

    @Inject
    OrmaDatabase orma;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_splash);

        OrmaMigration.builder(this)
                .schemaHashForSchemaDiffMigration(OrmaDatabase.SCHEMA_HASH)
                .build();

        LibernoteApplication.getComponent(this).inject(this);

        new MigrationTask(orma).execute(new MigrationTask.OnCompleted() {
            @Override
            public void doAction() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 500);
            }
        }, new BaseTask.OnError() {
            @Override
            public void doAction() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage("error");
                builder.setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
