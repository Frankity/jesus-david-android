package com.jedga95.reddit.reddits.json;

import com.jedga95.reddit.reddits.json.model.RedditData;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by jesdga95 on 21/01/17.
 */

public class RedditItemAPI {

    public static final String HTTP_BASE = "https://";
    public static final String BASE_URL = "https://www.reddit.com";

    /**
     * Get retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }


    public interface ApiService {

        @GET("/reddits.json")
        Call<RedditData> getRedditItems();
    }
}
