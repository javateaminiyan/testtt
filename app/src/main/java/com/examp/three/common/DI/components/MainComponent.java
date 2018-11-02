package com.examp.three.common.DI.components;

import com.examp.three.activity.Grievance_admin.GrievanceAdminActivity;
import com.examp.three.common.DI.modules.ReceivedModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ReceivedModule.class})
@Singleton
public interface MainComponent {
    void inject(GrievanceAdminActivity mainActivity);
}
