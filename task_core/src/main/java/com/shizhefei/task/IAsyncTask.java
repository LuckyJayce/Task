package com.shizhefei.task;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

/**
 * 异步Task，需要自己开线程执行异步操作，execute的方法在主线程执行，不能执行超时任务
 * Created by LuckyJayce on 2016/7/17.
 */
public interface IAsyncTask<DATA> extends ISuperTask<DATA> {

    /**
     * @param sender 发送进度或者结果数据，回调ICallback的onProgress或者onPost
     * @return 返回给外面一个取消句柄，外面通过这个执行取消
     * @throws Exception
     */
    RequestHandle execute(ResponseSender<DATA> sender) throws Exception;

}
