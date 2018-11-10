package com.shizhefei.mvc.http.okhttp;

import com.shizhefei.mvc.http.UrlBuilder;

import java.util.Map;
import java.util.Map.Entry;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class PostMethod extends HttpWithBodyMethod<PostMethod> {
    public PostMethod() {
    }

    public PostMethod(String url) {
        super(url);
    }

    public PostMethod(OkHttpClient httpClient, String url) {
        super(httpClient, url);
    }

    @Override
    protected Request.Builder buildRequest() {
        Map<String, Object> queryParams = getQueryParams();
        Map<String, Object> bodyParams = getBodyParams();
        String url = new UrlBuilder(getUrl()).params(queryParams).build();
        FormBody.Builder builder = new FormBody.Builder();
        if (bodyParams != null) {
            for (Entry<String, ?> entry : bodyParams.entrySet()) {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        RequestBody formBody = builder.build();
        return new Request.Builder().url(url).post(formBody);
    }
}
