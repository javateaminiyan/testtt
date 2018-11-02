package com.examp.three.adapter.Building;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.examp.three.R;
import com.examp.three.model.Building.Building_OnlinePayment_Pojo;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.activity.Trade.PaymentGateway_OnlinePayment;
import com.examp.three.activity.paymentgateway.utility.AvenuesParams;
import com.examp.three.activity.paymentgateway.utility.Params;
import dmax.dialog.SpotsDialog;

import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_EMAIL;
import static com.examp.three.activity.paymentgateway.utility.AvenuesParams.BILLING_TEL;
import static com.examp.three.common.Common.ACCESS_TOKEN;
import static com.examp.three.common.Common.API_ONLINEPAYMENT_BUILDINGONCLICK;

public class Building_OnlinePayment_Adapter extends RecyclerView.Adapter<Building_OnlinePayment_Adapter.MyViewHolder> {

    ArrayList<Building_OnlinePayment_Pojo> paidList;
    Context mContext;
    LinearLayout rootlayout;
    android.app.AlertDialog waitingDialog;

    public Building_OnlinePayment_Adapter(Context context, ArrayList<Building_OnlinePayment_Pojo> paidList) {
        this.mContext = context;
        this.paidList = paidList;
    }

    @NonNull
    @Override
    public Building_OnlinePayment_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_building_onlinepayment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Building_OnlinePayment_Adapter.MyViewHolder holder, int position) {
        final Building_OnlinePayment_Pojo pay_online = paidList.get(position);

        holder.mtxt_applicationno.setText(Html.fromHtml("<b>Application No: </b> " + pay_online.getApplicationNo()));
        holder.mtxt_developmentcharge.setText(Html.fromHtml("<b>Development Charge :</b>" + pay_online.getDevelopmentCharge()));
        holder.mtxt_applicationdate.setText(Html.fromHtml(pay_online.getApplicationDate()));
        holder.mtxt_applicationname.setText(Html.fromHtml(pay_online.getApplicationName()));
        holder.mtxt_totalbl.setText(Html.fromHtml("<b>Total Amount :</b>" + pay_online.getTotalBL()));
        holder.mtxt_applicant_name.setText(Html.fromHtml("<b>Applicant Name :</b>" + pay_online.getApplicantName()));
        holder.mtxt_vacantandtax.setText(Html.fromHtml("<b>Vacant and Tax :</b>" + pay_online.getVacantLandTax()));
        holder.mtxt_licencefee.setText(Html.fromHtml("<b>Licence Fee :</b>" + pay_online.getLicenceFees()));
        holder.btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                building_pendinglicence(pay_online);

            }
        });
    }

    @Override
    public int getItemCount() {
        return paidList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mtxt_applicationno, mtxt_applicationdate, mtxt_applicationname, mtxt_applicant_name, mtxt_totalbl, mtxt_developmentcharge, mtxt_vacantandtax, mtxt_licencefee;
        Button btn_pay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxt_applicationno = itemView.findViewById(R.id.txt_applicationno);
            mtxt_developmentcharge = itemView.findViewById(R.id.txt_developementcharge);
            mtxt_applicationdate = itemView.findViewById(R.id.txt_applicationdate);
            mtxt_applicationname = itemView.findViewById(R.id.txt_applicationname);
            mtxt_vacantandtax = itemView.findViewById(R.id.txt_VacantLandTax);
            mtxt_totalbl = itemView.findViewById(R.id.txt_TotalBL);
            mtxt_applicant_name = itemView.findViewById(R.id.txt_applicantname);

            rootlayout = itemView.findViewById(R.id.rootlayout);
            mtxt_licencefee = itemView.findViewById(R.id.txt_licencefee);
            btn_pay = itemView.findViewById(R.id.btn_pay);

        }
    }

    //This method is for pending building lisence retrieval
    private void building_pendinglicence(final Building_OnlinePayment_Pojo pay_online) {
        waitingDialog = new SpotsDialog(mContext);
        waitingDialog.show();
        waitingDialog.setCancelable(false);
        String REQUEST_TAG = "apiDistrictDetails_Request";


        JsonArrayRequest apiDistrictDetails_Request = new JsonArrayRequest(Request.Method.GET, API_ONLINEPAYMENT_BUILDINGONCLICK + "ApplicationNo="+pay_online.getApplicationNo(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    //Log.e(TAG, response.toString());


                    if (!response.isNull(0)) {
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject jsonObject = response.getJSONObject(i);

                            String UserId = jsonObject.optString("UserId");
                            String ApplicationNo = jsonObject.optString("ApplicationNo");
                            String ApplicationDate = jsonObject.optString("ApplicationDate");
                            String ApplicantName = jsonObject.optString("ApplicantName");
                            String ApplicationName = jsonObject.optString("ApplicationName");
                            String MobileNo = jsonObject.optString("MobileNo");
                            String EmailId = jsonObject.optString("EmailId");
                            String Panchayat = jsonObject.optString("Panchayat");
                            String District = jsonObject.optString("District");
                            String UserName = jsonObject.optString("UserName");


                            Intent move = new Intent(mContext, PaymentGateway_OnlinePayment.class);

                            move.putExtra("OnlinePaymentType", "Building");
                            move.putExtra("TotalAmount", pay_online.getTotalBL());
                            move.putExtra(BILLING_EMAIL, EmailId);
                            move.putExtra(BILLING_TEL, MobileNo);
                            move.putExtra(Params.userId, UserId);
                            move.putExtra(AvenuesParams.AMOUNT, pay_online.getTotalBL());
                            move.putExtra(Params.panchayatName, Panchayat);
                            move.putExtra("ApplicationRefNo", ApplicationNo);
                            move.putExtra(Params.districtName, District);
                            move.putExtra("user", UserName);
                            move.putExtra("applicantname", ApplicantName);


                            mContext.startActivity(move);

                        }


                    } else Snackbar.make(rootlayout, "Error", Snackbar.LENGTH_SHORT).show();


                    waitingDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();

                    Snackbar.make(rootlayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    waitingDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                waitingDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    SnackShowTop("Time out", rootlayout);
                } else if (error instanceof AuthFailureError) {
                    SnackShowTop("Connection Time out", rootlayout);
                } else if (error instanceof ServerError) {
                    SnackShowTop("Could not connect server", rootlayout);
                } else if (error instanceof NetworkError) {
                    SnackShowTop("Please check the internet connection", rootlayout);
                } else if (error instanceof ParseError) {
                    SnackShowTop("Parse Error", rootlayout);
                } else {
                    SnackShowTop(error.getMessage(), rootlayout);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);

                return params;
            }
        };
        AppSingleton.getInstance(mContext).addToRequestQueue(apiDistrictDetails_Request, REQUEST_TAG);


    }

    //This method is for showing snack bar in this activity
    private void SnackShowTop(String message, View view) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view_layout = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_layout.getLayoutParams();
        params.gravity = Gravity.TOP;
        view_layout.setLayoutParams(params);
        snack.show();
    }


}