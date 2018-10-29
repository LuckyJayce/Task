package com.shizhefei.task.demo.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shizhefei.task.demo.R;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.main_login_button);
        registerButton = findViewById(R.id.main_register_button);

        loginButton.setOnClickListener(onClickListener);
        registerButton.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == loginButton) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else if (v == registerButton) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        }
    };
}
