package com.shizhefei.mvc.http.okhttp;

import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.mvc.http.AbsHttpMethod;
import com.shizhefei.mvc.http.RestfulUrlBuilder;
import com.shizhefei.mvc.http.UrlBuilder;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class HttpMethod<METHOD extends HttpMethod> extends AbsHttpMethod<METHOD, Callback> {
    private static OkHttpClient defaultClient = new OkHttpClient();
    private Call call;
    private OkHttpClient client;
    private boolean isRestful;

    public HttpMethod() {

    }

    public HttpMethod(String url) {
        this(defaultClient, url);
    }

    public HttpMethod(OkHttpClient httpClient, String url) {
        super(url);
        this.client = httpClient;
    }

    public static void setDefaultOkHttpClient(OkHttpClient okHttpClient) {
        defaultClient = okHttpClient;
    }

    public static OkHttpClient getDefaultOkHttpClient() {
        return defaultClient;
    }

    public HttpMethod setOkHttpClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public boolean isRestful() {
        return isRestful;
    }

    public METHOD setRestful(boolean restful) {
        isRestful = restful;
        return (METHOD) this;
    }

    protected void appendQueryParams(Request.Builder requestBuilder) {
        String url;
        if (isRestful()) {
            url = new RestfulUrlBuilder(getUrl()).params(getQueryParams()).build();
        } else {
            url = new UrlBuilder(getUrl()).params(getQueryParams()).build();
        }
        requestBuilder.url(url);
    }

    protected void appendHeader(Request.Builder requestBuilder) {
        Map<String, String> headers = getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }
    }

    protected void appendBody(Request.Builder requestBuilder) {

    }

    /**
     * 执行异步请求，执行的结果会自动调用sender的sender.sendData,执行失败或者出现异常会调用sender.sendError
     *
     * @param sender         mvchelper的datasource的ResponseSender
     * @param responseParser 数据解析器
     * @param <DATA>
     */
    public final <DATA> void executeAsync(ResponseSender<DATA> sender, final ResponseParser<DATA> responseParser) {
        executeAsync(new CallBackParser<DATA>(sender, responseParser));
    }

    /**
     * 执行异步请求,callback回调请求结果
     *
     * @param callback 请求的回调
     */
    @Override
    public final void executeAsync(Callback callback) {
        Request request = buildHttpRequest();
        call = client.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 执行同步请求,解析Response
     *
     * @param responseParser 数据解析器
     * @param <DATA>
     * @return 返回请求后解析得到的数据
     * @throws Exception
     */
    public final <DATA> DATA executeSync(ResponseParser<DATA> responseParser) throws Exception {
        return responseParser.parse(executeSync());
    }

    /**
     * 执行同步请求
     *
     * @return 返回请求回来的 Response
     * @throws Exception
     */
    public final Response executeSync() throws Exception {
        Request request = buildHttpRequest();
        call = client.newCall(request);
        return call.execute();
    }


    private Request buildHttpRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        appendQueryParams(requestBuilder);
        appendHeader(requestBuilder);
        appendBody(requestBuilder);
        return requestBuilder.build();
    }

    @Override
    public void cancle() {
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public boolean isRunning() {
        return call != null && !call.isExecuted();
    }
}
