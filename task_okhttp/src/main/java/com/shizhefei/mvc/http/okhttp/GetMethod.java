package com.shizhefei.mvc.http.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetMethod extends HttpMethod<GetMethod> {
    public GetMethod() {

    }

    public GetMethod(String url) {
        super(url);
    }

    public GetMethod(OkHttpClient httpClient, String url) {
        super(httpClient, url);
    }

    @Override
    protected void appendBody(Request.Builder requestBuilder) {
        requestBuilder.get();
    }
}
