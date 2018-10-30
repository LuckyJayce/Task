package com.shizhefei.task.demo.models.task.home;

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.demo.models.entities.HomeSaleData;

public class GetHomeSaleTask implements IAsyncTask<HomeSaleData> {
    @Override
    public RequestHandle execute(ResponseSender<HomeSaleData> sender) throws Exception {
        sender.sendData(new HomeSaleData());
        return null;
    }
}
