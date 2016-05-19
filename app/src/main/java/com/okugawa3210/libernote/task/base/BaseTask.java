package com.okugawa3210.libernote.task.base;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class BaseTask implements Observer<Void> {
    protected abstract void core() throws Exception;

    private OnCompleted completed;
    private OnError error;

    public void execute() {
        this.execute(null, null);
    }

    public void execute(OnCompleted completed) {
        this.execute(completed, null);
    }

    public void execute(OnCompleted completed, OnError error) {

        this.completed = completed;
        this.error = error;

        Observable.create(new Observable.OnSubscribe<Void>() {

            @Override
            public void call(Subscriber<? super Void> subscriber) {
                try {
                    core();
                } catch (final Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onCompleted() {
        if (completed != null) {
            completed.doAction();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (error != null) {
            error.doAction();
        }
    }

    @Override
    public void onNext(Void aVoid) {

    }

    public interface OnCompleted {
        void doAction();
    }

    public interface OnError {
        void doAction();
    }
}
