package com.shizhefei.task.tasks;

import com.shizhefei.task.Code;
import com.shizhefei.task.ICallback;
import com.shizhefei.task.function.Action0;
import com.shizhefei.task.function.Action1;

class WrapCallback<D> implements ICallback<D> {
    private Action1<D> completedAction;
    private Action1<Exception> errorAction;
    private Action0 cancelAction;

    public WrapCallback(Action1<D> completedAction, Action1<Exception> errorAction, Action0 cancelAction) {
        this.completedAction = completedAction;
        this.errorAction = errorAction;
        this.cancelAction = cancelAction;
    }

    @Override
    public void onPreExecute(Object task) {

    }

    @Override
    public void onProgress(Object task, int percent, long current, long total, Object extraData) {

    }

    @Override
    public void onPostExecute(Object task, Code code, Exception exception, D d) {
        switch (code) {
            case SUCCESS:
                completedAction.call(d);
                break;
            case EXCEPTION:
                errorAction.call(exception);
                break;
            case CANCEL:
                cancelAction.call();
                break;
        }
    }
}
