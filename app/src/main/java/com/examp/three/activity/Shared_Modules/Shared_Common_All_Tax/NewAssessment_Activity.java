package com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import com.badoualy.stepperindicator.StepperIndicator;
import com.examp.three.R;
import com.examp.three.adapter.SharedAdapter.NewAssessment_PagerAdapter;
import com.examp.three.common.Common;


public class NewAssessment_Activity extends AppCompatActivity  {

    public static ViewPager pager;
    StepperIndicator indicator;
    NewAssessment_PagerAdapter adapter;

    String step1, step2, step3;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String prefername = "pager";
    Toolbar mtoolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_ass_stepper);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_new_assessment);

        preferences = getSharedPreferences(prefername, Context.MODE_PRIVATE);
        editor = preferences.edit();

        step1 = preferences.getString("assFrag1", "0");
        step2 = preferences.getString("assFrag2", "0");
        step3 = preferences.getString("assFrag3", "0");

        //ID's
        pager = (ViewPager) findViewById(R.id.new_ass_pager);
        indicator = (StepperIndicator) findViewById(R.id.new_ass_indicator);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        Intent b = this.getIntent();
        String type = b.getStringExtra(Common.Type);

        assert pager != null;

        if (type.equalsIgnoreCase("PR") || type.equalsIgnoreCase("W")) {
            adapter = new NewAssessment_PagerAdapter(getSupportFragmentManager(), 2, type);
            pager.setAdapter(adapter);
            indicator.setViewPager(pager);
        } else {
            adapter = new NewAssessment_PagerAdapter(getSupportFragmentManager(), 3, type);
            pager.setAdapter(adapter);
            indicator.setViewPager(pager);
        }
    }
}
