package com.shizhefei.task.tasks;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.function.Func3;
import com.shizhefei.task.imp.SimpleCallback;

/**
 * Created by luckyjayce on 2017/4/16.
 */

class Combine3Task<D1, D2, D3, DATA> extends LinkTask<DATA> {
    private final IAsyncTask<D3> task3;
    private IAsyncTask<D1> task1;
    private IAsyncTask<D2> task2;
    private Func3<D1, D2, D3, DATA> func2;
    private D1 data1;
    private D2 data2;
    private D3 data3;
    private boolean d1Success;
    private boolean d2Success;
    private boolean d3Success;

    public Combine3Task(IAsyncTask<D1> task1, IAsyncTask<D2> task2, IAsyncTask<D3> task3, Func3<D1, D2, D3, DATA> func2) {
        this.task1 = task1;
        this.task2 = task2;
        this.task3 = task3;
        this.func2 = func2;
    }

    @Override
    public RequestHandle execute(final ResponseSender<DATA> sender) throws Exception {
        final SimpleTaskHelper taskHelper = new SimpleTaskHelper();
        taskHelper.execute(task1, new SimpleCallback<D1>() {
            @Override
            public void onProgress(Object task, int percent, long current, long total, Object extraData) {
                super.onProgress(task, percent, current, total, extraData);
                sender.sendProgress(current, total, extraData);
            }

            @Override
            public void onPostExecute(Object task, Code code, Exception exception, D1 d1) {
                switch (code) {
                    case SUCCESS:
                        data1 = d1;
                        d1Success = true;
                        combine(sender);
                        break;
                    case EXCEPTION:
                        sender.sendError(exception);
                        taskHelper.cancelAll();
                        break;
                    case CANCEL:
                        break;
                }
            }
        });
        taskHelper.execute(task2, new SimpleCallback<D2>() {
            @Override
            public void onProgress(Object task, int percent, long current, long total, Object extraData) {
                super.onProgress(task, percent, current, total, extraData);
                sender.sendProgress(current, total, extraData);
            }

            @Override
            public void onPostExecute(Object task, Code code, Exception exception, D2 d2) {
                switch (code) {
                    case SUCCESS:
                        data2 = d2;
                        d2Success = true;
                        combine(sender);
                        break;
                    case EXCEPTION:
                        sender.sendError(exception);
                        taskHelper.cancelAll();
                        break;
                    case CANCEL:
                        break;
                }
            }
        });
        taskHelper.execute(task3, new SimpleCallback<D3>() {
            @Override
            public void onProgress(Object task, int percent, long current, long total, Object extraData) {
                super.onProgress(task, percent, current, total, extraData);
                sender.sendProgress(current, total, extraData);
            }

            @Override
            public void onPostExecute(Object task, Code code, Exception exception, D3 d3) {
                switch (code) {
                    case SUCCESS:
                        data3 = d3;
                        d3Success = true;
                        combine(sender);
                        break;
                    case EXCEPTION:
                        sender.sendError(exception);
                        taskHelper.cancelAll();
                        break;
                    case CANCEL:
                        break;
                }
            }
        });
        return taskHelper;
    }

    private void combine(ResponseSender<DATA> sender) {
        if (d1Success && d2Success && d3Success) {
            try {
                DATA data = func2.call(data1, data2, data3);
                sender.sendData(data);
            } catch (Exception e) {
                sender.sendError(e);
            }
        }
    }

    @Override
    public String toString() {
        return "CombineTask->{task1:" + task1 + ",task2:" + task2 + ",task3:" + task3 + "}";
    }
}
