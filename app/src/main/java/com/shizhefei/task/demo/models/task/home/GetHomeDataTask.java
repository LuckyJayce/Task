package com.shizhefei.task.demo.models.task.home;

import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ProxyTask;
import com.shizhefei.task.demo.models.entities.HomeAdData;
import com.shizhefei.task.demo.models.entities.HomeConfigData;
import com.shizhefei.task.demo.models.entities.HomeData;
import com.shizhefei.task.demo.models.entities.HomeSaleData;
import com.shizhefei.task.function.Func1;
import com.shizhefei.task.function.Func2;
import com.shizhefei.task.tasks.Tasks;

public class GetHomeDataTask extends ProxyTask<HomeData> {

    @Override
    protected IAsyncTask<HomeData> getTask() {
        return Tasks.create(new GetHomeConfigDataTask()).delay(10 * 1000)
                .concatMap(new Func1<HomeConfigData, IAsyncTask<HomeData>>() {
                    @Override
                    public IAsyncTask<HomeData> call(HomeConfigData data) throws Exception {
                        return Tasks.combine(new GetHomeAdDataTask(), new GetHomeSaleTask(), new Func2<HomeAdData, HomeSaleData, HomeData>() {
                            @Override
                            public HomeData call(HomeAdData homeAdData, HomeSaleData homeSaleData) throws Exception {
                                return new HomeData(homeAdData, homeSaleData);
                            }
                        });
                    }
                }).retry(3).timeout(30 * 1000);
    }
}
