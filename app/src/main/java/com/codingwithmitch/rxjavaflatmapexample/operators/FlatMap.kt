package com.codingwithmitch.rxjavaflatmapexample.operators

import android.util.Log
import com.codingwithmitch.rxjavaflatmapexample.models.Student
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class FlatMap {
    private val TAG = "FlatMap"

    var students = listOf(
        Student("Abel", 27),
        Student("Yohan", 4)
    )

    fun flatMapOperator() : Disposable {
        val flatMapDisposable = Observable.fromIterable(students)
            .subscribeOn(Schedulers.io())
            .flatMap {
                val student = Student(it.name.uppercase(Locale.getDefault()), it.age)
                return@flatMap Observable.just(student)
            }
            .filter {
                it.age < 25
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d(TAG, "flatMapOperator: "+ it.name)
            }
        return flatMapDisposable
    }
}