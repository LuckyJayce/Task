package com.shizhefei.mvc.http.okhttp;

import com.shizhefei.mvc.http.UrlBuilder;

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
    protected Request.Builder buildRequest() {
        String url = new UrlBuilder(getUrl()).params(getQueryParams()).build();
        return new Request.Builder().url(url).get();
    }
}
