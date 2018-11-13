package com.shizhefei.task.tasks;

import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.IDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ISuperTask;
import com.shizhefei.task.ITask;
import com.shizhefei.task.ResponseSenderCallback;
import com.shizhefei.task.TaskHelper;

import java.util.concurrent.Executor;

class AsyncLinkTask<DATA> extends LinkTask<DATA> {
    private final boolean isExeRefresh;
    private Executor executor;
    private ISuperTask<DATA> task;

    public AsyncLinkTask(ISuperTask<DATA> task, boolean isExeRefresh) {
        this.task = task;
        this.isExeRefresh = isExeRefresh;
    }

    public AsyncLinkTask(ISuperTask<DATA> task, boolean isExeRefresh, Executor executor) {
        this.task = task;
        this.isExeRefresh = isExeRefresh;
        this.executor = executor;
    }


    @Override
    public RequestHandle execute(final ResponseSender<DATA> sender) throws Exception {
        if (task instanceof ITask) {
            if (executor != null) {
                return TaskHelper.create((ITask<DATA>) task, new ResponseSenderCallback<>(sender), executor).execute();
            } else {
                return TaskHelper.createExecutor((ITask<DATA>) task, new ResponseSenderCallback<>(sender)).execute();
            }
        } else if (task instanceof IAsyncTask) {
            return TaskHelper.createExecutor((IAsyncTask<DATA>) task, new ResponseSenderCallback<>(sender)).execute();
        } else if (task instanceof IDataSource) {
            if (executor != null) {
                return TaskHelper.create((IDataSource<DATA>) task, isExeRefresh, new ResponseSenderCallback<>(sender), executor).execute();
            } else {
                return TaskHelper.createExecutor((IDataSource<DATA>) task, isExeRefresh, new ResponseSenderCallback<>(sender)).execute();
            }
        } else if (task instanceof IAsyncDataSource) {
            return TaskHelper.createExecutor((IAsyncDataSource<DATA>) task, isExeRefresh, new ResponseSenderCallback<>(sender)).execute();
        }
        return null;
    }

    @Override
    public String toString() {
        return "AsyncLinkTask->{" + task + "}";
    }
}
