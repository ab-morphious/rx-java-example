package com.codingwithmitch.rxjavaflatmapexample.operators


import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class From {
    val TAG = "From"
    lateinit var fromObserver: Observer<String>
    val names = arrayOf("Seli", "Yohan", "Abel")

    fun fromOperator() : Disposable {
      val disposable: Disposable = Observable.fromArray(*names)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {  Log.d(TAG, "fromOperator: " + it) },

                { throwable ->Log.d(TAG, "fromOperator: " + throwable.message) },

                { Log.d(TAG, "fromOperator: "+ "done emmiting.") }
            )
        return disposable
    }
}