package com.shizhefei.task.demo.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.shizhefei.task.Code;
import com.shizhefei.task.TaskHandle;
import com.shizhefei.task.TaskHelper;
import com.shizhefei.task.demo.R;
import com.shizhefei.task.demo.models.entities.User;
import com.shizhefei.task.demo.models.task.login.LoginAsyncTask;
import com.shizhefei.task.function.Func1;
import com.shizhefei.task.imp.SimpleCallback;
import com.shizhefei.task.tasks.Tasks;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private Button downloadButton;
    private Button opButton;
    private Button testcaseButton;
    private TaskHelper<Object> taskHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskHelper = new TaskHelper<>();
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.main_login_button);
        registerButton = findViewById(R.id.main_register_button);
        downloadButton = findViewById(R.id.main_download_button);
        opButton = findViewById(R.id.main_complex_button);
        testcaseButton = findViewById(R.id.main_testcase_button);

        loginButton.setOnClickListener(onClickListener);
        registerButton.setOnClickListener(onClickListener);
        downloadButton.setOnClickListener(onClickListener);
        opButton.setOnClickListener(onClickListener);
        testcaseButton.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskHelper.destroy();
    }

    TaskHandle taskHandle;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == loginButton) {
                if (taskHandle == null || !taskHandle.isRunning()) {
                    taskHandle = taskHelper.execute(Tasks.create(new LoginAsyncTask("a", "111111")).map(new Func1<User, Object>() {
                        @Override
                        public Object call(User data) throws Exception {
                            throw new RuntimeException("失败");
                        }
                    }), new SimpleCallback<Object>() {
                        @Override
                        public void onPostExecute(Object task, Code code, Exception exception, Object o) {

                        }
                    });
                }
//
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else if (v == registerButton) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            } else if (v == downloadButton) {
                startActivity(new Intent(getApplicationContext(), DownloadActivity.class));
            } else if (v == opButton) {
                startActivity(new Intent(getApplicationContext(), OPActivity.class));
            } else if (v == testcaseButton) {
                startActivity(new Intent(getApplicationContext(), TestCaseActivity.class));
            }
        }
    };
}
