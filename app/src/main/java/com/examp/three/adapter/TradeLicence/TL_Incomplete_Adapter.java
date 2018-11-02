package com.examp.three.adapter.TradeLicence;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.model.TradeLicence.TL_Incomplete_Pojo;

import java.util.ArrayList;


public class TL_Incomplete_Adapter extends RecyclerView.Adapter<TL_Incomplete_Adapter.ViewHolder> {

    private ArrayList<TL_Incomplete_Pojo> beanList;
    clicktoComplete clicktoComplete;

    public TL_Incomplete_Adapter(ArrayList<TL_Incomplete_Pojo> beanList, clicktoComplete clicktoComplete) {
        this.beanList = beanList;
        this.clicktoComplete = clicktoComplete;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_tl_incomplete, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.incomp_tv_name.setText("Name : " + beanList.get(position).getName());
        holder.incomp_tv_mobileno.setText("Mobile No : " + beanList.get(position).getMobileNo());
        holder.incomp_tv_emailid.setText("Email Id : " + beanList.get(position).getEmailID());
        holder.incomp_tv_district.setText("District : " + beanList.get(position).getDistrict());
        holder.incomp_tv_panchayat.setText("Panchayat : " + beanList.get(position).getPanchayat());
        holder.incomp_tv_entrydate.setText("Entry Date : " + beanList.get(position).getEntryDate());

        holder.incomp_tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicktoComplete.clickToComplete(position, beanList.get(position).getSno());
            }
        });

    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public ArrayList<TL_Incomplete_Pojo> getList() {
        return beanList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView incomp_tv_name, incomp_tv_mobileno, incomp_tv_emailid, incomp_tv_district,
                incomp_tv_panchayat, incomp_tv_entrydate, incomp_tv_click;

        public ViewHolder(View itemView) {
            super(itemView);
            incomp_tv_name = itemView.findViewById(R.id.incomp_tv_name);
            incomp_tv_mobileno = itemView.findViewById(R.id.incomp_tv_mobileno);
            incomp_tv_emailid = itemView.findViewById(R.id.incomp_tv_emailid);
            incomp_tv_district = itemView.findViewById(R.id.incomp_tv_district);
            incomp_tv_panchayat = itemView.findViewById(R.id.incomp_tv_panchayat);
            incomp_tv_entrydate = itemView.findViewById(R.id.incomp_tv_entrydate);
            incomp_tv_click = itemView.findViewById(R.id.incomp_tv_click);
        }
    }

    public interface clicktoComplete {
        void clickToComplete(int position, int sno);
    }
}
