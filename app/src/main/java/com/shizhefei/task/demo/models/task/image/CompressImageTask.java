package com.shizhefei.task.demo.models.task.image;

import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.task.ITask;

/**
 * 通过imagePath压缩图片，返回压缩后的图片文件路径
 */
public class CompressImageTask implements ITask<String> {
    private String imagePath;

    public CompressImageTask(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String execute(ProgressSender progressSender) throws Exception {
        String newPath = "new" + imagePath;
        //返回压缩后的文件路径
        return newPath;
    }

    @Override
    public void cancel() {

    }
}
