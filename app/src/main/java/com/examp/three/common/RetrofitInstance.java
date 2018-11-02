package com.examp.three.common;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 8/3/2018.
 */

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static Retrofit retrofitPaypre;
    private static Retrofit retrofitEtown;
   static OkHttpClient okHttpClient =  okHttpClient = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS)
           .readTimeout(60, TimeUnit.SECONDS)
           .writeTimeout(60, TimeUnit.SECONDS)
           .build();

    public static Retrofit getRetrofit(){

            if(retrofit == null){
                retrofit = new Retrofit.Builder().baseUrl(Common.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
            }

        return  retrofit;
    }

    public static Retrofit getRetrofitEtown() {

        if (retrofitEtown == null) {
            retrofitEtown = new Retrofit.Builder().baseUrl(Common.baseUrlETown)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofitEtown;
    }

    public static Retrofit getRetrofitPaypre() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(Common.baseUrlPaypre)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }


}
