package com.examp.three.adapter.TradeLicence;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.examp.three.fragment.Trade_NewAssessment.LicenseRegistration_First_Fragment;
import com.examp.three.fragment.Trade_NewAssessment.LicenseRegistration_Second_Fragment;
import com.examp.three.fragment.Trade_NewAssessment.LicenseRegistration_Third_Fragment;
import com.examp.three.fragment.Trade_NewAssessment.LicenseRegistration_fourth_Fragment;
/**
 * Created by priyadharshini on 31/07/2018.
 */

public class LicenseRegistration_PagerAdapter extends FragmentPagerAdapter {
    private int count;

    public LicenseRegistration_PagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new LicenseRegistration_First_Fragment();

            case 1:
                return new LicenseRegistration_Second_Fragment();

            case 2:
                return new LicenseRegistration_Third_Fragment();

            case 3:
                return new LicenseRegistration_fourth_Fragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
