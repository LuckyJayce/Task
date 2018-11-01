package com.shizhefei.task.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ITask;
import com.shizhefei.task.demo.R;
import com.shizhefei.task.imp.SimpleCallback;
import com.shizhefei.task.tasks.LinkTask;
import com.shizhefei.task.tasks.Tasks;


public class TaskSettingView extends FrameLayout {
    private TextView nameTextView;
    private EditText exceptionEditText;
    private EditText timeEditText;
    private TextView exceptionLabelTextView;

    public TaskSettingView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public TaskSettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TaskSettingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int time = 0;
        String exceptionName = null;
        String taskName = null;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TaskSettingView);
            if (ta != null) {
                time = ta.getInt(R.styleable.TaskSettingView_time, 0);
                exceptionName = ta.getString(R.styleable.TaskSettingView_exception);
                taskName = ta.getString(R.styleable.TaskSettingView_task_name);
                ta.recycle();
            }
        }


        LayoutInflater.from(getContext()).inflate(R.layout.layout_tasksetting, this);
        nameTextView = findViewById(R.id.tasksetting_name_textView);
        exceptionEditText = findViewById(R.id.tasksetting_exception_editText);
        timeEditText = findViewById(R.id.tasksetting_time_editText);
        exceptionLabelTextView = findViewById(R.id.tasksetting_exceptionLabel_textView);

        exceptionEditText.setText(exceptionName);
        nameTextView.setText(taskName);
        timeEditText.setText(String.valueOf(time));
    }

    public void setTaskName(String taskName) {
        nameTextView.setText(taskName);
    }

    public <DATA> IAsyncTask<DATA> buildTask(final DATA data) {
        final String exceptionName = exceptionEditText.getText().toString();
        int time = 0;
        try {
            time = Integer.parseInt(timeEditText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        final String taskName = nameTextView.getText().toString();
        final int finalTime = time;
        LinkTask<DATA> task = Tasks.async(new ITask<DATA>() {
            @Override
            public DATA execute(ProgressSender progressSender) throws Exception {
                Thread.sleep(finalTime);
                if (!TextUtils.isEmpty(exceptionName)) {
                    throw new Exception(taskName + "->" + exceptionName);
                }
                return data;
            }

            @Override
            public void cancel() {

            }

            @Override
            public String toString() {
                return taskName;
            }
        });
        return Tasks.wrapCallback(task, new UIUpdateCallback<DATA>());
    }

    private class UIUpdateCallback<DATA> extends SimpleCallback<DATA> {
        @Override
        public void onPreExecute(Object task) {
            super.onPreExecute(task);
            nameTextView.setBackgroundColor(Color.parseColor("#55cc55"));
        }

        @Override
        public void onPostExecute(Object task, Code code, Exception exception, DATA data) {
            nameTextView.setBackgroundColor(Color.TRANSPARENT);
            switch (code) {
                case EXCEPTION:
                    exceptionLabelTextView.setBackgroundColor(Color.parseColor("#fe7474"));
                    exceptionLabelTextView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exceptionLabelTextView.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }, 1000);
                    break;
            }
        }
    }
}