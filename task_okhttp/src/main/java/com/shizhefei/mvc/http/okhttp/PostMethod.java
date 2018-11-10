package com.shizhefei.mvc.http.okhttp;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


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
    protected void appendBody(Request.Builder requestBuilder) {
        Map<String, Object> bodyParams = getBodyParams();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (bodyParams != null) {
            for (Map.Entry<String, ?> entry : bodyParams.entrySet()) {
                bodyBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        requestBuilder.post(bodyBuilder.build());
    }
}
