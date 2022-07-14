package com.codingwithmitch.rxjavaflatmapexample.operators

import android.util.Log
import com.codingwithmitch.rxjavaflatmapexample.models.Student
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Map {
    private val TAG = "Map"
    lateinit var mapObserver: Observer<Student>
    var students = arrayOf<Student>(Student("Abel", 27),
                                        Student("Yohan", 4))
    fun mapOperator() : Disposable {
        val mapDisposable = Observable.fromArray(*students)
            .map {
                it.name.toUpperCase() + " " + it.age
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d(TAG, "mapOperator: "+ it)
            }
        return mapDisposable
    }
}