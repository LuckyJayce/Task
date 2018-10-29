package com.shizhefei.task.demo.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shizhefei.mvchelper.testcase.ABSTestCaseFragment;
import com.shizhefei.mvchelper.testcase.TestCaseData;
import com.shizhefei.task.demo.R;
import com.shizhefei.task.demo.models.task.login.LoginSyncTask;

import java.util.ArrayList;
import java.util.List;

public class TestCaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_case);
    }

    public static class TestCaseFrFragment extends ABSTestCaseFragment {

        @Override
        protected List<TestCaseData> getTestCaseDatas() {
            List<TestCaseData> data = new ArrayList<>();
            data.add(new TestCaseData("登录", new LoginSyncTask("小明", "111111")));
            return data;
        }
    }
}
