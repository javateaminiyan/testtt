package com.examp.three.presenter.Grievances_admin;

import android.util.Log;


import com.examp.three.common.CommanDatas;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.model.Grievances_admin.GrievanceData;
import com.examp.three.model.Grievances_admin.ReceivedUpdatePojo;
import com.examp.three.model.Grievances_admin.ReceivedUpdateResult;
import com.examp.three.presenter.Grievances_admin.contracts.IPendingInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PendingPresenter implements IPendingInterface.presenter {
    IPendingInterface.view view;
    @Inject
    public PendingPresenter(IPendingInterface.view view) {
        this.view = view;
    }




    @Override
    public ArrayList<String> getDesignationList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Executive Officer");
        arrayList.add("Head Clerk");
        arrayList.add("Jr Assistent");
        arrayList.add("Bill Collectors");
        arrayList.add("Record Clerk");
        arrayList.add("Office Assistense");
        arrayList.add("Computer Operator");
        arrayList.add("Others");
        return arrayList;
    }

    @Override
    public List<GrievanceData> getGdata(Retrofit retrofit, String type, String district, String panchayat, String grievanceNo) {
        final List<GrievanceData> grievanceData = new ArrayList<>();
        Call<List<GrievanceData>> mydatas = retrofit.create(RetrofitInterface.class).getGrievanceDatas("eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94",type,district,panchayat,"");
        mydatas.enqueue(new Callback<List<GrievanceData>>() {
            @Override
            public void onResponse(Call<List<GrievanceData>> call, Response<List<GrievanceData>> response) {
                grievanceData.addAll(response.body());
                CommanDatas.sPendingList.addAll(grievanceData);
//                Log.e("xcbh=>",response.body().get(0).getComplainerName());
                view.setRecyclerViewWithitsData(grievanceData);
            }

            @Override
            public void onFailure(Call<List<GrievanceData>> call, Throwable t) {
                Log.e("dsfv=>",t.getMessage());
            }
        });
        return grievanceData;
    }

    @Override
    public List<ReceivedUpdatePojo> getReceivedPojo(Retrofit retrofit, String type, String district, String panchayat, String grievanceNo) {
        final List<ReceivedUpdatePojo> grievanceDataReceived = new ArrayList<>();
        Call<List<ReceivedUpdatePojo>> mydatas = retrofit.create(RetrofitInterface.class).getGrievanceReceivedDatas("eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94",type,district,panchayat,grievanceNo);
        Log.e("xcghvxc=>",mydatas.request().toString());
        mydatas.enqueue(new Callback<List<ReceivedUpdatePojo>>() {
            @Override
            public void onResponse(Call<List<ReceivedUpdatePojo>> call, Response<List<ReceivedUpdatePojo>> response) {
                grievanceDataReceived.addAll(response.body());
                view.setRecyclerViewWithReceivedDatas(grievanceDataReceived);
            }

            @Override
            public void onFailure(Call<List<ReceivedUpdatePojo>> call, Throwable t) {
                Log.e("dsfv=>",t.getMessage());
            }
        });
        return grievanceDataReceived;
    }

    @Override
    public void updateReceived(Retrofit retrofit, String UniqueId, String GrievanceNo, String District, String Panchayat, String Remarks, String Status, String UserDesgination, String UserId, String UserName, String EntryType) {
        Call<ReceivedUpdateResult> receivedUpdateResult = retrofit.create(RetrofitInterface.class).updateResult("eyJhbGciOiJIUzI1NiJ9.UHJlbWF0aXg.VnYf5L2bruAL3IhIbhOCnqW3SADSM2qjWrZAV0yrB94"
                ,UniqueId, GrievanceNo, District, Panchayat, Remarks, Status, UserDesgination, UserId, UserName, EntryType );
        receivedUpdateResult.enqueue(new Callback<ReceivedUpdateResult>() {
            @Override
            public void onResponse(Call<ReceivedUpdateResult> call, Response<ReceivedUpdateResult> response) {
                if(response.body().getMessage().equalsIgnoreCase("Success"))
                    view.showResult("Updated Successfully!!");
                else
                    view.showResult(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ReceivedUpdateResult> call, Throwable t) {
                view.showResult(t.getMessage());
            }
        });
    }

    @Override
    public void filterTheItems(ReceivedPresenter receivedPresenter, PendingPresenter pendingPresenter, CompletedPresenter completedPresenter, FilterClass filterClass, String priority, String wardno, String streetName) {
        filterClass.filterTheItems(receivedPresenter,pendingPresenter,completedPresenter,priority, wardno,streetName,CommanDatas.sPendingList,"Pending");
    }


    public void informFilteredListToPrensenter(List<GrievanceData> grievanceData2) {
        view.showFilteredList(grievanceData2);
    }

}
