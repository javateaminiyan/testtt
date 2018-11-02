package com.examp.three.adapter.Property_Tax;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.activity.Property.PaidHistory;
import com.examp.three.model.propertytax.PaymenyHistoryBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.MyViewHolder> {

    ArrayList<PaymenyHistoryBean> paidList;
    Context mContext;
    Ipaidhistory ipaidhistory;


    public PaymentHistoryAdapter(Context context, ArrayList<PaymenyHistoryBean> paidList, Ipaidhistory ipaidhistory) {
        this.mContext = context;
        this.paidList = paidList;
        this.ipaidhistory=ipaidhistory;

    }

    @NonNull
    @Override
    public PaymentHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.paid_history_item_new, parent, false);

        return new PaymentHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentHistoryAdapter.MyViewHolder holder, int position) {
        final PaymenyHistoryBean paidHistory = paidList.get(position);

        holder.tvReceiptNo.setText(Html.fromHtml("<b>Receipt No: </b> " + paidHistory.getmReceiptNo()));
        holder.tvAmount.setText(Html.fromHtml("<b>Amount :</b>" + paidHistory.getmAmount()));
        holder.tvAssessmentName.setText(paidHistory.getmAssessmentName());
        holder.tvDistrict.setText(Html.fromHtml("<b>District :</b>" + paidHistory.getmDistrict()));
        holder.tvPanchayat.setText(Html.fromHtml("<b>Panchayat :</b>" + paidHistory.getmPanchayat()));
        holder.tvTaxNo.setText(Html.fromHtml("<b>Tax No :</b>" + paidHistory.getmTaxNo()));
        holder.tvCollDate.setText(Html.fromHtml("<b>Date  :</b>" + paidHistory.getmCollectionDate()));
        holder.tvMobileNo.setText(Html.fromHtml("<b>Mobile :</b>" + paidHistory.getmMobileNo()));
        holder.tvOrderNo.setText(Html.fromHtml("<b>Order No :</b>" + paidHistory.getmOrderNo()));
        holder.tvEmail.setText(Html.fromHtml( paidHistory.getmEmailId()));

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.tvView.getText().toString().equalsIgnoreCase("View")){

                    holder.tvView.setText("Hide");
                    holder.llDistrictPanchayat.setVisibility(View.VISIBLE);
                    holder.llMobOrd.setVisibility(View.VISIBLE);
                    holder.llEmail.setVisibility(View.VISIBLE);
                }else {
                    holder.tvView.setText("View");
                    holder.llDistrictPanchayat.setVisibility(View.GONE);
                    holder.llMobOrd.setVisibility(View.GONE);
                    holder.llEmail.setVisibility(View.GONE);

                }
            }
        });

        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PaidHistory paidHistory1 = (PaidHistory)mContext;
                try {
                    paidHistory1.taxBalancePayment(Integer.valueOf(paidHistory.getmTaxNo()),paidHistory.getmDistrict(),
                            paidHistory.getmPanchayat(),paidHistory.getTaxType());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return paidList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvReceiptNo,tvAmount,tvAssessmentName,tvView,tvDistrict,tvPanchayat,tvTaxNo,tvCollDate,tvMobileNo,
                tvOrderNo,tvEmail;

        ImageView imgDownload;

        LinearLayout llDistrictPanchayat,llMobOrd,llEmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvView = itemView.findViewById(R.id.view);
            tvReceiptNo = itemView.findViewById(R.id.receipt_no);
            tvAmount = itemView.findViewById(R.id.amount);
            tvAssessmentName = itemView.findViewById(R.id.assessment_name);
            tvDistrict = itemView.findViewById(R.id.district);
            tvPanchayat = itemView.findViewById(R.id.panchayat);
            tvTaxNo = itemView.findViewById(R.id.tax_no);
            tvCollDate = itemView.findViewById(R.id.coll_date);
            tvMobileNo = itemView.findViewById(R.id.mobile_no);
            tvOrderNo = itemView.findViewById(R.id.order_no);
            tvEmail = itemView.findViewById(R.id.email_id);

            llDistrictPanchayat = itemView.findViewById(R.id.ll_dis_pan);
            llMobOrd = itemView.findViewById(R.id.ll_type_order);
            llEmail = itemView.findViewById(R.id.ll_email);

            imgDownload = itemView.findViewById(R.id.download);

        }
    }

    public   interface  Ipaidhistory {
        void viewReceipt(String TaxType, String taxNo, String receiptNo, String receiptDate, String district, String panchayat);
    }
}
