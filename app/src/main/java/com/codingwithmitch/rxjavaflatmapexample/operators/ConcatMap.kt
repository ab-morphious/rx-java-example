package com.codingwithmitch.rxjavaflatmapexample.operators

import android.util.Log
import com.codingwithmitch.rxjavaflatmapexample.models.Student
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ConcatMap {
    private val TAG = "FlatMap"

    private var students = listOf(
        Student("Abel", 27),
        Student("Yohan", 4)
    )

    fun concatMapOperator() : Disposable {
        val concatMapDisposable = Observable.fromIterable(students)
            .subscribeOn(Schedulers.io())
            .concatMap {
                val student = Student(it.name.uppercase(Locale.getDefault()), it.age)
                return@concatMap Observable.just(student)
            }
            .filter {
                it.age < 25
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d(TAG, "concatMapOperator: "+ it.name)
            }
        return concatMapDisposable
    }
}