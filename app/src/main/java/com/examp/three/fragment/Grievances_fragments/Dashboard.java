package com.examp.three.fragment.Grievances_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.chart.MyValueFormatter;
import com.examp.three.common.Common;
import com.examp.three.common.DI.components.DaggerDashboardComponent;
import com.examp.three.common.DI.modules.DashboardModule;
import com.examp.three.common.DI.modules.NetworkModule;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.Grievances_admin.DashboardPojo;
import com.examp.three.presenter.Grievances_admin.DashoboardPresenter;
import com.examp.three.presenter.Grievances_admin.contracts.IdashboardInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_district_department;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_panchayat_department;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_userid;

public class Dashboard extends Fragment implements IdashboardInterface.view {
@BindView(R.id.bar_chart)
BarChart bar_chart;

@BindView(R.id.tv_received_date)
TextView tv_received_date;
@BindView(R.id.tv_pending_date)
TextView tv_pending_date;

@BindView(R.id.tv_received_application_count)
TextView tv_received_application_count;
@BindView(R.id.tv_pending_application_count)
TextView tv_pending_application_count;
@BindView(R.id.tv_completed_application_count)
TextView tv_completed_application_count;

View dashBoardView;
@Inject
DashoboardPresenter dashoboardPresenter;
@Inject
Retrofit retrofit;

String mType="Abstract";
String mDistrict="";
String mPanchayat="";
String mGrievanceNo="";
    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashBoardView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        ButterKnife.bind(this,dashBoardView);
        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mDistrict = sharedPreference.getString(pref_login_district_department,null);
        mPanchayat = sharedPreference.getString(pref_login_panchayat_department,null);
        DaggerDashboardComponent.builder().networkModule(new NetworkModule("http://www.predemos.com")).dashboardModule(new DashboardModule(this)).build().inject(this);
        initBarChart();
        dashoboardPresenter.getDashboardData(retrofit,mType,mDistrict,mPanchayat,mGrievanceNo);
        tv_received_date.setText(dashoboardPresenter.getDate());
        return dashBoardView;
    }
    void initBarChart(){
        bar_chart.setDrawBarShadow(false);
        bar_chart.setDrawValueAboveBar(true);

        bar_chart.setMaxVisibleValueCount(50);
        bar_chart.setPinchZoom(false);
        bar_chart.setDrawGridBackground(false);
        bar_chart.animateXY(1000,1000);
        bar_chart.getAxisRight().setDrawGridLines(false);
        bar_chart.getAxisLeft().setDrawGridLines(false);
        bar_chart.getXAxis().setDrawGridLines(false);
        XAxis xAxis = bar_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bar_chart.getXAxis().setDrawAxisLine(true);

        bar_chart.setPinchZoom(false);
        bar_chart.setDoubleTapToZoomEnabled(false);

        YAxis rightAxis = bar_chart.getAxisRight();
        rightAxis.setEnabled(false);

        Description description = new Description();
        description.setEnabled(false);
        bar_chart.setDescription(description);    // Hide the description
        bar_chart.getAxisLeft().setDrawLabels(true);
        bar_chart.getAxisRight().setDrawLabels(false);
        bar_chart.getXAxis().setDrawLabels(false);

        bar_chart.getLegend().setEnabled(true);   // Hide the legend
    }

    @Override
    public void showResult(String msg) {
        Log.e("message=>",msg);
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setBarChartUsingActualDatas(BarData barData, DashboardPojo dashboardPojo) {
        barData.setValueFormatter(new MyValueFormatter());
        bar_chart.setData(barData);
        bar_chart.invalidate();
        if(dashboardPojo!=null){
            tv_received_application_count.setText(Integer.toString(dashboardPojo.getReceived())+" Applications");
            tv_pending_application_count.setText(Integer.toString(dashboardPojo.getProcess())+" Applications");
            tv_completed_application_count.setText(Integer.toString(dashboardPojo.getCompleted())+" Applications");
        }


    }
}
