package com.examp.three.adapter.Building;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.examp.three.R;
import com.examp.three.model.BuildingPlan.BP_RequestStatus_Pojo;

public class BP_RequestStatus_Adapter extends RecyclerView.Adapter<BP_RequestStatus_Adapter.ViewHolder> {

    private ArrayList<BP_RequestStatus_Pojo> beanlist;

    Context context;

    public BP_RequestStatus_Adapter(ArrayList<BP_RequestStatus_Pojo> beanlist, Context context) {
        this.beanlist = beanlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_request_status, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bars_tv_appNo.setText(context.getString(R.string.cAppNo)+" "+ beanlist.get(position).getApplicationNo());
        holder.bars_tv_status.setText(context.getString(R.string.cStatus)+" "+ beanlist.get(position).getStatus());
        holder.bars_tv_date.setText( beanlist.get(position).getDate());
        holder.bars_tv_name.setText( beanlist.get(position).getName());
        holder.bars_tv_father_name.setText( beanlist.get(position).getFatherName());
        holder.bars_tv_mobileNo.setText( beanlist.get(position).getMobileNo());
        holder.bars_tv_emailId.setText( beanlist.get(position).getEmailId());
        holder.bars_tv_plotArea.setText( beanlist.get(position).getPlot_area_sqft());
        holder.bars_tv_buildingType.setText( beanlist.get(position).getBuildingType());

    }

    @Override
    public int getItemCount() {
        return beanlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bars_tv_appNo, bars_tv_date, bars_tv_name,
                bars_tv_father_name, bars_tv_mobileNo,
                bars_tv_emailId, bars_tv_plotArea,
                bars_tv_buildingType, bars_tv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bars_tv_appNo = (TextView) itemView.findViewById(R.id.bars_tv_appNo);
            bars_tv_date = (TextView) itemView.findViewById(R.id.bars_tv_date);
            bars_tv_name = (TextView) itemView.findViewById(R.id.bars_tv_name);
            bars_tv_father_name = (TextView) itemView.findViewById(R.id.bars_tv_father_name);
            bars_tv_mobileNo = (TextView) itemView.findViewById(R.id.bars_tv_mobileNo);
            bars_tv_emailId = (TextView) itemView.findViewById(R.id.bars_tv_emailId);
            bars_tv_plotArea = (TextView) itemView.findViewById(R.id.bars_tv_plot_area);
            bars_tv_buildingType = (TextView) itemView.findViewById(R.id.bars_tv_buildingType);

            bars_tv_status = (TextView) itemView.findViewById(R.id.bars_tv_status);
        }
    }
}
