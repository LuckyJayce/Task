package com.shizhefei.task.demo.models.task.download;

import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.task.ITask;

/**
 * 模拟下载task
 */
public class DownloadSyncTask implements ITask<String> {
    private String url;
    private String filePath;

    public DownloadSyncTask(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    @Override
    public String execute(ProgressSender progressSender) throws Exception {
        int total = 500;
        for (int i = 0; i < total; i++) {
            Thread.sleep(10);
            progressSender.sendProgress(i, total, null);
        }
        return filePath;
    }

    @Override
    public void cancel() {

    }
}
