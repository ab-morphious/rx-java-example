package com.codingwithmitch.rxjavaflatmapexample.operators

import android.util.Log
import com.codingwithmitch.rxjavaflatmapexample.models.Student
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class Create {
    val TAG = "Create"
    val students = arrayListOf(
        Student("Abel", 27),
        Student("Yeshwas", 28), Student
            ("Yaya", 29)
    )

    fun createOperator() : Disposable {
        val createObservable = Observable.create<Student> { emmiter ->

                for (student in students) {
                    emmiter.onNext(student)
                }
                emmiter.onComplete()
        }

        val createObserver = createObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { Log.d(TAG, "createOperator: ${it.name}") }

        return createObserver
    }

}