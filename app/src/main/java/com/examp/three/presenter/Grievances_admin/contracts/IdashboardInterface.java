package com.examp.three.presenter.Grievances_admin.contracts;

import com.examp.three.model.Grievances_admin.DashboardPojo;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;

import java.util.List;

import retrofit2.Retrofit;

public interface IdashboardInterface {
    interface view{
        void showResult(String msg);
        void setBarChartUsingActualDatas(BarData barData, DashboardPojo dashboardPojo);
    }
    interface presenter{
        PieData getDataForPieChart();
        void getDataForBarChart(List<DashboardPojo> dashboardPojoList);
        void getDashboardData(Retrofit retrofit, String type, String district, String panchayat, String grievanceNo);
        String getDate();
    }
    interface model{

    }
}
