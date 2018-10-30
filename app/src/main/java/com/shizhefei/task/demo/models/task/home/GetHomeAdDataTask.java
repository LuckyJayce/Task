package com.shizhefei.task.demo.models.task.home;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.demo.models.entities.HomeAdData;

public class GetHomeAdDataTask implements IAsyncTask<HomeAdData> {
    @Override
    public RequestHandle execute(ResponseSender<HomeAdData> sender) throws Exception {
        sender.sendData(new HomeAdData());
        return null;
    }
}
