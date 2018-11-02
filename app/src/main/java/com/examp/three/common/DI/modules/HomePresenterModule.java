package com.examp.three.common.DI.modules;


import com.examp.three.presenter.HomeactivtyContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HomePresenterModule {
    HomeactivtyContract.view view;

    public HomePresenterModule(HomeactivtyContract.view view) {
        this.view = view;
    }
    @Provides
    @Singleton
    HomeactivtyContract.view provideHomeContractView(){
        return view;
    }
}
