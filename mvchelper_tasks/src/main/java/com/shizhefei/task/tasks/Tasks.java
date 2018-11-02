package com.shizhefei.task.tasks;


import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.IDataSource;
import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ICallback;
import com.shizhefei.task.ITask;
import com.shizhefei.task.function.Func0;
import com.shizhefei.task.function.Func1;
import com.shizhefei.task.function.Func2;
import com.shizhefei.task.function.Func3;
import com.shizhefei.task.function.LogFuncs;

/**
 * Created by luckyjayce on 2017/4/16.
 */

public class Tasks {

    /**
     * 将一个同步task转化为异步task
     *
     * @param task
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> async(ITask<DATA> task) {
        return new AsyncLinkTask<>(task, true);
    }

    /**
     * 延迟创建task
     *
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> defer(Func0<IAsyncTask<DATA>> func) {
        return new DeferLinkTask<>(func);
    }

    /**
     * 通过异步task创建一个链式task
     *
     * @param <DATA>
     * @param task
     * @param func1
     * @return 一个链式task
     */
    public static <D, DATA> LinkTask<DATA> map(IAsyncTask<D> task, Func1<D, DATA> func1) {
        return new MapLinkTask<>(task, func1);
    }

    public static <DATA> LinkTask<DATA> just(DATA data) {
        return new DataLinkTask<>(data);
    }

    /**
     * 通过同步task创建一个链式task
     *
     * @param task
     * @param <DATA>
     * @return 一个链式task
     */
    public static <DATA> LinkTask<DATA> create(ITask<DATA> task) {
        return new AsyncLinkTask<>(task, true);
    }

    /**
     * 通过异步task创建一个链式task
     *
     * @param task
     * @param <DATA>
     * @return 一个链式task
     */
    public static <DATA> LinkTask<DATA> create(IAsyncTask<DATA> task) {
        if (task instanceof LinkTask) {
            return (LinkTask<DATA>) task;
        }
        return new LinkProxyTask<>(task);
    }

    /**
     * 通过一个同步dataSource创建一个链式task
     *
     * @param dataSource
     * @param isExeRefresh true：dataSource的refresh转成task，false：dataSource的loadMore转成task
     * @param <DATA>
     * @return 一个链式task
     */
    public static <DATA> LinkTask<DATA> create(IDataSource<DATA> dataSource, boolean isExeRefresh) {
        return new AsyncLinkTask<>(dataSource, isExeRefresh);
    }

    /**
     * 通过一个异步dataSource创建一个链式task
     *
     * @param dataSource
     * @param isExeRefresh true：dataSource的refresh转成task，false：dataSource的loadMore转成task
     * @param <DATA>
     * @return 一个链式task
     */
    public static <DATA> LinkTask<DATA> create(IAsyncDataSource<DATA> dataSource, boolean isExeRefresh) {
        return new AsyncLinkTask<>(dataSource, isExeRefresh);
    }

    /**
     * 按两个task先后顺序执行
     *
     * @param task
     * @param task2
     * @param <D>
     * @param <DATA>
     * @return
     */
    public static <D, DATA> LinkTask<DATA> concatWith(IAsyncTask<D> task, final IAsyncTask<DATA> task2) {
        return concatMap(task, new Func1<D, IAsyncTask<DATA>>() {
            @Override
            public IAsyncTask<DATA> call(D data) throws Exception {
                return task2;
            }
        });
    }

    /**
     * 按两个task先后顺序执行，task1的结果可以作为task的参数
     *
     * @param task   task1
     * @param func   将task的结果传给task2，返回一个可执行task2
     * @param <D>    数据1
     * @param <DATA> 结果数据
     * @return
     */
    public static <D, DATA> LinkTask<DATA> concatMap(IAsyncTask<D> task, Func1<D, IAsyncTask<DATA>> func) {
        return new ConcatLinkTask<>(task, LogFuncs.made("concat", func));
    }


    /**
     * 合并两个task，两个task一起执行，其中一个报错就停止执行，func将两个d1和d2的结果转化成最终的data
     *
     * @param task1
     * @param task2
     * @param func
     * @param <D1>
     * @param <D2>
     * @param <DATA>
     * @return
     */
    public static <D1, D2, DATA> LinkTask<DATA> combine(IAsyncTask<D1> task1, IAsyncTask<D2> task2, Func2<D1, D2, DATA> func) {
        return new CombineTask<>(task1, task2, LogFuncs.made("combine", func));
    }

    /**
     * 合并三个task，三个task一起执行，其中一个报错就停止执行，func将两个d1,d2和d3的结果转化成最终的data
     *
     * @param task1
     * @param task2
     * @param func
     * @param <D1>
     * @param <D2>
     * @param <DATA>
     * @return
     */
    public static <D1, D2, D3, DATA> LinkTask<DATA> combine(IAsyncTask<D1> task1, IAsyncTask<D2> task2, IAsyncTask<D3> task3, Func3<D1, D2, D3, DATA> func) {
        return new Combine3Task<>(task1, task2, task3, LogFuncs.made("combine", func));
    }


    /**
     * 按两个task先后顺序执行，task1的结果可以作为task的参数
     *
     * @param task   task1
     * @param func   将task的结果传给task2，返回一个可执行task2
     * @param <D>    数据1
     * @param <DATA> 结果数据
     * @return
     */
    public static <D, DATA> LinkTask<DATA> concatMap(IAsyncTask<D> task, Func3<Code, Exception, D, IAsyncTask<DATA>> func) {
        return new ConcatResultLinkTask<>(task, LogFuncs.made("concat", func));
    }

    /**
     * 失败重试， task执行过程出现异常时调用
     *
     * @param task
     * @param maxTimes 失败的最大次数，超过这个次数不再重试
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> retry(IAsyncTask<DATA> task, final int maxTimes) {
        return new RetryLinkTask<>("retry", task, maxTimes);
    }

    /**
     * 失败重试， task执行过程出现异常时调用
     *
     * @param task
     * @param func2  重试的处理逻辑 func2的call 返回重试的task，如果返回null表示不继续重试
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> retry(IAsyncTask<DATA> task, Func2<IAsyncTask<DATA>, Exception, IAsyncTask<DATA>> func2) {
        return new RetryLinkTask<>(task, LogFuncs.made("retry", func2));
    }


    /**
     * 延迟执行
     *
     * @param task
     * @param delay  延迟时间，单位：毫秒
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> delay(IAsyncTask<DATA> task, long delay) {
        return new DelayLinkTask<>(task, delay);
    }

    /**
     * 超时timeout毫秒任务直接失败，Callback接收到TimeoutException不再接收task返回的数据，task会被调用cancel执行取消逻辑
     *
     * @param task
     * @param timeout 超时时间，单位：毫秒
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> timeout(IAsyncTask<DATA> task, long timeout) {
        return new TimeoutLinkTask<>(task, timeout);
    }

    /**
     * 将task和callback组成一个新的task
     *
     * @param task
     * @param callback
     * @param <DATA>
     * @return
     */
    public static <DATA> IAsyncTask<DATA> wrapCallback(final IAsyncTask<DATA> task, final ICallback<DATA> callback) {
        return new WrapCallbackLinkTask<>(task, callback);
    }
}
