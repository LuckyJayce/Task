package com.shizhefei.mvc.http.okhttp;


import com.shizhefei.mvc.http.UrlBuilder;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class DeleteMethod extends HttpWithBodyMethod<DeleteMethod> {
    public DeleteMethod() {
    }

    public DeleteMethod(String url) {
        super(url);
    }

    public DeleteMethod(OkHttpClient httpClient, String url) {
        super(httpClient, url);
    }

    @Override
    protected Request.Builder buildRequest() {
        Map<String, Object> bodyParams = getBodyParams();
        Map<String, Object> queryParams = getQueryParams();
        String url = new UrlBuilder(getUrl()).params(queryParams).build();
        FormBody.Builder builder = new FormBody.Builder();
        if (bodyParams != null) {
            for (Map.Entry<String, ?> entry : bodyParams.entrySet()) {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        RequestBody formBody = builder.build();
        return new Request.Builder().url(url).put(formBody);
    }
}
