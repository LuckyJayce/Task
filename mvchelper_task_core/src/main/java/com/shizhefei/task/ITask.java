package com.shizhefei.task;


import com.shizhefei.mvc.ProgressSender;

/**
 * 同步Task，不需要自己开线程，execute的方法在后台执行可以执行超时任务
 * Created by LuckyJayce on 2016/7/17.
 */
public interface ITask<DATA> extends ISuperTask<DATA> {

    /**
     * @param progressSender 发送进度数据
     * @return 返回结果数据
     * @throws Exception 失败的时候通过异常抛出
     */
    DATA execute(ProgressSender progressSender) throws Exception;

    /**
     * 外面通过这个执行取消
     */
    void cancel();
}
