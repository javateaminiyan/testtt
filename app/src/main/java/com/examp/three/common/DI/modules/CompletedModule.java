package com.examp.three.common.DI.modules;


import com.examp.three.presenter.Grievances_admin.contracts.ICompletedInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CompletedModule {
    ICompletedInterface.view view;

    public CompletedModule(ICompletedInterface.view view) {
        this.view = view;
    }

    @Provides
    @Singleton
    ICompletedInterface.view provideCompletedView(){
        return view;
    }
}
