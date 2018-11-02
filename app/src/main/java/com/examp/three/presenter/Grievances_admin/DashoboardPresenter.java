package com.examp.three.presenter.Grievances_admin;

import android.graphics.Color;
import android.util.Log;

import com.examp.three.common.RetrofitInterface;
import com.examp.three.model.Grievances_admin.DashboardPojo;
import com.examp.three.presenter.Grievances_admin.contracts.IdashboardInterface;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashoboardPresenter implements IdashboardInterface.presenter {
    IdashboardInterface.view view;
    @Inject
    public DashoboardPresenter(IdashboardInterface.view view) {
        this.view = view;
    }

    String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    @Override
    public PieData getDataForPieChart() {
        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(34f,"Received"));
        yValues.add(new PieEntry(23f,"Pending"));
        yValues.add(new PieEntry(14f,"Cancelled"));

        PieDataSet dataset = new PieDataSet(yValues,"Countries");
        dataset.setSliceSpace(3f);
        dataset.setSelectionShift(5f);
        dataset.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData((dataset));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        return data;
    }

    @Override
    public void getDataForBarChart(List<DashboardPojo> dashboardPojoList) {
        BarData data1 = null;
        if(dashboardPojoList.size()>0){
            for(int i=0;i<dashboardPojoList.size();i++){
                //1st
                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(1,(float)dashboardPojoList.get(i).getReceived()));

                BarDataSet barDataSetReceived = new BarDataSet(barEntries,"Received");
                barDataSetReceived.setColors(new int[]{Color.parseColor("#6600ff")});


                ArrayList<BarEntry> barEntries1 = new ArrayList<>();

                barEntries1.add(new BarEntry(2,(float)dashboardPojoList.get(i).getProcess()));

                BarDataSet barDataSetPending = new BarDataSet(barEntries1,"Pending");
                barDataSetPending.setColors(new int[]{Color.parseColor("#0099ff")});

                ArrayList<BarEntry> barEntries2 = new ArrayList<>();

                barEntries2.add(new BarEntry(3,(float)dashboardPojoList.get(i).getCompleted()));

                BarDataSet barDataSetCompleted = new BarDataSet(barEntries2,"Completed");
                barDataSetCompleted.setColors(new int[]{Color.parseColor("#009933")});

                data1 =new BarData(barDataSetReceived,barDataSetPending,barDataSetCompleted);
                data1.setBarWidth(0.5f);

                view.setBarChartUsingActualDatas(data1,dashboardPojoList.get(i));
            }
        }

    }
    @Override
    public void getDashboardData(Retrofit retrofit, String type, String district, String panchayat, String grievanceNo) {
        final List<DashboardPojo> dashboardList = new ArrayList<>();
        Call<List<DashboardPojo>> getDashBoardPojo = retrofit.create(RetrofitInterface.class).getDashboardDatas("eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94",
                type,district, panchayat,grievanceNo);
        Log.e("url=>",getDashBoardPojo.request().toString()+"fbgdc");
        getDashBoardPojo.enqueue(new Callback<List<DashboardPojo>>() {
            @Override
            public void onResponse(Call<List<DashboardPojo>> call, Response<List<DashboardPojo>> response) {
                dashboardList.addAll(response.body());
                getDataForBarChart(dashboardList);
            }

            @Override
            public void onFailure(Call<List<DashboardPojo>> call, Throwable t) {
                view.showResult(t.getMessage());
            }
        });

    }

    @Override
    public String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String actualdate = simpleDateFormat.format(date);
        //24/10/2018 12:15:51
        String[] ac_array = actualdate.split(" ");
        String ac_date_array[] = ac_array[0].split("/");
        String dateString = ac_date_array[0];
        String monthString = months[Integer.parseInt(ac_date_array[1])-1];
        String yearString = ac_date_array[2];

        return monthString+" "+dateString+", "+yearString;
    }


}
