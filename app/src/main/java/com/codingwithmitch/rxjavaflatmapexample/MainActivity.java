package com.codingwithmitch.rxjavaflatmapexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import com.codingwithmitch.rxjavaflatmapexample.models.Post;
import com.codingwithmitch.rxjavaflatmapexample.operators.ConcatMap;
import com.codingwithmitch.rxjavaflatmapexample.operators.Create;
import com.codingwithmitch.rxjavaflatmapexample.operators.FlatMap;
import com.codingwithmitch.rxjavaflatmapexample.operators.From;
import com.codingwithmitch.rxjavaflatmapexample.operators.Just;
import com.codingwithmitch.rxjavaflatmapexample.operators.Map;
import com.codingwithmitch.rxjavaflatmapexample.requests.ServiceGenerator;
import android.os.Bundle;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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

        Just just = new Just();
        disposables.add(just.justOperator());

        From from = new From();
        disposables.add(from.fromOperator());

        Create create = new Create();
        disposables.add(create.createOperator());

        Map map = new Map();
        disposables.add(map.mapOperator());

        FlatMap flatMap = new FlatMap();
        disposables.add(flatMap.flatMapOperator());

        ConcatMap concatMap = new ConcatMap();
        disposables.add(concatMap.concatMapOperator());

        //TODO: some more operators, zip, merge, throttle, combine...


        initRecyclerView();
        fetchPostObservable()
                .subscribeOn(Schedulers.io())
                //Conctmap in action wooo hoo!
                //flatMap(this::fetchCommentsObservable)
                .concatMap(this::fetchCommentsObservable) // Observable<Post> + comments
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
        //TODO: Hilt
        //TODO: Brush up Retrofit
        //TODO: Brush up Room
        //TODO: Bush up Room + Retrofit + RxJava
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
