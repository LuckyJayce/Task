package com.shizhefei.task.tasks;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.function.Func1;

class MapLinkTask<D, DATA> extends LinkTask<DATA> {
    private final IAsyncTask<D> task;
    private Func1<D, DATA> func1;

    public MapLinkTask(IAsyncTask<D> task, Func1<D, DATA> func1) {
        this.task = task;
        this.func1 = func1;
    }

    @Override
    public RequestHandle execute(final ResponseSender<DATA> sender) throws Exception {
        return task.execute(new ResponseSender<D>() {
            @Override
            public void sendError(Exception exception) {
                sender.sendError(exception);
            }

            @Override
            public void sendData(D d) {
                try {
                    sender.sendData(func1.call(d));
                } catch (Exception e) {
                    e.printStackTrace();
                    sender.sendError(e);
                }
            }

            @Override
            public void sendProgress(long current, long total, Object extraData) {
                sender.sendProgress(current, total, extraData);
            }
        });
    }
}
