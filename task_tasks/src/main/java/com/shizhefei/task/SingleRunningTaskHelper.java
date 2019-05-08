package com.shizhefei.task;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.function.Func0;
import com.shizhefei.task.imp.SimpleCallback;

import java.util.LinkedHashSet;
import java.util.Set;

public class SingleRunningTaskHelper<DATA> {
    private TaskHandle taskHandle;
    private ProxyCallback<DATA> proxyCallback;
    private TaskHelper taskHelper;
    private Func0<IAsyncTask<DATA>> factory;

    public SingleRunningTaskHelper(TaskHelper taskHelper, Func0<IAsyncTask<DATA>> factory) {
        this.taskHelper = taskHelper;
        this.factory = factory;
    }

    public void execute() {
        execute(null);
    }

    public void execute(ICallback<DATA> callback) {
        if (proxyCallback == null) {
            proxyCallback = new ProxyCallback<>();
        }
        if (taskHandle == null || !taskHandle.isRunning()) {
            try {
                IAsyncTask<DATA> task = factory.call();
                registerCallback(callback);
                taskHandle = taskHelper.execute(task, proxyCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerCallback(callback);
        }
    }

    public IAsyncTask<DATA> buildTask() {
        return new IAsyncTask<DATA>() {
            @Override
            public RequestHandle execute(ResponseSender<DATA> responseSender) throws Exception {
                final ResponseSenderCallback<DATA> responseSenderCallback = new ResponseSenderCallback<>(responseSender);
                SingleRunningTaskHelper.this.execute(responseSenderCallback);
                return new RequestHandle() {
                    @Override
                    public void cancle() {
                        unregisterCallback(responseSenderCallback);
                    }

                    @Override
                    public boolean isRunning() {
                        return false;
                    }
                };
            }
        };
    }

    private void registerCallback(ICallback<DATA> callback) {
        if (callback != null) {
            if (taskHandle != null && taskHandle.isRunning()) {
                callback.onPreExecute(taskHandle.getTask());
            }
            proxyCallback.addCallback(callback);
        }
    }

    public void unregisterCallback(ICallback<DATA> callback) {
        if (proxyCallback != null) {
            proxyCallback.removeCallback(callback);
        }
    }

    public void cancel() {
        if (taskHandle != null) {
            taskHandle.cancle();
        }
    }

    private static class ProxyCallback<DATA> extends SimpleCallback<DATA> {
        private Set<ICallback<DATA>> callbacks = new LinkedHashSet<>();

        public void addCallback(ICallback<DATA> callback) {
            callbacks.add(callback);
        }

        public void removeCallback(ICallback<DATA> callback) {
            callbacks.remove(callback);
        }

        @Override
        public void onPreExecute(Object task) {
            super.onPreExecute(task);
            for (ICallback<DATA> callback : callbacks) {
                callback.onPreExecute(task);
            }
        }

        @Override
        public void onProgress(Object task, int percent, long current, long total, Object extraData) {
            super.onProgress(task, percent, current, total, extraData);
            for (ICallback<DATA> callback : callbacks) {
                callback.onProgress(task, percent, current, total, extraData);
            }
        }

        @Override
        public void onPostExecute(Object task, Code code, Exception e, DATA data) {
            for (ICallback<DATA> callback : callbacks) {
                callback.onPostExecute(task, code, e, data);
            }
        }
    }
}
