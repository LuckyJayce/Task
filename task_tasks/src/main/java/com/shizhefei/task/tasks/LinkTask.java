package com.shizhefei.task.tasks;


import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ICallback;
import com.shizhefei.task.function.Action0;
import com.shizhefei.task.function.Action1;
import com.shizhefei.task.function.Func1;
import com.shizhefei.task.function.Func2;
import com.shizhefei.task.function.Func3;

public abstract class LinkTask<D> implements IAsyncTask<D> {

    /**
     * 合并两个task，两个task一起执行，其中一个报错就停止执行，func将两个d1和d2的结果转化成最终的data
     *
     * @param task2
     * @param func
     * @param <D1>
     * @param <DATA>
     * @return
     */
    public <D1, DATA> LinkTask<DATA> combine(IAsyncTask<D1> task2, Func2<D, D1, DATA> func) {
        return Tasks.combine(this, task2, func);
    }

    /**
     * 按两个task先后顺序执行，task1的结果可以作为task的参数
     *
     * @param func   将task的结果传给task2，返回一个可执行task2
     * @param <DATA> 结果数据
     * @return
     */
    public <DATA> LinkTask<DATA> concatMap(Func1<D, IAsyncTask<DATA>> func) {
        return Tasks.concatMap(this, func);
    }

    /**
     * 按两个task先后顺序执行，task1的结果可以作为task的参数
     *
     * @param func   将task的结果传给task2，返回一个可执行task2
     * @param <DATA> 结果数据
     * @return
     */
    public <DATA> LinkTask<DATA> concatMap(Func3<Code, Exception, D, IAsyncTask<DATA>> func) {
        return Tasks.concatMap(this, func);
    }

    /**
     * 按两个task先后顺序执行
     *
     * @param task2
     * @param <DATA>
     * @return
     */
    public <DATA> LinkTask<DATA> concatWith(IAsyncTask<DATA> task2) {
        return Tasks.concatWith(this, task2);
    }

    /**
     * 失败重试， task执行过程出现异常时调用
     *
     * @param func2 重试的处理逻辑 func2的call 返回重试的task，如果返回null表示不继续重试
     * @return
     */
    public LinkTask<D> retry(Func2<IAsyncTask<D>, Exception, IAsyncTask<D>> func2) {
        return Tasks.retry(this, func2);
    }

    /**
     * 失败重试， task执行过程出现异常时调用
     *
     * @param maxTimes 失败的最大次数，超过这个次数不再重试
     * @return
     */
    public LinkTask<D> retry(int maxTimes) {
        return Tasks.retry(this, maxTimes);
    }

    /**
     * 超时timeout毫秒任务直接失败，Callback接收到TimeoutException不再接收task返回的数据，task会被调用cancel执行取消逻辑
     *
     * @param timeout 超时时间，单位：毫秒
     * @return
     */
    public LinkTask<D> timeout(long timeout) {
        return Tasks.timeout(this, timeout);
    }

    /**
     * 延迟执行
     *
     * @param delay 延迟时间，单位：毫秒
     * @return
     */
    public LinkTask<D> delay(long delay) {
        return Tasks.delay(this, delay);
    }

    public <DATA> LinkTask<DATA> map(Func1<D, DATA> func1) {
        return Tasks.map(this, func1);
    }

    public LinkTask<D> doOnCancel(Action0 onCancel) {
        return Tasks.doOnCancel(this, onCancel);
    }

    public LinkTask<D> doOnCompleted(Action1<D> completedAction) {
        return Tasks.doOnCompleted(this, completedAction);
    }

    public LinkTask<D> doOnError(Action1<Exception> onError) {
        return Tasks.doOnError(this, onError);
    }

    public LinkTask<D> doOnCallback(ICallback<D> callback) {
        return Tasks.donOnCallback(this, callback);
    }

    //
//    public LinkTask<D> retry() {
//        return Tasks.retry(this);
//    }
//

//
//    public ISuperTask<?>[] getChildTasks(){
//        return null;
//    }
//
//    public ISuperTask<?>[] getTasks(){
//        return null;
//    }
//
//    public ISuperTask<?>[] getCurrentChildTasks(){
//        return null;
//    }
//
//    public ISuperTask<?>[] getCurrentTasks(){
//        return null;
//    }
//
//    public Throwable[] getThrowables(){
//        return null;
//    }
}
