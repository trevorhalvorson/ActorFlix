package com.trevorhalvorson.actorflix;


import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Trevor on 8/20/2015.
 */
public interface Api {

    @GET("/api/api.php")
    void getProductions(@Query("actor") String actor,
                        Callback<ArrayList<Production>> response);
}
