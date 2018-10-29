package com.shizhefei.task.demo.models.task.register;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ProxyTask;
import com.shizhefei.task.demo.models.entities.ImageInfo;
import com.shizhefei.task.demo.models.entities.User;
import com.shizhefei.task.demo.models.task.image.CompressImageTask;
import com.shizhefei.task.demo.models.task.upload.UploadImageTask;
import com.shizhefei.task.function.Func1;
import com.shizhefei.task.tasks.Tasks;

/**
 * 注册task
 * 1.压缩图片
 * 2.上传图片到七牛云
 * 3.册信息提交给服务器返回注册用户的信息
 */
public class RegisterTask extends ProxyTask<User> {
    private String name;
    private int age;
    private String password;
    private String imagePath;

    public RegisterTask(String name, int age, String password, String imagePath) {
        this.name = name;
        this.age = age;
        this.password = password;
        this.imagePath = imagePath;
    }

    @Override
    protected IAsyncTask<User> getTask() {
        return Tasks.create(new CompressImageTask(imagePath))//压缩图片返回压缩图片后的文件路径
                .concatMap(new Func1<String, IAsyncTask<ImageInfo>>() {//上传图片到七牛云返回七牛云的url
                    @Override
                    public IAsyncTask<ImageInfo> call(String compressImagePath) throws Exception {
                        return Tasks.async(new UploadImageTask(compressImagePath));
                    }
                }).concatMap(new Func1<ImageInfo, IAsyncTask<User>>() {//将注册信息提交给服务器返回注册用户的信息给界面
                    @Override
                    public IAsyncTask<User> call(ImageInfo data) throws Exception {
                        return new RegisterServerTask(data.getUrl(), name, age);
                    }
                });
    }

    //将注册信息提交给服务器
    private static class RegisterServerTask implements IAsyncTask<User> {
        private String imageUrl;
        private String name;
        private int age;

        public RegisterServerTask(String imageUrl, String name, int age) {
            this.imageUrl = imageUrl;
            this.name = name;
            this.age = age;
        }

        @Override
        public RequestHandle execute(ResponseSender<User> sender) throws Exception {
            sender.sendData(new User(name, age));
            return null;
        }
    }
}
