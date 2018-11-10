package com.shizhefei.mvc.http.okhttp;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

abstract class HttpWithBodyMethod<METHOD extends HttpWithBodyMethod> extends HttpMethod<METHOD> {
    private Map<String, Object> bodyParams = new HashMap<>();
    public HttpWithBodyMethod() {
    }

    public HttpWithBodyMethod(String url) {
        super(url);
    }

    public HttpWithBodyMethod(OkHttpClient httpClient, String url) {
        super(httpClient, url);
    }

    public METHOD addBodyParam(String key, int value) {
        bodyParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addBodyParam(String key, boolean value) {
        bodyParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addBodyParam(String key, double value) {
        bodyParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addBodyParam(String key, float value) {
        bodyParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addBodyParam(String key, long value) {
        bodyParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addBodyParam(String key, String value) {
        bodyParams.put(key, value);
        return (METHOD) this;
    }

    public METHOD addBodyParams(Map<String, ?> params) {
        bodyParams.putAll(params);
        return (METHOD) this;
    }

    public Map<String, Object> getBodyParams() {
        return bodyParams;
    }
}
