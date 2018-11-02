package com.shizhefei.task.tasks;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.function.Func0;

class DeferLinkTask<D> extends LinkTask<D> {
    private Func0<IAsyncTask<D>> func;

    public DeferLinkTask(Func0<IAsyncTask<D>> func) {
        this.func = func;
    }

    @Override
    public RequestHandle execute(ResponseSender<D> sender) throws Exception {
        IAsyncTask<D> call = func.call();
        return call.execute(sender);
    }
}
