package com.example.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<User> observable = getObservableUser();
        Observer<User> observer = getObserverUser();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    private Observable<User> getObservableUser() {
        // phát ra dữ liệu sau khi thực hiện công việc gì đó.
        List<User> listUser = getListUser();
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> emitter) throws Throwable {
                if (listUser.isEmpty()) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(new Exception());
                    }
                }
                for (User user : listUser) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(user);
                    }
                }
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }

            }
        });
    }

    private Observer<User> getObserverUser() {
        return new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("Chienhd", "onSubscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull User user) {
                Log.e("Chienhd", "onNext " + user.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("Chienhd", "onError");
            }

            @Override
            public void onComplete() {
                Log.e("Chienhd", "onComplete");
            }
        };
    }

    private List<User> getListUser() {
        List<User> list = new ArrayList<>();
        list.add(new User(1, "Chienhd 1"));
        list.add(new User(2, "Chienhd 2"));
        list.add(new User(3, "Chienhd 3"));
        list.add(new User(4, "Chienhd 4"));
        list.add(new User(5, "Chienhd 5"));
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}