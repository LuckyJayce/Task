# Task #

该类库的用途：将执行过程抽象成Task

**【1】抽象性，面向接口编程，实现Task接口即可，所有执行过程都可以描述为一个task**

**【2】可组合，复杂的Task（复杂多个多个步骤的执行过程）可以由多个简单task组合成一个task**

例如：注册流程：

用户点击注册将头像，名字性别提交服务器：1.压缩头像，2上传头像到存储服务器返回头像url，3.将头像url和名字性别等信息提交服务器

可以分解成：压缩图片Task，上传图片Task，提交服务器数据Task

注册Task由 压缩图片Task，上传图片Task，提交服务器数据Task组合而成一个复杂Task，外面调用只关心注册Task。

**【3】单一原则，一个Task实现单独一种业务**

**【4】单元测试,  提供了TestCase界面方便直接测试**

该类库由之前自己的类库开始，后借鉴于RxJava的操作发和写法进行完善

与RxJava比较不同点

| RxJava      | Task |
| ------------- |:-------------:|
|Observable是类| Task是接口  |
|Observable是源源不断的相同类型流数据传递| task 有单独的进度通知和结束，实现进度比较容易 |
|通过Observable操作符进行组合，操作符逻辑都在Observable| 有单独提供Tasks类对个Task组合，Task只是接口 |
|Observable是流事件上个Observable会继续影响下个Observable| Task执行完成才会继续执行下个Task |
|Observable既可以是后期使用的时候指定线程| ITask和IAsyncTask直接区主线程执行还是后台执行，不用在使用的时候设置线程，当然使用的时候也提供了设置线程的方法 |

Download sample [Apk](https://github.com/LuckyJayce/MVCHelper/blob/master/raw/MVCHelper_Demo.apk?raw=true)  

### 历史版本和更新信息  

https://github.com/LuckyJayce/Task/releases

# Gradle导入

## 1.必须导入：

```
//TAsk核心类库
compile 'com.shizhefei:MVCHelper-Task-Core:1.4.0'
```

## 2.可选：

**【1】  Tasks ：Task操作符类库**
​    比如：

```
IAsyncTask<User> task = Tasks
        .create(new InitTokenTask())
        .concatWith(new GetUserTask()));
```

引入方式：

    //Task操作符类库
    compile 'com.shizhefei:MVCHelper-Tasks:1.4.0'

**【2】  TestCase ：Task测试用例类库**
​     用于直接单独测试写好的task，提供简化的界面执行操作

```
//Task操作符类库
compile 'com.shizhefei:MVCHelper-TestCase:1.4.0'
```

**【3】   mvchelper_okhttp：简化请求方便使用**

例如：

```
GetMethod method = new GetMethod("http://www.baidu.com");
method.addParam("name", "xx");
method.addParam("age", 1);
method.addHeader("sign", "HJKHUIGUI323H");
method.executeAsync(sender, new ResponseParser<User>() {
    @Override
    public User parse(Response response) throws Exception {
        if (response.isSuccessful()) {
            return new User();
        }
        throw new NetworkException(response);
    }
});
```

引入方式：

```
//Task操作符类库
compile 'com.shizhefei:MVCHelper-OkHttp:1.3.2'
```

## 结构

![image](raw/Task.png)  

**【1】 ITask ：同步Task，不需要自己开线程，execute的方法在后台执行可以执行超时任务**

```
public interface ITask<DATA> extends ISuperTask<DATA> {

    /**
     * 后台线程执行
     * @param progressSender 发送进度数据
     * @return 返回结果数据
     * @throws Exception 失败的时候通过异常抛出
     */
    DATA execute(ProgressSender progressSender) throws Exception;

    /**
     * 外面通过这个执行取消
     */
    void cancel();
}
```

例子：

```
public class DownloadSyncTask implements ITask<String> {
    private String url;
    private String filePath;
    private volatile boolean cancel;

    public DownloadSyncTask(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    @Override
    public String execute(ProgressSender progressSender) throws Exception {
      //这个函数是在后台线程调用执行，可以执行超时的任务
        int total = 500;
        for (int i = 0; i < total && !cancel; i++) {
            Thread.sleep(10);
            progressSender.sendProgress(i, total, “progress info”);
        }
    	//错误的情况通过自定义异常抛出， ICallback会接收到
    	//throw new NetWorkException("网络出错");
        return filePath;
    }

    @Override
    public void cancel() {
        cancel = true;
    }
}
```

**【2】IAsyncTask 异步Task，需要自己开线程异步操作，execute的方法在主线程执行，不能执行超时任务**

```
public interface IAsyncTask<DATA> extends ISuperTask<DATA> {

    /**
     * 主线程执行
     * @param sender 发送进度或者结果数据，回调ICallback的onProgress或者onPost
     * @return 返回给外面一个取消句柄，外面通过这个执行取消
     * @throws Exception
     */
    RequestHandle execute(ResponseSender<DATA> sender) throws Exception;

}
```

例如OkHttp异步请求：

```
public class GetDetail implements IAsyncTask<Book> {
    @Override
    public RequestHandle execute(final ResponseSender<Book> sender) throws Exception {
        Request request = new 	 Request.Builder().url("https://www.baidu.com").get().build();
        Call call = OkHttpUtils.client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //send 发送失败信息
                sender.sendError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //发送成功信息
                sender.sendData(new Book("Java编程思想"));
            }
        });
        return new OKHttpRequestHandle(call);
    }
}
```

**【3】IDataSource ， IAsyncDataSource 和ITask ， IAsyncTask 类似用于列表请求：**

​           使用详情参考：https://github.com/LuckyJayce/MVCHelper 项目

**【4】ICallback : 执行Task的回调**

```
/**
 * task回调
 * Created by LuckyJayce on 2016/7/17.
 */
public interface ICallback<DATA> {

    /**
     * 执行task之前的回调， 主线程
     */
   void onPreExecute(Object task);

    /**
     * 进度更新回调
     *
     * @param percent
     * @param current
     * @param total
     * @param extraData
     */
   void onProgress(Object task, int percent, long current, long total, Object extraData);

    /**
     * 执行完成的回调
     *
     * @param task      执行的task
     * @param code      判断：失败，异常，取消
     * @param exception 返回异常的时候数据
     * @param data 返回成功的数据
     */
   void onPostExecute(Object task, Code code, Exception exception, DATA data);
}
```

**【5】TaskHelper：执行task和设置Callback回调**

TaskHelper是连接task和Callback桥梁，执行task，task通过TaskHelper通知Callback数据和更新UI.

taskHelper可以同时执行多个task

例如：

```
//执行获取书本数据task
taskHelper.execute(new GetBookTask("Java编程思想"), new GetBookCallback()); 

//执行登录task, taskHandle可以对LoginTask进行单独取消
TaskHandle taskHandle = taskHelper.execute(new LoginTask(), new LoginCallback()); 

//根据task实例取消task
taskHelper.cancel(loginTask);

taskHelper.cancelAll();取消所有task
taskHelper.destroy();取消所有task，目前和cancelAll相同逻辑

//注册所有通过当前taskHelper执行task回调的callback事件
taskHelper.registerCallBack(new AllCallback());
//注销事件
taskHelper.unRegisterCallBack(allcallback);

```

