package com.shizhefei.task.demo.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shizhefei.task.Code;
import com.shizhefei.task.TaskHelper;
import com.shizhefei.task.demo.R;
import com.shizhefei.task.demo.models.entities.User;
import com.shizhefei.task.demo.models.task.login.LoginAsyncTask;
import com.shizhefei.task.demo.models.task.login.LoginSyncTask;
import com.shizhefei.task.demo.view.ProgressDialog;
import com.shizhefei.task.imp.SimpleCallback;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button loginAsyncButton;
    private TaskHelper<Object> taskHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameEditText = findViewById(R.id.login_name_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        loginButton = findViewById(R.id.login_login_button);
        loginAsyncButton = findViewById(R.id.login_login_async_button);

        taskHelper = new TaskHelper<>();

        loginButton.setOnClickListener(onClickListener);
        loginAsyncButton.setOnClickListener(onClickListener);
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
            String name = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (v == loginButton) {
                taskHelper.execute(new LoginSyncTask(name, password), new LoginCallback());
            } else if (v == loginAsyncButton) {
                taskHelper.execute(new LoginAsyncTask(name, password), new LoginCallback());
            }
        }
    };

    //登录回调
    private class LoginCallback extends SimpleCallback<User> {
        private ProgressDialog progressDialog;

        @Override
        public void onPreExecute(Object task) {
            super.onPreExecute(task);
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setText("登录中..");
            progressDialog.show();
        }

        @Override
        public void onPostExecute(Object task, Code code, Exception exception, User user) {
            progressDialog.dismiss();
            switch (code) {
                case SUCCESS:
                    Toast.makeText(getApplicationContext(), "登录成功:" + user, Toast.LENGTH_LONG).show();
                    break;
                case EXCEPTION:
                    Toast.makeText(getApplicationContext(), "登录失败:" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
