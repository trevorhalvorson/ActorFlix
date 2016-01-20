package com.trevorhalvorson.actorflix;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Trevor Halvorson on 1/1/2016.
 */
public interface FlixService {
    @GET("/api/api.php?")
    Call<List<Production>> listProductions(@Query("actor") String actor);
}
