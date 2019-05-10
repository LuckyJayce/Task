package com.shizhefei.task.tasks;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.function.Func1;
import com.shizhefei.task.imp.SimpleCallback;

class MapLinkTask<D, DATA> extends LinkTask<DATA> {
    private final IAsyncTask<D> task;
    private Func1<D, DATA> func1;

    public MapLinkTask(IAsyncTask<D> task, Func1<D, DATA> func1) {
        this.task = task;
        this.func1 = func1;
    }

    @Override
    public RequestHandle execute(final ResponseSender<DATA> sender) throws Exception {
        SimpleTaskHelper<Object> simpleTaskHelper = new SimpleTaskHelper<>();
        simpleTaskHelper.execute(task, new SimpleCallback<D>() {
            @Override
            public void onProgress(Object task, int percent, long current, long total, Object extraData) {
                super.onProgress(task, percent, current, total, extraData);
                sender.sendProgress(current, total, extraData);
            }

            @Override
            public void onPostExecute(Object task, Code code, Exception exception, D d) {
                switch (code) {
                    case SUCCESS:
                        try {
                            sender.sendData(func1.call(d));
                        } catch (Exception e) {
                            e.printStackTrace();
                            sender.sendError(e);
                        }
                        break;
                    case EXCEPTION:
                        sender.sendError(exception);
                        break;
                    case CANCEL:
                        break;
                }
            }
        });
        return simpleTaskHelper;
    }
}
