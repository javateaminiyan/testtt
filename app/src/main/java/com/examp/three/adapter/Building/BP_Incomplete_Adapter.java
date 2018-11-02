package com.examp.three.adapter.Building;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.examp.three.R;
import com.examp.three.model.Building.BP_Incomplete_Pojo;


public class BP_Incomplete_Adapter extends RecyclerView.Adapter<BP_Incomplete_Adapter.ViewHolder> {

    private ArrayList<BP_Incomplete_Pojo> beanList;

    public BP_Incomplete_Adapter(ArrayList<BP_Incomplete_Pojo> beanList) {
        this.beanList = beanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_bp_incomplete, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bp_incomp_tv_name.setText("Name : " + beanList.get(position).getmApplicationName());
        holder.bp_incomp_tv_father_name.setText("Father Name : " + beanList.get(position).getmFatherName());
        holder.bp_incomp_tv_mobileno.setText("Mobile No : " + beanList.get(position).getmMobileNo());
        holder.bp_incomp_tv_emailid.setText("Email Id : " + beanList.get(position).getmEmailId());
        holder.bp_incmp_tv_plot_area.setText("Plot Area (Sqft) : " + String.valueOf(beanList.get(position).getmPlotAreaSqFt()));
        holder.bp_incmp_tv_buildingtype.setText("Building Type : " + beanList.get(position).getmBuildingType());
        holder.bp_incmp_tv_status.setText("Status : " + beanList.get(position).getmAppStatus());
        holder.bp_incmp_tv_date.setText("Date : " + beanList.get(position).getmAppDate());
        holder.bp_incmp_tv_app_no.setText("Application No : " + beanList.get(position).getmApplicationNo());

    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public ArrayList<BP_Incomplete_Pojo> getList() {
        return beanList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bp_incomp_tv_name, bp_incomp_tv_father_name, bp_incomp_tv_mobileno, bp_incomp_tv_emailid,
                bp_incmp_tv_plot_area,
                bp_incmp_tv_buildingtype, bp_incmp_tv_status, bp_incmp_tv_date, bp_incmp_tv_click, bp_incmp_tv_app_no;

        public ViewHolder(View itemView) {
            super(itemView);
            bp_incomp_tv_name = itemView.findViewById(R.id.bp_incomp_tv_name);
            bp_incomp_tv_father_name = itemView.findViewById(R.id.bp_incomp_tv_father_name);
            bp_incomp_tv_mobileno = itemView.findViewById(R.id.bp_incomp_tv_mobileno);
            bp_incomp_tv_emailid = itemView.findViewById(R.id.bp_incomp_tv_emailid);
            bp_incmp_tv_plot_area = itemView.findViewById(R.id.bp_incmp_tv_plot_area);
            bp_incmp_tv_buildingtype = itemView.findViewById(R.id.bp_incmp_tv_buildingtype);
            bp_incmp_tv_status = itemView.findViewById(R.id.bp_incmp_tv_status);
            bp_incmp_tv_date = itemView.findViewById(R.id.bp_incmp_tv_date);
            bp_incmp_tv_click = itemView.findViewById(R.id.bp_incmp_tv_click);
            bp_incmp_tv_app_no = itemView.findViewById(R.id.bp_incmp_tv_app_no);
        }
    }

}
