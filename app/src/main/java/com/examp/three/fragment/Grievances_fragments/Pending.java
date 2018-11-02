package com.examp.three.fragment.Grievances_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.examp.three.R;
import com.examp.three.adapter.Grievances_admin.GrievListReceived;
import com.examp.three.adapter.Grievances_admin.ReceivedUpdateAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.DI.components.DaggerPendingComponent;
import com.examp.three.common.DI.modules.FilterModule;
import com.examp.three.common.DI.modules.NetworkModule;
import com.examp.three.common.DI.modules.PendingModule;
import com.examp.three.model.Grievances_admin.FilterPojo;
import com.examp.three.model.Grievances_admin.GrievanceData;
import com.examp.three.model.Grievances_admin.ReceivedUpdatePojo;
import com.examp.three.presenter.Grievances_admin.FilterClass;
import com.examp.three.presenter.Grievances_admin.PendingPresenter;
import com.examp.three.presenter.Grievances_admin.contracts.IPendingInterface;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Retrofit;

import static com.examp.three.common.SharedPreferenceHelper.pref_login_district_department;
import static com.examp.three.common.SharedPreferenceHelper.pref_login_panchayat_department;

public class Pending extends Fragment implements IPendingInterface.view,GrievListReceived.clickCallBack {
View PendingView;
@Inject
PendingPresenter pendingPresenter;

    @BindView(R.id.rv_grievance)RecyclerView rv_grievance;
    @BindView(R.id.root_layout)LinearLayout root_layout;
    @BindView(R.id.tv_data_pending_not_found)TextView tv_data_pending_not_found;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmer_view_container;

    GrievListReceived grievListReceived;

    List<GrievanceData> grievanceData;

    SharedPreferences sharedPreference;
    String MyPREFERENCES = "User";

    @Inject
    Retrofit retrofit;

    @Inject
    FilterClass filterClass;

    RecyclerView rv_received_update_table;

    String mType="Process";
    String mStatusType="Status";
    String mDistrict="Krishnagiri";
    String mPanchayat="Mathigiri";
    String mGrievanceNo="1020";
    String mUserDesignation="MD";
    String mUserId="1000237";
    String mUserName="Admin";
    String mEntryType="Android";
    String mUniqueNo;

    boolean isStatuspending= true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PendingView = inflater.inflate(R.layout.pending_fragment, container, false);
        ButterKnife.bind(this,PendingView);
        tv_data_pending_not_found.setVisibility(View.GONE);
        DaggerPendingComponent.builder().filterModule(new FilterModule()).networkModule(new NetworkModule("http://www.predemos.com")).pendingModule(new PendingModule(this)).build().injectPendingMethod(this);
        sharedPreference = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mDistrict = sharedPreference.getString(pref_login_district_department,null);
        mPanchayat = sharedPreference.getString(pref_login_panchayat_department,null);
        initRecyclerView();
        pendingPresenter.getGdata(retrofit,mType,mDistrict,mPanchayat,mGrievanceNo);
        return PendingView;
    }
    private void initRecyclerView() {
        rv_grievance.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_grievance.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showResult(String msg) {
        Snackbar.make(root_layout,msg,Snackbar.LENGTH_SHORT).show();
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
    public void setRecyclerViewWithReceivedDatas(List<ReceivedUpdatePojo> grievanceData1) {
        rv_received_update_table.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_received_update_table.setItemAnimator(new DefaultItemAnimator());
        ReceivedUpdateAdapter receivedUpdateAdapter = new ReceivedUpdateAdapter(grievanceData1);
        rv_received_update_table.setAdapter(receivedUpdateAdapter);
    }

    @Override
    public void showFilteredList(List<GrievanceData> grievanceData2) {

        if(grievanceData2.size()>0){
            tv_data_pending_not_found.setVisibility(View.GONE);
            rv_grievance.setVisibility(View.VISIBLE);
            grievanceData = grievanceData2;
            Log.e("xcbh=>",grievanceData.size()+"");
            grievListReceived = new GrievListReceived(getActivity(),grievanceData,this);
            rv_grievance.setAdapter(grievListReceived);
        }else{
            rv_grievance.setVisibility(View.GONE);
            tv_data_pending_not_found.setVisibility(View.VISIBLE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(FilterPojo filterPojo){
        Log.e("pending=>","<== pending");
        if(filterPojo!=null){
            if(filterPojo.getCatagory().equalsIgnoreCase("Pending")){
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
                pendingPresenter.filterTheItems(null,pendingPresenter,null,filterClass,priority, wardno, streetName);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void showStreetValuesInAlert(GrievanceData grievanceData) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View v2 = getLayoutInflater().inflate(R.layout.received_update_dialog, null);
        mGrievanceNo = Integer.toString(grievanceData.getComplaintNo());
        mUniqueNo = Integer.toString(grievanceData.getSno());

        ImageView cancel = (ImageView)v2.findViewById(R.id.cancel);
        TextView tv_gno = (TextView)v2.findViewById(R.id.tv_gno);
        TextView tv_c_type = (TextView)v2.findViewById(R.id.tv_c_type);
        TextView tv_wardno = (TextView)v2.findViewById(R.id.tv_wardno);
        RadioGroup rg_status_group = (RadioGroup) v2.findViewById(R.id.rg_status_group);
        RadioButton status_pending = (RadioButton) v2.findViewById(R.id.status_pending);
        RadioButton status_completed = (RadioButton) v2.findViewById(R.id.status_completed);
        final EditText et_remarks = (EditText) v2.findViewById(R.id.et_remarks);
        TextView tv_submit = (TextView) v2.findViewById(R.id.tv_submit);
        rv_received_update_table = (RecyclerView) v2.findViewById(R.id.rv_received_update_table);
        final TextView tv_Designation = (TextView) v2.findViewById(R.id.tv_Designation);
        tv_Designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpinnerPlaceBirth(tv_Designation);
            }
        });

        tv_gno.setText(Integer.toString(grievanceData.getComplaintNo()));
        tv_c_type.setText(grievanceData.getComplaintType());
        tv_wardno.setText(grievanceData.getWardNo());

        rg_status_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.status_pending){
                    isStatuspending=true;
                }else if(checkedId == R.id.status_completed){
                    isStatuspending=false;
                }
            }
        });
        pendingPresenter.getReceivedPojo(retrofit,mStatusType,mDistrict,mPanchayat,Integer.toString(grievanceData.getComplaintNo()));

        mBuilder.setView(v2);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_remarks.getText().toString().isEmpty()){
                    if(!tv_Designation.getText().equals("Select")){
                        mUserDesignation = tv_Designation.getText().toString();
                        String statusString;
                        if(isStatuspending){
                            statusString = "InProgress";
                        }else{
                            statusString = "Completed";
                        }
                        pendingPresenter.updateReceived(retrofit,mUniqueNo,mGrievanceNo,mDistrict,mPanchayat,et_remarks.getText().toString(),statusString,mUserDesignation,mUserId,mUserName,mEntryType);
                        dialog.dismiss();
                    }else{
                        showResult("Please choose the Designation");
                    }
                }else{
                    showResult("Please Fill the remarks");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    @Override
    public void receivedClick(GrievanceData grievanceData) {
        showStreetValuesInAlert(grievanceData);
    }
    public void setSpinnerPlaceBirth(final TextView tv) {
        final ArrayList<String> arrayList = pendingPresenter.getDesignationList();

        SpinnerDialog spinnerDialog = new SpinnerDialog(getActivity(), arrayList, "Select Designation", R.style.DialogAnimations_SmileWindow, "CLOSE");

        spinnerDialog.showSpinerDialog();

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tv.setText(arrayList.get(i));

            }
        });
    }
}
