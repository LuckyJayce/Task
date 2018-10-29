package com.shizhefei.task.demo.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shizhefei.task.Code;
import com.shizhefei.task.ICallback;
import com.shizhefei.task.TaskHelper;
import com.shizhefei.task.demo.R;
import com.shizhefei.task.demo.models.task.download.DownloadAsyncTask;
import com.shizhefei.task.demo.models.task.download.DownloadSyncTask;
import com.shizhefei.task.demo.view.ProgressDialog;

public class DownloadActivity extends AppCompatActivity {

    private Button asyncButton;
    private Button syncButton;
    private TaskHelper<Object> taskHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        asyncButton = findViewById(R.id.download_async_button);
        syncButton = findViewById(R.id.download_sync_button);

        taskHelper = new TaskHelper<>();

        asyncButton.setOnClickListener(onClickListener);
        syncButton.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskHelper.destroy();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = "";
            String filePath = "";
            if (v == asyncButton) {
                taskHelper.execute(new DownloadAsyncTask(url, filePath), new DownloadCallback());
            } else if (v == syncButton) {
                taskHelper.execute(new DownloadSyncTask(url, filePath), new DownloadCallback());
            }
        }
    };

    /**
     * 下载回调
     */
    private class DownloadCallback implements ICallback<String> {

        private ProgressDialog progressDialog;

        @Override
        public void onPreExecute(Object task) {
            progressDialog = new ProgressDialog(DownloadActivity.this);
            progressDialog.setText("开始下载:");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        public void onProgress(Object task, int percent, long current, long total, Object extraData) {
            progressDialog.setText("正在下载:current " + current + " total:" + total);
        }

        @Override
        public void onPostExecute(Object task, Code code, Exception exception, String s) {
            progressDialog.dismiss();
            switch (code) {
                case SUCCESS:
                    Toast.makeText(getApplicationContext(), "下载成功:" + s, Toast.LENGTH_LONG).show();
                    break;
                case EXCEPTION:
                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
