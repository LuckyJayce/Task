package com.shizhefei.mvc.http;

import com.shizhefei.mvc.RequestHandle;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsHttpMethod<METHOD extends AbsHttpMethod, CALLBACK> implements RequestHandle {

    private String url;

    public AbsHttpMethod() {
    }

    public AbsHttpMethod(String url) {
        this.url = url;
    }

    public abstract void executeAsync(CALLBACK callback);

    private Map<String, Object> queryParams = new HashMap<String, Object>();
    private Map<String, Object> pathParams = new HashMap<String, Object>();
    private Map<String, String> headers = new HashMap<String, String>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public Map<String, Object> getPathParams() {
        return pathParams;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public METHOD addHeader(String key, String value) {
        this.headers.put(key, value);
        return (METHOD) this;
    }

    public METHOD addPathParam(String key, int value) {
        pathParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addPathParam(String key, boolean value) {
        pathParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addPathParam(String key, double value) {
        pathParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addPathParam(String key, float value) {
        pathParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addPathParam(String key, long value) {
        pathParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addPathParam(String key, String value) {
        pathParams.put(key, value);
        return (METHOD) this;
    }

    public METHOD addPathParams(Map<String, ?> params) {
        pathParams.putAll(params);
        return (METHOD) this;
    }

    public METHOD addQueryParam(String key, int value) {
        queryParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addQueryParam(String key, boolean value) {
        queryParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addQueryParam(String key, double value) {
        queryParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addQueryParam(String key, float value) {
        queryParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addQueryParam(String key, long value) {
        queryParams.put(key, String.valueOf(value));
        return (METHOD) this;
    }

    public METHOD addQueryParam(String key, String value) {
        queryParams.put(key, value);
        return (METHOD) this;
    }

    public METHOD addQueryParams(Map<String, ?> params) {
        queryParams.putAll(params);
        return (METHOD) this;
    }
}
