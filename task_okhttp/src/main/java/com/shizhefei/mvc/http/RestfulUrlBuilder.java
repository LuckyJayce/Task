package com.shizhefei.mvc.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class RestfulUrlBuilder {
    private String url;
    private LinkedList<String> paths = new LinkedList<String>();
    private LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();

    public RestfulUrlBuilder(String url) {
        super();
        this.url = url;
    }

    public RestfulUrlBuilder sp(Object path) {
        paths.add(String.valueOf(path));
        return this;
    }

    public RestfulUrlBuilder param(String param, String value) {
        params.put(param, value);
        return this;
    }

    public RestfulUrlBuilder param(String param, int value) {
        params.put(param, value);
        return this;
    }

    public RestfulUrlBuilder param(String param, boolean value) {
        params.put(param, value);
        return this;
    }

    public RestfulUrlBuilder param(String param, double value) {
        params.put(param, value);
        return this;
    }

    public RestfulUrlBuilder param(String param, float value) {
        params.put(param, value);
        return this;
    }

    public RestfulUrlBuilder param(String param, byte value) {
        params.put(param, value);
        return this;
    }

    public RestfulUrlBuilder param(String param, long value) {
        params.put(param, value);
        return this;
    }

    public RestfulUrlBuilder params(Map<String, Object> p) {
        params.putAll(p);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(url);
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '/') {
            builder.deleteCharAt(builder.length() - 1);
        }
        for (String path : paths) {
            builder.append("/").append(path);
        }
        String url = builder.toString();
        if (!params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                try {
                    String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
                    url = url.replace("{" + entry.getKey() + "}", value);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return url;
    }
}
