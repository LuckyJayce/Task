package com.shizhefei.task.demo.view;

import android.content.Context;
import android.widget.Toast;

import com.shizhefei.task.Code;
import com.shizhefei.task.imp.SimpleCallback;

public class ToastCallback<DATA> extends SimpleCallback<DATA> {
    private Context context;

    public ToastCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onPreExecute(Object task) {
        super.onPreExecute(task);
        Toast.makeText(context, "开始执行", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostExecute(Object task, Code code, Exception exception, DATA data) {
        switch (code) {
            case SUCCESS:
                Toast.makeText(context, String.valueOf(data), Toast.LENGTH_LONG).show();
                break;
            case EXCEPTION:
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
