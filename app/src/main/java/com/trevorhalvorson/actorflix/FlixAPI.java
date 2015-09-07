package com.trevorhalvorson.actorflix;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Trevor on 9/6/2015.
 */
public interface FlixAPI {

    @GET("/api/api.php")
    void getProductions(@Query("actor") String actor,
                        Callback<ArrayList<Production>> response);
}

