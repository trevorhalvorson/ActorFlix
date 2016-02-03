package com.trevorhalvorson.actorflix;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Trevor Halvorson on 2/2/2016.
 */
public interface FlixApi {

    /**
     * See http://netflixroulette.net/api/api.php?actor=Harrison%20Ford
     */
    @GET("/api/api.php?")
    Observable<List<Production>> listProductions(@Query("actor") String actor);
}
