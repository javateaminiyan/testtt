package com.examp.three.adapter.TradeLicence;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.common.Common;
import com.examp.three.model.TradeLicence.ViewReceiptHistory_Pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ViewReceiptHistory_Adapter extends RecyclerView.Adapter<ViewReceiptHistory_Adapter.ViewHolder> {

    private ArrayList<ViewReceiptHistory_Pojo> beanlist;
    Context mContext;
    SpotsDialog spotsDialog;
    String   regdate;
    public ViewReceiptHistory_Adapter(Context ctx,ArrayList<ViewReceiptHistory_Pojo> beanlist) {
     this.mContext=ctx;
        this.beanlist = beanlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_view_receipt_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tv_referenceNo.setText("Reference No : "+beanlist.get(position).getReferenceNo());
        holder.vrh_tv_app_date.setText(beanlist.get(position).getApplicationDate());
        holder.vrh_tv_app_name.setText(beanlist.get(position).getApplicantName());
        holder.vrh_tv_establish_name.setText(beanlist.get(position).getEstablishmentName());
        holder.vrh_tv_finyear.setText(beanlist.get(position).getFinYear());
        holder.vrh_tv_licence_validity.setText(beanlist.get(position).getLicenseValidity());
        holder.vrh_tv_licence_no.setText(beanlist.get(position).getLicenseNo());
        holder.vrh_tv_district.setText(beanlist.get(position).getDistrict());
        holder.vrh_tv_panchayat.setText(beanlist.get(position).getPanchayat());
        holder.vrh_tv_status.setText("Status : "+beanlist.get(position).getStatus());

        holder.btnReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             getViewDate(beanlist.get(position).getLicenseNo(),beanlist.get(position).getTradersCode(),beanlist.get(position).getDistrict(),beanlist.get(position).getPanchayat(),beanlist.get(position).getFinYear());

                String url= "https://www.etownpanchayat.com/PublicTL/Transaction/Receipt.aspx?qDistrict="+beanlist.get(position).getDistrict()+"&qPanchayat="+beanlist.get(position).getPanchayat()+"" +
                        "&qFinancialYear="+beanlist.get(position).getFinYear()+"&qUserName="+beanlist.get(position).getApplicantName()+"&" +
                        "qUserId="+beanlist.get(position).getUserid()+"&qRegNo="+beanlist.get(position).getLicenseNo()+"&qDate="+regdate+"&qApplicationNo="+beanlist.get(position).getReferenceNo()+"&qReportType=PaymentHistory";

                openDownloadBrowser(mContext,url);
            }
        });

        holder.btnLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getViewDate(beanlist.get(position).getLicenseNo(),beanlist.get(position).getTradersCode(),beanlist.get(position).getDistrict(),beanlist.get(position).getPanchayat(),beanlist.get(position).getFinYear())  ;

                String url= "https://www.etownpanchayat.com/PublicTL/Transaction/Receipt.aspx?qDistrict="+beanlist.get(position).getDistrict()+"&" +
                        "qPanchayat="+beanlist.get(position).getPanchayat()+"&qFinancialYear="+beanlist.get(position).getFinYear()+"&qUserName="+beanlist.get(position).getApplicantName()+"&qUserId="+beanlist.get(position).getUserid()+"&qRegNo="+beanlist.get(position).getLicenseNo()+"" +
                        "&qDate="+regdate+"&qApplicationNo="+beanlist.get(position).getReferenceNo()+"&qReportType=ViewLicenceReceipt";
                openDownloadBrowser(mContext,url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_referenceNo, vrh_tv_app_date, vrh_tv_app_name, vrh_tv_establish_name, vrh_tv_finyear,
                vrh_tv_licence_validity, vrh_tv_licence_no, vrh_tv_district, vrh_tv_panchayat, vrh_tv_status;
        Button btnReceipt,btnLicence;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_referenceNo = (TextView) itemView.findViewById(R.id.tv_referenceNo);
            vrh_tv_app_date = (TextView) itemView.findViewById(R.id.vrh_tv_app_date);
            vrh_tv_app_name = (TextView) itemView.findViewById(R.id.vrh_tv_app_name);
            vrh_tv_establish_name = (TextView) itemView.findViewById(R.id.vrh_tv_establish_name);
            vrh_tv_finyear = (TextView) itemView.findViewById(R.id.vrh_tv_finyear);
            vrh_tv_licence_validity = (TextView) itemView.findViewById(R.id.vrh_tv_licence_validity);
            vrh_tv_licence_no = (TextView) itemView.findViewById(R.id.vrh_tv_licence_no);
            vrh_tv_district = (TextView) itemView.findViewById(R.id.vrh_tv_district);
            vrh_tv_panchayat = (TextView) itemView.findViewById(R.id.vrh_tv_panchayat);
            vrh_tv_status = (TextView) itemView.findViewById(R.id.vrh_tv_status);
            btnReceipt = (Button)itemView.findViewById(R.id.btn_receipt);
            btnLicence = (Button)itemView.findViewById(R.id.btn_licence);

        }
    }
    public void openDownloadBrowser(Context activity, String url) {
        Uri uri = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        if (i.resolveActivity(activity.getPackageManager()) == null) {
            i.setData(Uri.parse(url));
        }
        activity.startActivity(i);
    }

    public void  getViewDate(String licenceNo,String tradersCode,String district,String panchayat,String finYear){

        String url= Common.baseUrl+"Trade_GetTradeLicenceViewDateAndReceipt?LicenceNo="+licenceNo+"&" +
                "TradersCode="+tradersCode+"&District="+district+"&Panchayat="+panchayat+"&FinancialYear="+finYear+"";

        System.out.println("arrrrrr"+url);

        JsonArrayRequest api_history_details = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                System.out.println("arrrrrr"+response);

                if (response.length() > 0) {

                        try {
                            JSONObject json_history = (JSONObject) response.getJSONObject(0);
                            int regNo = json_history.getInt("RegNo");
                            int tradersCode = json_history.getInt("TradersCode");
                            regdate = json_history.getString("RegDate");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("arrrrrr"+error.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", Common.ACCESS_TOKEN);
                return params;
            }
        };
        AppSingleton.getInstance(mContext).addToRequestQueue(api_history_details, "HistoyRequest");

    }
}
