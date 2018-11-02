package com.examp.three.common.DI.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.examp.three.common.SharedPrefManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class SharedPrefModule {
    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(@Named("Application")Application application){
        return  PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    SharedPrefManager provideSharedPrefManager(){
        return new SharedPrefManager();
    }
}
