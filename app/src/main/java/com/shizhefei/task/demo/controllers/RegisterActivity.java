package com.shizhefei.task.demo.controllers;

import android.os.Environment;
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
import com.shizhefei.task.demo.models.task.register.RegisterTask;
import com.shizhefei.task.demo.view.ProgressDialog;
import com.shizhefei.task.imp.SimpleCallback;

import java.io.File;

/**
 * 注册界面, 多个task组合demo
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TaskHelper<Object> taskHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEditText = findViewById(R.id.register_name_editText);
        passwordEditText = findViewById(R.id.register_password_editText);
        registerButton = findViewById(R.id.register_register_button);

        taskHelper = new TaskHelper<>();

        registerButton.setOnClickListener(onClickListener);
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
            if (v == registerButton) {
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                String imagePath = new File(Environment.getExternalStorageDirectory(), "a.png").getPath();
                taskHelper.execute(new RegisterTask(name, 11, password, imagePath), new RegisterCallback());
            }
        }
    };

    //注册回调
    private class RegisterCallback extends SimpleCallback<User> {
        private ProgressDialog progressDialog;

        @Override
        public void onPreExecute(Object task) {
            super.onPreExecute(task);
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setText("注册中..");
            progressDialog.show();
        }

        @Override
        public void onPostExecute(Object task, Code code, Exception exception, User user) {
            progressDialog.dismiss();
            switch (code) {
                case SUCCESS:
                    Toast.makeText(getApplicationContext(), "注册成成功:" + user, Toast.LENGTH_LONG).show();
                    break;
                case EXCEPTION:
                    Toast.makeText(getApplicationContext(), "注册失败:" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
