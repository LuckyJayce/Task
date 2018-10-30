package com.shizhefei.task.demo.models.task.home;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.demo.models.entities.HomeConfigData;

public class GetHomeConfigDataTask implements IAsyncTask<HomeConfigData> {
    @Override
    public RequestHandle execute(ResponseSender<HomeConfigData> sender) throws Exception {
        sender.sendData(new HomeConfigData());
        return null;
    }
}
