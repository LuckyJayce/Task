package com.shizhefei.task.tasks;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ICallback;
import com.shizhefei.task.ResponseSenderCallback;

class WrapCallbackLinkTask<D> extends LinkTask<D> {
    private IAsyncTask<D> realTask;
    private ICallback<D> callback;

    public WrapCallbackLinkTask(IAsyncTask<D> task, ICallback<D> callback) {
        this.realTask = task;
        this.callback = callback;
    }

    @Override
    public RequestHandle execute(ResponseSender<D> sender) throws Exception {
        return new SimpleTaskHelper<>().execute(realTask, new ResponseSenderCallback<D>(sender) {
            @Override
            public void onPreExecute(Object task) {
                super.onPreExecute(task);
                callback.onPreExecute(task);
            }

            @Override
            public void onProgress(Object task, int percent, long current, long total, Object extraData) {
                super.onProgress(task, percent, current, total, extraData);
                callback.onProgress(task, percent, current, total, extraData);
            }

            @Override
            public void onPostExecute(Object task, Code code, Exception exception, D data) {
                super.onPostExecute(task, code, exception, data);
                callback.onPostExecute(task, code, exception, data);
                //执行取消置空，避免WrapCallbackLinkTask被持有导致callback不能及时释放
                //执行失败或者finish不能释放有可能会通过retry继续调用
                if (code == Code.CANCEL) {
                    callback = null;
                    realTask = null;
                }
            }
        });
    }
}
