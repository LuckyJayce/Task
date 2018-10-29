package com.shizhefei.task.demo.models.task.download;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;

/**
 * 模拟下载task
 */
public class DownloadAsyncTask implements IAsyncTask<String> {
    private String url;
    private String filePath;

    public DownloadAsyncTask(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    @Override
    public RequestHandle execute(ResponseSender<String> sender) throws Exception {
        int total = 500;
        for (int i = 0; i < total; i++) {
            sender.sendProgress(i, total, null);
        }
        sender.sendData(filePath);
        return null;
    }
}
