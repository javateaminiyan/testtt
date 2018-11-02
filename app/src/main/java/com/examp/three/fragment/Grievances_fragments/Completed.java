package com.examp.three.fragment.Grievances_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examp.three.R;
import com.examp.three.adapter.Grievances_admin.GrievListReceived;
import com.examp.three.adapter.Grievances_admin.ReceivedUpdateAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.DI.components.DaggerCompletedComponent;
import com.examp.three.common.DI.modules.CompletedModule;
import com.examp.three.common.DI.modules.FilterModule;
import com.examp.three.common.DI.modules.NetworkModule;
import com.examp.three.common.SharedPreferenceHelper;
import com.examp.three.model.Grievances_admin.FilterPojo;
import com.examp.three.model.Grievances_admin.GrievanceData;
import com.examp.three.model.Grievances_admin.ReceivedUpdatePojo;
import com.examp.three.presenter.Grievances_admin.CompletedPresenter;
import com.examp.three.presenter.Grievances_admin.FilterClass;
import com.examp.three.presenter.Grievances_admin.contracts.ICompletedInterface;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_district_department;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_panchayat_department;


public class Completed extends Fragment implements ICompletedInterface.view, GrievListReceived.clickCallBack {

    View CompletedView;

    @BindView(R.id.rv_grievance)RecyclerView rv_grievance;
    @BindView(R.id.tv_data_completed_not_found)TextView tv_data_completed_not_found;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmer_view_container;

    GrievListReceived grievListReceived;

    List<GrievanceData> grievanceData;

    @Inject
    Retrofit retrofit;

    @Inject
    FilterClass filterClass;

    @Inject
    CompletedPresenter completedPresenter;

    RecyclerView rv_received_update_table;

    String mType="Completed";
    String mStatusType="Status";
    String mDistrict="";
    String mPanchayat="";
    String mGrievanceNo="";
    String mUserDesignation="MD";
    String mUserId="1000237";
    String mUserName="Admin";
    String mEntryType="Android";
    String mUniqueNo;

    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    boolean isStatuspending= true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CompletedView = inflater.inflate(R.layout.completed_fragment, container, false);

        ButterKnife.bind(this, CompletedView);
        tv_data_completed_not_found.setVisibility(View.GONE);
        DaggerCompletedComponent.builder().completedModule(new CompletedModule(this))
                .networkModule(new NetworkModule("http://www.predemos.com"))
                .filterModule(new FilterModule()).build().inject(this);
        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mDistrict = sharedPreference.getString(pref_login_district_department,null);
        mPanchayat = sharedPreference.getString(pref_login_panchayat_department,null);
        initRecyclerView();
        completedPresenter.getGdata(retrofit,mType,mDistrict,mPanchayat,mGrievanceNo);
        return CompletedView;
    }

    private void initRecyclerView() {
        rv_grievance.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_grievance.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void receivedClick(GrievanceData grievanceData) {
        showStreetValuesInAlert(grievanceData);
    }

    @Override
    public void showResult(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRecyclerViewWithitsData(List<GrievanceData> grievanceData1) {
        // Stopping Shimmer Effect's animation after data is loaded to ListView
        rv_grievance.setVisibility(View.VISIBLE);
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);
        grievanceData = grievanceData1;
        Log.e("xcbh",grievanceData.size()+"");
        grievListReceived = new GrievListReceived(getActivity(),grievanceData,this);
        rv_grievance.setAdapter(grievListReceived);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
    }

    @Override
    public void setRecyclerViewWithReceivedDatas(List<ReceivedUpdatePojo> recyclerViewWithReceivedDatas) {
        rv_received_update_table.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_received_update_table.setItemAnimator(new DefaultItemAnimator());
        ReceivedUpdateAdapter receivedUpdateAdapter = new ReceivedUpdateAdapter(recyclerViewWithReceivedDatas);
        rv_received_update_table.setAdapter(receivedUpdateAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(FilterPojo filterPojo){
        Log.e("pending=>","<== pending");
        if(filterPojo!=null){
            if(filterPojo.getCatagory().equalsIgnoreCase("Completed")){
                String priority, wardno, streetName;
                if(filterPojo.getPriority()!=null){
                    priority = filterPojo.getPriority();
                }else{
                    priority = "null";
                }
                if(filterPojo.getWard()!=null){
                    wardno = filterPojo.getWard();
                }else{
                    wardno = "null";
                }
                if(filterPojo.getStreet()!=null){
                    streetName = filterPojo.getStreet();
                }else{
                    streetName = "null";
                }
                completedPresenter.filterTheItems(null,null,completedPresenter,filterClass,priority, wardno, streetName);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showFilteredList(List<GrievanceData> grievanceData2) {
        if(grievanceData2.size()>0){
            tv_data_completed_not_found.setVisibility(View.GONE);
            rv_grievance.setVisibility(View.VISIBLE);
            grievanceData = grievanceData2;
            Log.e("xcbh=>",grievanceData.size()+"");
            grievListReceived = new GrievListReceived(getActivity(),grievanceData,this);
            rv_grievance.setAdapter(grievListReceived);
        }else{
            rv_grievance.setVisibility(View.GONE);
            tv_data_completed_not_found.setVisibility(View.VISIBLE);
        }

    }
    public void showStreetValuesInAlert(GrievanceData grievanceData) {

        android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        View v2 = getLayoutInflater().inflate(R.layout.received_update_completed_dialog, null);
        mGrievanceNo = Integer.toString(grievanceData.getComplaintNo());
        mUniqueNo = Integer.toString(grievanceData.getSno());


        ImageView cancel = (ImageView)v2.findViewById(R.id.cancel);
        TextView tv_gno = (TextView)v2.findViewById(R.id.tv_gno);
        TextView tv_c_type = (TextView)v2.findViewById(R.id.tv_c_type);
        TextView tv_wardno = (TextView)v2.findViewById(R.id.tv_wardno);
        TextView tv_street = (TextView)v2.findViewById(R.id.tv_street);
        rv_received_update_table = (RecyclerView) v2.findViewById(R.id.rv_received_update_table);

        tv_gno.setText(Integer.toString(grievanceData.getComplaintNo()));
        tv_c_type.setText(grievanceData.getComplaintType());
        tv_wardno.setText(grievanceData.getWardNo());
        tv_street.setText(grievanceData.getStreetName());

        completedPresenter.getReceivedPojo(retrofit,mStatusType,mDistrict,mPanchayat,Integer.toString(grievanceData.getComplaintNo()));

        mBuilder.setView(v2);
        final android.support.v7.app.AlertDialog dialog = mBuilder.create();
        dialog.show();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}

