package com.shizhefei.task.demo.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ProxyTask;
import com.shizhefei.task.TaskHelper;
import com.shizhefei.task.demo.R;
import com.shizhefei.task.demo.models.entities.HomeAdData;
import com.shizhefei.task.demo.models.entities.HomeConfigData;
import com.shizhefei.task.demo.models.entities.HomeData;
import com.shizhefei.task.demo.models.entities.HomeSaleData;
import com.shizhefei.task.demo.view.TaskSettingView;
import com.shizhefei.task.function.Func1;
import com.shizhefei.task.function.Func2;
import com.shizhefei.task.imp.SimpleCallback;
import com.shizhefei.task.tasks.Tasks;

public class OPActivity extends AppCompatActivity {

    private Button runButton;
    private TaskSettingView getConfigView;
    private TaskSettingView getAdView;
    private TaskSettingView getSaleView;
    private TaskSettingView reportView;
    private EditText delayEditText;
    private TaskHelper taskHelper;
    private EditText timeoutEditText;
    private EditText retryEditText;
    private View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op);

        rootLayout = findViewById(R.id.op_root_layout);
        runButton = findViewById(R.id.op_run_button);
        delayEditText = findViewById(R.id.op_delay_editText);
        timeoutEditText = findViewById(R.id.op_timeout_editText);
        retryEditText = findViewById(R.id.op_retry_editText);
        getConfigView = findViewById(R.id.op_gethomeconfig_taskSettingView);
        getAdView = findViewById(R.id.op_gethomead_taskSettingView);
        getSaleView = findViewById(R.id.op_gethomesale_taskSettingView);
        reportView = findViewById(R.id.op_report_taskSettingView);

        runButton.setOnClickListener(onClickListener);
        rootLayout.setOnClickListener(onClickListener);

        taskHelper = new TaskHelper();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskHelper.destroy();
    }

    /**
     * 点击事件
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == runButton) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(runButton.getWindowToken(), 0);
                }
                taskHelper.execute(new LoadTask(), new LoadCallback());
            } else if (v == rootLayout) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);
                }
            }
        }
    };

    private class LoadTask extends ProxyTask<String> {
        @Override
        protected IAsyncTask<String> getTask() {
            int delay = 0;
            try {
                delay = Integer.parseInt(delayEditText.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            int timeout = 0;
            try {
                timeout = Integer.parseInt(timeoutEditText.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            int retry = 0;
            try {
                retry = Integer.parseInt(retryEditText.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return Tasks.create(getConfigView.buildTask(new HomeConfigData())).delay(delay).concatMap(new Func1<HomeConfigData, IAsyncTask<HomeData>>() {
                @Override
                public IAsyncTask<HomeData> call(HomeConfigData data) throws Exception {
                    return Tasks.combine(getAdView.buildTask(new HomeAdData()), getSaleView.buildTask(new HomeSaleData()), new Func2<HomeAdData, HomeSaleData, HomeData>() {
                        @Override
                        public HomeData call(HomeAdData homeAdData, HomeSaleData homeSaleData) throws Exception {
                            return new HomeData(homeAdData, homeSaleData);
                        }
                    });
                }
            }).concatWith(reportView.buildTask("success")).retry(retry).timeout(timeout);
        }
    }

    private class LoadCallback extends SimpleCallback<String> {

        @Override
        public void onPostExecute(Object task, Code code, Exception exception, String s) {
            switch (code) {
                case SUCCESS:
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    break;
                case EXCEPTION:
                    Toast.makeText(getApplicationContext(), "出现异常:" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                case CANCEL:
                    Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
