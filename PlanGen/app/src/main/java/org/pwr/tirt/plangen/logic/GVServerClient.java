package org.pwr.tirt.plangen.logic;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class GVServerClient {
    //private static final String SERVER_URL = "http://planpwr.unicloud.pl/schedule/";
    private static final String SERVER_URL = "https://google.com";

    public static void connectToServer(Context context, final ITaskListener listener, String indexNumber) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String link = SERVER_URL + indexNumber;
        /*JsonArrayRequest getRequest = new JsonArrayRequest(link,
                new Response.Listener<JSONArray>() {
                    @Override public void onResponse(JSONArray response) {
                        listener.dataDownloaded(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override public void onErrorResponse(VolleyError error) {
                        listener.downloadingFailed();
                    }
                }
        );*/
        StringRequest getRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.dataDownloaded(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.downloadingFailed();
            }
        });
        queue.add(getRequest);
    }
}
