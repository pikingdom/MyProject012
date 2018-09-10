package com.tsy.sdk.myokhttp.builder;

import com.tsy.sdk.myokhttp.Common;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.callback.MyCallback;
import com.tsy.sdk.myokhttp.response.IResponseHandler;
import com.tsy.sdk.myokhttp.util.LogUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Get Builder
 * Created by tsy on 16/9/18.
 */
public class GetBuilder extends OkHttpRequestBuilderHasParam<GetBuilder> {
    private boolean addCommonHead = false;

    public GetBuilder(MyOkHttp myOkHttp, boolean addHead) {
        super(myOkHttp);
        this.addCommonHead = addHead;
    }

    @Override
    public void enqueue(final IResponseHandler responseHandler) {
        try {
            Request request = getRequest();

            mMyOkHttp.getOkHttpClient().
                    newCall(request).
                    enqueue(new MyCallback(responseHandler));
        } catch (Exception e) {
            LogUtils.e("Get enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    private Request getRequest() {
        if(mUrl == null || mUrl.length() == 0) {
            throw new IllegalArgumentException("url can not be null !");
        }

        if (mParams != null && mParams.size() > 0) {
            mUrl = appendParams(mUrl, mParams);
        }

        Request.Builder builder = new Request.Builder().url(mUrl).get();
        appendHeaders(builder, mHeaders);

        if (mTag != null) {
            builder.tag(mTag);
        }
        if(addCommonHead){
            Common.addCommonHeader(builder,"");
        }
        return builder.build();
    }

    @Override
    public Response execute() {
        try {
            Request request = getRequest();

            return mMyOkHttp.getOkHttpClient().
                    newCall(request).execute();
        } catch (Exception e) {
            LogUtils.e("Get enqueue error:" + e.getMessage());
            return null;
        }
    }

    public String executeStr() {
        Response response = execute();
        if(response != null && response.isSuccessful() && response.body() != null){
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    //append params to url
    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
