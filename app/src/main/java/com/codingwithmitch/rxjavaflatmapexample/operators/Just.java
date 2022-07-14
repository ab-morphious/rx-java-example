package com.codingwithmitch.rxjavaflatmapexample.operators;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Just {

    private String TAG = "Just";
    private Observable<String> justObservable;

    public Disposable justOperator() {
        //just operator takes a max of 10 items
        justObservable = Observable.just(
                "Abel",
                "Yohan",
                "Seli"
        );

        Disposable justObserver =
                justObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        value -> Log.d(TAG, "justOperator: " + value),
                        throwable -> Log.d(TAG, "justOperator: " + throwable.getMessage()),
                        () -> Log.d(TAG, "justOperator: Done emitting..")
                );

        return justObserver;
    }
}
