package com.shizhefei.task.tasks;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class RxTasks {

    /**
     * 将observable转化为task
     *
     * @param observable
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> task(Observable<DATA> observable) {
        return new RxTask<>(observable);
    }

    /**
     * 将observable转化为task
     *
     * @param observable
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> task(Flowable<DATA> observable) {
        return new RxTask<>(observable.toObservable());
    }

    /**
     * 将observable转化为task
     *
     * @param observable
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> task(Single<DATA> observable) {
        return new RxTask<>(observable.toObservable());
    }

    private static class RxTask<DATA> extends LinkTask<DATA> {
        private final Observable<DATA> observable;

        public RxTask(Observable<DATA> observable) {
            this.observable = observable;
        }

        @Override
        public RequestHandle execute(final ResponseSender<DATA> sender) throws Exception {
            final DisposableObserver<DATA> disposableObserver = new DisposableObserver<DATA>() {
                private volatile DATA data;

                @Override
                public void onNext(DATA data) {
                    this.data = data;
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof Exception) {
                        sender.sendError((Exception) e);
                    } else {
                        sender.sendError(new Exception(e));
                    }
                }

                @Override
                public void onComplete() {
                    sender.sendData(data);
                }
            };
            observable.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(disposableObserver);
            return new RequestHandle() {
                @Override
                public void cancle() {
                    if (!disposableObserver.isDisposed()) {
                        disposableObserver.dispose();
                    }
                }

                @Override
                public boolean isRunning() {
                    return false;
                }
            };
        }
    }
}
