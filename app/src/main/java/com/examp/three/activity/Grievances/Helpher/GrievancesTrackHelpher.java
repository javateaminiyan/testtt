package com.examp.three.activity.Grievances.Helpher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.examp.three.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.examp.three.adapter.Grievances.TrackGrievanceAdapter;
import com.examp.three.common.Common;
import com.examp.three.common.RetrofitInstance;
import com.examp.three.common.RetrofitInterface;
import com.examp.three.listener.RecyclerClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrievancesTrackHelpher {
    public static GrievancesTrackHelpher grievancesTrackHelpher = null;
    Activity activity;
    boolean isdatevalid;
    LinearLayout li_parent_lay;
    Typeface mAvvaiyarfont;

    TrackGrievanceAdapter trackGrievanceAdapter;
    AlertDialog dialogBirthDetails;
    AlertDialog dialogGriveanceDetails;
    ArrayList<com.examp.three.model.Grievance.TrackGrievanceBean> mGrievanceBeans = new ArrayList<>();

    private GrievancesTrackHelpher(Activity activity,LinearLayout li_parent_lay){
        this.activity = activity;
        this.li_parent_lay = li_parent_lay;
    }

    public static GrievancesTrackHelpher getInstance(Activity activity,LinearLayout li_parent_lay){
        if(grievancesTrackHelpher==null){
            grievancesTrackHelpher = new GrievancesTrackHelpher(activity,li_parent_lay);
        }
        grievancesTrackHelpher.activity = activity;
        return grievancesTrackHelpher;
    }

    //This method is for checking date
    public boolean checkDate(String fromDate,String toDate){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = sdf.parse(fromDate);
            Date date2 = sdf.parse(toDate);
            Log.e("sfhbfsd","dtg");
            if(date2.after(date1)){
                Log.e("correct","correctu");
                isdatevalid=true;
            }else if(date2.before(date1)){
                Snackbar.make(li_parent_lay,"To date should be after the from date",Snackbar.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  isdatevalid;
    }

    //This method is for retrieving grievances by date
    public void getGrievancesByDate(String fromDate, String toDate,String grievanceno){

        mGrievanceBeans.clear();
         RetrofitInterface retrofitInterface = RetrofitInstance.getRetrofit().create(RetrofitInterface.class);
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        if(!activity.isFinishing()){
            if(!pd.isShowing())
                pd.show();
        }
        Call grievanceResult = retrofitInterface.getGrievance(Common.ACCESS_TOKEN,
                grievanceno, parseDate(fromDate, "dd/MM/yyyy", "yyyy-MM-dd"), parseDate(toDate, "dd/MM/yyyy", "yyyy-MM-dd"));
        grievanceResult.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("re",response.toString());
                String response1 = new Gson().toJson(response.body());
                Log.e("re",response1);
                try {
                    JSONObject records = new JSONObject(response1);
                    JSONArray jsonArray = records.getJSONArray("recordsets");
                    if(jsonArray.length()>0){
                        JSONArray grievanceArray = jsonArray.getJSONArray(0);
                        if(grievanceArray.length()>0){
                            for(int i=0;i<grievanceArray.length();i++){
                                JSONObject grievanceObject = grievanceArray.getJSONObject(i);
                                int mGrivno = grievanceObject.optInt("Grievance No");
                                String mCompDate = grievanceObject.optString("ComplaintDate");
                                String mDistrict = grievanceObject.optString("District");
                                String mPanchayat = grievanceObject.optString("Panchayat");
                                String mComplaintType = grievanceObject.optString("ComplaintType");
                                String mDescription = grievanceObject.optString("Description");
                                String mName = grievanceObject.optString("Name");
                                String mDoorNo= grievanceObject.optString("DoorNo");
                                String mWardNo= grievanceObject.optString("WardNo");
                                String mStreetName= grievanceObject.optString("StreetName");
                                String mCity= grievanceObject.optString("City");
                                String mMobileNo= grievanceObject.optString("MobileNo");
                                String mEmailId= grievanceObject.optString("EmailId");
                                String mStatus= grievanceObject.optString("Status");

                                mGrievanceBeans.add(new com.examp.three.model.Grievance.TrackGrievanceBean(mGrivno,mCompDate,mDistrict,mPanchayat,mComplaintType,
                                        mDescription,mName,mDoorNo,mWardNo,mStreetName,mCity,mMobileNo,mEmailId,mStatus));
                            }
                            pd.dismiss();
                            if(mGrievanceBeans.size()>0){
                                Log.e("dfhv","SDfgvs");
                                trackGrievanceAdapter = new TrackGrievanceAdapter(mGrievanceBeans,activity);
                                alertShowGriveance(mGrievanceBeans);
                            }
                        }
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Log.e("exception=>",e.toString());
                    Snackbar.make(li_parent_lay,e.toString(),Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                pd.dismiss();
                Log.e("Failure=>",t.toString());
                Snackbar.make(li_parent_lay,t.toString(),Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    //This method is for searching grievances
    public void searchGrievance(String s){

            ArrayList<com.examp.three.model.Grievance.TrackGrievanceBean> filterdlist = new ArrayList<>();

            for (int i = 0; i < mGrievanceBeans.size(); i++) {
                if (Integer.toString(mGrievanceBeans.get(i).getmGrievanceNo()).toLowerCase().contains(s.toLowerCase())) {
                    filterdlist.add(mGrievanceBeans.get(i));
                }
            }

        trackGrievanceAdapter.filterList(filterdlist);
     }

    //This method is for showing the grievances in alert
    public void alertShowGriveance(final List<com.examp.three.model.Grievance.TrackGrievanceBean> data_list) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        Log.e("alerttt","vh");
        View v2 = activity.getLayoutInflater().inflate(R.layout.alert_griveance, null);
        final EditText et_search = (EditText) v2.findViewById(R.id.et_search);
        final LinearLayout rv_linear = (LinearLayout) v2.findViewById(R.id.rv_linear);
        TextView tv_close_dialog = (TextView)v2.findViewById(R.id.tv_close_dialog);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                searchGrievance(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RecyclerView recyclerView = (RecyclerView) v2.findViewById(R.id.rv_grievances);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v2.getContext());
        recyclerView.setLayoutManager(layoutManager);
        rv_linear.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(trackGrievanceAdapter);
        trackGrievanceAdapter.notifyDataSetChanged();

        mBuilder.setView(v2);
        dialogGriveanceDetails = mBuilder.create();
        dialogGriveanceDetails.setCancelable(false);
//        if(!activity.isFinishing()){
        if(!dialogGriveanceDetails.isShowing()){
            Log.e("XFGh","fdgfd");
            dialogGriveanceDetails.show();
        }
        tv_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGriveanceDetails.dismiss();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(activity, new RecyclerClickListener.OnItemTouchListener() {
            @Override
            public void OnItemCLikc(View view, int position) {

                dialogGriveanceDetails.dismiss();
                showSpecificDetail(position);
            }
        }));
    }

    //This method is for showing specific details in alert
    public void showSpecificDetail(int pos) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View v2 = activity.getLayoutInflater().inflate(R.layout.track_grievance_final, null);
        ImageView img_cancel =  (ImageView)v2.findViewById(R.id.img_cancel);
        TextView tv_grivno =  (TextView)v2.findViewById(R.id.tv_grivno);
        TextView tv_district =  (TextView)v2.findViewById(R.id.tv_district);
        TextView tv_panchayat =  (TextView)v2.findViewById(R.id.tv_panchayat);
        TextView tv_name =  (TextView)v2.findViewById(R.id.tv_name);
        TextView tv_doorno =  (TextView)v2.findViewById(R.id.tv_doorno);
        TextView tv_wardno =  (TextView)v2.findViewById(R.id.tv_wardno);
        TextView tv_streetname =  (TextView)v2.findViewById(R.id.tv_streetname);
        TextView tv_comptype =  (TextView)v2.findViewById(R.id.tv_comptype);
        TextView tv_description =  (TextView)v2.findViewById(R.id.tv_description);
        TextView tv_status =  (TextView)v2.findViewById(R.id.tv_status);
        mAvvaiyarfont = Typeface.createFromAsset(activity.getAssets(), "fonts/avvaiyar.ttf");
        tv_grivno.setText(mGrievanceBeans.get(pos).getmGrievanceNo()+"");
        tv_district.setText(mGrievanceBeans.get(pos).getmDistrict());
        tv_panchayat.setText(mGrievanceBeans.get(pos).getmPanchayat());
        tv_name.setText(mGrievanceBeans.get(pos).getmName());
        tv_doorno.setText(mGrievanceBeans.get(pos).getmDoorNo());
        tv_wardno.setText(mGrievanceBeans.get(pos).getmWardNo());
        tv_streetname.setText(mGrievanceBeans.get(pos).getmStreetName());
        tv_streetname.setTypeface(mAvvaiyarfont);
        tv_comptype.setText(mGrievanceBeans.get(pos).getmComplaintType());
        tv_description.setText(mGrievanceBeans.get(pos).getmDescription());
        tv_status.setText(mGrievanceBeans.get(pos).getmStatus());
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBirthDetails.dismiss();
            }
        });
        mBuilder.setView(v2);
        dialogBirthDetails = mBuilder.create();
        dialogBirthDetails.show();
    }

    //This method is for parsing the date values which is passed and return in expected format
    public String parseDate(String date, String givenformat, String resultformat) {

        String result = "";
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;

        try {
            sdf = new SimpleDateFormat(givenformat);
            sdf1 = new SimpleDateFormat(resultformat);
            result = sdf1.format(sdf.parse(date));

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            sdf = null;
            sdf1 = null;
        }
        return result;
    }
}
