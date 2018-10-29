package com.shizhefei.task.demo.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shizhefei.task.demo.R;

public class ProgressDialog extends Dialog {
    private final TextView progressTextView;

    public ProgressDialog(Context context) {
        super(context);
        setContentView(R.layout.layout_progress);
        progressTextView = findViewById(R.id.progress_info_textView);
    }

    public void setText(String text) {
        progressTextView.setText(text);
    }
}
