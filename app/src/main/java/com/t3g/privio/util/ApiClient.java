package com.t3g.privio.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.t3g.privio.Model.Contants.BASE_URL;

/**
 * Created by pBrowser on 05-03-2018.
 */

public class ApiClient {
    /**
     * Get Retrofit Instance
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
    public static ApiResponse getApiService() {
        return getRetrofitInstance().create(ApiResponse.class);
    }
}
