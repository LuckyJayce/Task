package com.shizhefei.task.demo.models.task.download;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;

/**
 * 模拟自己开线程下载task
 */
public class DownloadAsyncTask implements IAsyncTask<String> {
    private String url;
    private String filePath;
    private volatile boolean cancel = false;

    public DownloadAsyncTask(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    @Override
    public RequestHandle execute(final ResponseSender<String> sender) throws Exception {
        new Thread() {
            @Override
            public void run() {
                int total = 200;
                for (int i = 0; i < total && !cancel; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sender.sendProgress(i, total, null);
                }
                if (!cancel) {
                    sender.sendData(filePath);
                }
            }
        }.start();
        return new RequestHandle() {
            @Override
            public void cancle() {
                cancel = true;
            }

            @Override
            public boolean isRunning() {
                return false;
            }
        };
    }
}
