package com.shizhefei.task.tasks;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

class DataLinkTask<DATA, CD extends DATA> extends LinkTask<DATA> {
    private CD data;

    public DataLinkTask(CD data) {
        this.data = data;
    }

    @Override
    public RequestHandle execute(ResponseSender<DATA> sender) throws Exception {
        sender.sendData(data);
        return null;
    }

    @Override
    public String toString() {
        return "DataLinkTask->{data:" + data+"}";
    }
}