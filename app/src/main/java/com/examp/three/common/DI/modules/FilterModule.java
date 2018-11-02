package com.examp.three.common.DI.modules;


import com.examp.three.presenter.Grievances_admin.FilterClass;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FilterModule {
    public FilterModule() {
    }
    @Provides
    @Singleton
    FilterClass provideFilterClassObject(){
        return  new FilterClass();
    }
}
