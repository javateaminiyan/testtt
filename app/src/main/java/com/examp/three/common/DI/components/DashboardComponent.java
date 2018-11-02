package com.examp.three.common.DI.components;



import com.examp.three.common.DI.modules.DashboardModule;
import com.examp.three.common.DI.modules.NetworkModule;
import com.examp.three.fragment.Grievances_fragments.Dashboard;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {DashboardModule.class,NetworkModule.class})
@Singleton
public interface DashboardComponent {
    void inject(Dashboard dashboard);
}
