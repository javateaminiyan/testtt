package com.examp.three.presenter.Grievances_admin.contracts;



import com.examp.three.model.Grievances_admin.GrievanceData;
import com.examp.three.model.Grievances_admin.ReceivedUpdatePojo;
import com.examp.three.presenter.Grievances_admin.CompletedPresenter;
import com.examp.three.presenter.Grievances_admin.FilterClass;
import com.examp.three.presenter.Grievances_admin.PendingPresenter;
import com.examp.three.presenter.Grievances_admin.ReceivedPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public interface IPendingInterface {
    interface view{
        void showResult(String msg);
        void setRecyclerViewWithitsData(List<GrievanceData> grievanceData1);
        void setRecyclerViewWithReceivedDatas(List<ReceivedUpdatePojo> grievanceData1);
        void showFilteredList(List<GrievanceData> grievanceData2);
    }
    interface model{

    }
    interface presenter{
        ArrayList<String> getDesignationList();
        List<GrievanceData> getGdata(Retrofit retrofit, String type, String district, String panchayat, String grievanceNo);
        List<ReceivedUpdatePojo> getReceivedPojo(Retrofit retrofit, String type, String district, String panchayat, String grievanceNo);
        void updateReceived(Retrofit retrofit, String UniqueId, String GrievanceNo, String District,
                            String Panchayat, String Remarks, String Status,
                            String UserDesgination, String UserId, String UserName, String EntryType);
        void filterTheItems(ReceivedPresenter receivedPresenter, PendingPresenter pendingPresenter, CompletedPresenter completedPresenter, FilterClass filterClass, String priority, String wardno, String streetName);

    }
}
