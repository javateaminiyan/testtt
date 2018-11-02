package com.examp.three.adapter.Building;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.examp.three.fragment.Building.BuildingLicenceFirstFragment;
import com.examp.three.fragment.Building.BuildingLicenseSecondFragment;
import com.examp.three.fragment.Building.BuildingLicenseThirdFragment;

/**
 * Created by priyadharshini on 31/07/2018.
 */


public class BuildingPlanNewApplication_PagerAdapter extends FragmentPagerAdapter {
    private int count;

    public BuildingPlanNewApplication_PagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new BuildingLicenceFirstFragment();

            case 1:
                return new BuildingLicenseSecondFragment();

            case 2:
                return new BuildingLicenseThirdFragment();


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
