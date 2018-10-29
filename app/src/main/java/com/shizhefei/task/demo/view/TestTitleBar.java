package com.shizhefei.task.demo.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shizhefei.task.demo.R;

import org.w3c.dom.Text;

public class TestTitleBar extends FrameLayout {
    private View backButton;
    private TextView titleTextView;

    public TestTitleBar(@NonNull Context context) {
        super(context);
        init();
    }

    public TestTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_titlebar, this, false);
        addView(view);
        backButton = view.findViewById(R.id.titlebar_back_button);
        titleTextView = view.findViewById(R.id.titlebar_title_textView);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof Activity) {
                    ((Activity) v.getContext()).finish();
                }
            }
        });
    }

    public void setText(String text) {
        titleTextView.setText(text);
    }
}
