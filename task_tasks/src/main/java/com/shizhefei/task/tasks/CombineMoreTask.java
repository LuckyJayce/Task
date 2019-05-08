package com.shizhefei.task.tasks;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.imp.SimpleCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luckyjayce on 2017/4/16.
 */

class CombineMoreTask<DATA> extends LinkTask<List<DATA>> {
    private final List<IAsyncTask<DATA>> asyncTasks;
    private boolean[] states;
    private Object[] datas;

    public CombineMoreTask(List<IAsyncTask<DATA>> asyncTasks) {
        this.asyncTasks = asyncTasks;
        states = new boolean[asyncTasks.size()];
        datas = new Object[asyncTasks.size()];
    }
//
//    @Override
//    public String toString() {
//        return "CombineTask->{task1:" + task1 + ",task2:" + task2 + ",task3:" + task3 + "}";
//    }

    @Override
    public RequestHandle execute(final ResponseSender<List<DATA>> sender) throws Exception {
        if (asyncTasks.isEmpty()) {
            sender.sendData(new ArrayList<DATA>(0));
            return null;
        }
        final SimpleTaskHelper<DATA> taskHelper = new SimpleTaskHelper<>();
        for (int i = 0; i < asyncTasks.size(); i++) {
            IAsyncTask<DATA> task = asyncTasks.get(i);
            final int finalI = i;
            taskHelper.execute(task, new SimpleCallback<DATA>() {
                @Override
                public void onProgress(Object task, int percent, long current, long total, Object extraData) {
                    super.onProgress(task, percent, current, total, extraData);
                    sender.sendProgress(current, total, extraData);
                }

                @Override
                public void onPostExecute(Object task, Code code, Exception exception, DATA d1) {
                    switch (code) {
                        case SUCCESS:
                            states[finalI] = true;
                            datas[finalI] = d1;
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
        }
        return taskHelper;
    }

    private void combine(ResponseSender<List<DATA>> sender) {
        for (boolean state : states) {
            if (!state) {
                return;
            }
        }
        List<DATA> list = new ArrayList<>(datas.length);
        for (Object data : datas) {
            list.add((DATA) data);
        }
        sender.sendData(list);
    }
}
