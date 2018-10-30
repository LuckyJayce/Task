package com.shizhefei.task.demo.models.task.login;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.demo.exception.AuthException;
import com.shizhefei.task.demo.models.entities.User;

public class LoginAsyncTask implements IAsyncTask<User> {
    private String name;
    private String password;

    public LoginAsyncTask(String name, String password) {
        this.name = name;
        this.password = password;
    }

    //这个函数是在主线程调用执行，不可以执行超时的任务
    // 可以自己开线程或者其它框架有线程，返回结果后再通过调用sender在异步线程调用返回结果
    @Override
    public RequestHandle execute(ResponseSender<User> sender) throws Exception {
        if (!password.equals("111111")) {
            //错误的情况通过异常抛出
            sender.sendError(new AuthException("密码不正确"));
        } else {
            //返回正确的结果
            sender.sendData(new User(name, 11));
        }
        //返回取消的句柄，如果不需要取消逻辑直接返回null，如果有的话实现RequestHandle
        return null;
    }

}
