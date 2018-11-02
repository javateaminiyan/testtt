package com.examp.three.common.DI.components;


import com.examp.three.HomeActivity;
import com.examp.three.common.DI.modules.AppModule;
import com.examp.three.common.DI.modules.HomePresenterModule;
import com.examp.three.common.DI.modules.SharedPrefModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class,SharedPrefModule.class,HomePresenterModule.class})
@Singleton
public interface IconInfoComponents {
   void inject(HomeActivity mainActivity);
}
