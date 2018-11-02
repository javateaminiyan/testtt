package com.examp.three.adapter.Grievances_admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.model.Grievances_admin.ReceivedUpdatePojo;

import java.util.List;

/**
 * Created by admin on 15-09-2017.
 */

public class ReceivedUpdateAdapter extends RecyclerView.Adapter<ReceivedUpdateAdapter.ViewHolder> {

    private List<ReceivedUpdatePojo> beanList;

    public ReceivedUpdateAdapter(List<ReceivedUpdatePojo> beanList) {
        this.beanList = beanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_grievance_complains, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_date.setText(beanList.get(position).getDate());
        holder.tv_remarks.setText(beanList.get(position).getRemarks());
        holder.tv_status.setText(beanList.get(position).getStatus());
        if(beanList.get(position).getAttender()!=null)
        holder.tv_attender.setText(beanList.get(position).getAttender());
        else
        holder.tv_attender.setText("-");
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public List<ReceivedUpdatePojo> getList() {
        return beanList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_remarks,tv_status,tv_attender;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_remarks = (TextView) itemView.findViewById(R.id.tv_remarks);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_attender = (TextView) itemView.findViewById(R.id.tv_attender);
        }
    }
}
