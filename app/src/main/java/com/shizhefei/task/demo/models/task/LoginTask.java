package com.shizhefei.task.demo.models.task;

import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.task.ITask;
import com.shizhefei.task.demo.exception.AuthException;
import com.shizhefei.task.demo.models.User;

public class LoginTask implements ITask<User> {
    private String name;
    private String password;

    public LoginTask(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public User execute(ProgressSender progressSender) throws Exception {
        Thread.sleep(1000);

        if (!password.equals("111111")) {
            throw new AuthException("密码不正确");
        }
        return new User(name, 11);
    }

    @Override
    public void cancel() {

    }
}
