package com.shizhefei.task.demo.models.task.login;

import android.text.TextUtils;

import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.task.ITask;
import com.shizhefei.task.demo.exception.AuthException;
import com.shizhefei.task.demo.exception.BizException;
import com.shizhefei.task.demo.models.entities.User;
import com.shizhefei.task.demo.utils.HttpUtils;

public class LoginSyncTask implements ITask<User> {

    private String name;
    private String password;

    public LoginSyncTask(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    //ITask的execute函数是在后台线程调用执行，可以执行超时的任务
    @Override
    public User execute(ProgressSender progressSender) throws Exception {
        // 这里用百度首页模拟网络请求，如果网路出错的话，直接抛异常不会执行后面语句
        String text = HttpUtils.executeGet("https://www.baidu.com");
        Thread.sleep(300);
        if (TextUtils.isEmpty(name)) {
            throw new BizException("请输入用户名");
        }
        if (TextUtils.isEmpty(password)) {
            throw new BizException("请输入密码");
        }
        //错误的情况通过异常跑出
        if (!password.equals("111111")) {
            throw new AuthException("密码不正确");
        }
        return new User(name, 11);
    }

    @Override
    public void cancel() {
        //这里可以实现取消的逻辑, taskHelper.cancel会调用这里进行对应的取消
    }
}
