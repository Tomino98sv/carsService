package com.globalsovy.carserviceapp;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

public class InputStreamVolleyRequest extends Request<byte[]> {

    private Response.Listener<byte[]> mListener;
    public Map<String, String> responseHeaders ;

    public InputStreamVolleyRequest(int method,String url,Response.Listener<byte[]> mListener,Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        setShouldCache(false);
        this.mListener = mListener;
    }


    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        responseHeaders = response.headers;
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }
}
