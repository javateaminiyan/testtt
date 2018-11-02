package com.examp.three.common.DI.modules;

import android.app.Application;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Named("Application")
    Application provideApplication(){
        return  application;
    }

}
