package com.codingwithmitch.rxjavaflatmapexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


import android.os.Bundle;
import android.util.Log;

import com.codingwithmitch.rxjavaflatmapexample.models.Comment;
import com.codingwithmitch.rxjavaflatmapexample.models.Post;
import com.codingwithmitch.rxjavaflatmapexample.requests.ServiceGenerator;

import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //ui
    private RecyclerView recyclerView;

    // vars
    private CompositeDisposable disposables = new CompositeDisposable();
    private RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        initRecyclerView();
        fetchPostObservable()
                .subscribeOn(Schedulers.io())
                .flatMap((post) -> fetchCommentsObservable(post)) // Observable<Post> + comments
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Observer<Post>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {
                                disposables.add(disposable);
                            }

                            @Override
                            public void onNext(Post post) {
                                Log.d(TAG, "onNext: ");
                                updatePost(post);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Log.e(TAG, "onError: "+throwable.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
        //TODO: learn map, buffer, throttleFirst, flatMap, concatMap, switchMap, merge , etc
    }

    private Observable<Post> fetchPostObservable() {
        return ServiceGenerator.getRequestApi()
                .getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(posts -> {
                    adapter.setPosts(posts);
                    return Observable.fromIterable(posts)
                            .subscribeOn(Schedulers.io());
                });
    }

    private Observable<Post> fetchCommentsObservable(final Post post) {
        return ServiceGenerator.getRequestApi().getComments(post.getId())
                .subscribeOn(Schedulers.io())
                .map(comments -> {
                    post.setComments(comments);
                    return post;
                })
                .subscribeOn(Schedulers.io());
    }

    private void updatePost(Post post) {
        adapter.updatePost(post);
    }

    private void initRecyclerView() {
        adapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
