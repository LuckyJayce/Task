package com.shizhefei.task;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

/**
 * Created by luckyjayce on 2017/4/16.
 */

public abstract class ProxyTask<DATA> implements IAsyncTask<DATA> {

    @Override
    public final RequestHandle execute(ResponseSender<DATA> sender) throws Exception {
        return getTask().execute(sender);
    }

    protected abstract IAsyncTask<DATA> getTask() throws Exception;
}
