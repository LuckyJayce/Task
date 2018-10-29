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
import com.shizhefei.task.demo.models.User;
import com.shizhefei.task.demo.models.task.LoginTask;
import com.shizhefei.task.imp.SimpleCallback;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TaskHelper<Object> taskHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameEditText = findViewById(R.id.login_name_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        loginButton = findViewById(R.id.login_login_button);

        taskHelper = new TaskHelper<>();

        loginButton.setOnClickListener(onClickListener);
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
            if (v == loginButton) {
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                taskHelper.execute(new LoginTask(name, password), new LoginCallback());
            }
        }
    };

    //登录回调
    private class LoginCallback extends SimpleCallback<User> {
        @Override
        public void onPreExecute(Object task) {
            super.onPreExecute(task);
            loginButton.setEnabled(false);
            loginButton.setText("登录中...");
        }

        @Override
        public void onPostExecute(Object task, Code code, Exception exception, User user) {
            loginButton.setEnabled(true);
            loginButton.setText("登录");
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
