package com.shizhefei.task.demo.models.task.login;

import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.task.ITask;
import com.shizhefei.task.demo.exception.AuthException;
import com.shizhefei.task.demo.models.entities.User;

public class LoginSyncTask implements ITask<User> {
    private String name;
    private String password;

    public LoginSyncTask(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public User execute(ProgressSender progressSender) throws Exception {
        //这个函数是在后台线程调用执行，可以执行超时的任务
        Thread.sleep(1000);

        //错误的情况通过异常跑出
        if (!password.equals("111111")) {
            throw new AuthException("密码不正确");
        }
        //返回正确的结果
        return new User(name, 11);
    }

    @Override
    public void cancel() {
        //这里可以实现取消的逻辑, taskHelper.cancel会调用这里进行对应的取消
    }
}
