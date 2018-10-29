package com.shizhefei.task.demo.models.task.upload;

import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.task.ITask;
import com.shizhefei.task.demo.models.entities.ImageInfo;

/**
 * 上传图片返回上传图片后的图片信息包含url地址
 */
public class UploadImageTask implements ITask<ImageInfo> {
    private final String compressImagePath;

    public UploadImageTask(String compressImagePath) {
        this.compressImagePath = compressImagePath;
    }

    @Override
    public ImageInfo execute(ProgressSender progressSender) throws Exception {
        String url = "";
        int width = 400;
        int height = 400;
        return new ImageInfo(url, width, height);
    }

    @Override
    public void cancel() {

    }
}
