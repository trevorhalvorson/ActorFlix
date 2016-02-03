package com.trevorhalvorson.actorflix;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by Trevor Halvorson on 1/1/2016.
 */
public class FlixService {

    private FlixService() {
    }

    public static FlixApi createFlixService() {
        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://netflixroulette.net");

        return builder.build().create(FlixApi.class);
    }
}
