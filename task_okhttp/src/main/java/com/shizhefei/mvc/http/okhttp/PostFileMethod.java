package com.shizhefei.mvc.http.okhttp;

import android.util.Pair;

import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.mvc.http.MimeUtils;
import com.shizhefei.mvc.http.UrlBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostFileMethod extends PostMethod {
    public PostFileMethod() {
    }

    private Map<String, Pair<String, RequestBody>> httpbodys = new HashMap<String, Pair<String, RequestBody>>();
    private CountingRequestBody.Listener listener;

    public PostFileMethod(String url) {
        super(url);
    }

    public PostFileMethod(OkHttpClient httpClient, String url) {
        super(httpClient, url);
    }

    @Override
    protected Request.Builder buildRequest() {
        String url = new UrlBuilder(getUrl()).params(getQueryParams()).build();
        Map<String, Object> bodyParams = getBodyParams();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (bodyParams != null) {
            for (Map.Entry<String, ?> entry : bodyParams.entrySet()) {
                builder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        if (httpbodys != null) {
            for (Map.Entry<String, Pair<String, RequestBody>> entry : httpbodys.entrySet()) {
                String key = entry.getKey();
                Pair<String, RequestBody> body = entry.getValue();
                builder.addFormDataPart(key, body.first, body.second);
            }
        }
        RequestBody formBody = builder.build();
        if (listener != null) {
            formBody = new CountingRequestBody(formBody, listener);
        }
        return new Request.Builder().url(url).post(formBody);
    }

    public PostFileMethod addBodyParam(String key, String fileName, RequestBody requestBody) {
        httpbodys.put(key, new Pair<>(fileName, requestBody));
        return this;
    }

    public PostFileMethod addBodyParam(String key, String fileName, File file) {
        String mime = MimeUtils.getFileMimeType(file);
        MediaType mediaType = MediaType.parse(mime);
        return addBodyParam(key, fileName, RequestBody.create(mediaType, file));
    }

    public PostFileMethod addBodyParam(String key, File file) {
        return addBodyParam(key, file.getName(), file);
    }

    public void setProgressListener(CountingRequestBody.Listener listener) {
        this.listener = listener;
    }

    public void setProgressListener(final ProgressSender progressSender) {
        this.listener = new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength) {
                progressSender.sendProgress(bytesWritten, contentLength, null);
            }
        };
    }

}
