package com.hck.imagemap.svg;

import android.graphics.Picture;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.UnsupportedEncodingException;
public class FlieRequest extends Request<Picture>
{

    private final Callback mCallback;

    public FlieRequest(String url, Callback callback)
    {
        this(Method.DEPRECATED_GET_OR_POST, url, callback);
    }

    public FlieRequest(int method, String url, Callback callback)
    {
        super(method, url, callback);
        mCallback = callback;
    }

    public static class Callback implements Response.ErrorListener,
            Response.Listener<Picture>
    {

        @Override
        public void onResponse(Picture response)
        {

        }
        
        @Override
        public void onErrorResponse(VolleyError error)
        {

        }
    }

    @Override
    protected void deliverResponse(Picture data)
    {
        if (mCallback != null)
        {
            mCallback.onResponse(data);
        }
    }

    @Override
    protected Response<Picture> parseNetworkResponse(NetworkResponse response)
    {
        if (response == null)
            return Response.error(new VolleyError(" NetworkResponse is null"));
        byte[] bytes = response.data;
        try
        {
            String result = new String(bytes, "ISO-8859-1");
            System.out.println("result -= " + result);
            Cache.Entry entry = new Cache.Entry();
            entry.data = bytes;
            SVG svg = SVGHelper.createSVGFromData(result);
            Picture data = svg.renderToPicture();
            return Response.success(data, entry);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        } catch (SVGParseException e)
        {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        }
    }
}
