package com.examp.three.common.DI.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
   String baseUrl;

    public NetworkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    @Provides
    Gson provideGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return  gsonBuilder.create();
    }

    @Provides
    Retrofit provideRetrofit(){
        OkHttpClient okHttpClient = getOkHttpCleint(getHttpLoggingInterceptor());
        return  new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }
    @Provides
    public static OkHttpClient getOkHttpCleint(HttpLoggingInterceptor httpLoggingInterceptor) {

        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }
    @Provides
    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

}
