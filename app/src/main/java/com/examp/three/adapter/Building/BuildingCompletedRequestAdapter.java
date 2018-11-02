package com.examp.three.adapter.Building;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.examp.three.R;
import com.examp.three.model.Building.BLCompletedRequestBean;

public class BuildingCompletedRequestAdapter extends RecyclerView.Adapter<BuildingCompletedRequestAdapter.ViewHolder> {

    Context mContext;
    List<BLCompletedRequestBean> mblList;

    public  BuildingCompletedRequestAdapter(Context context, List<BLCompletedRequestBean> blList) {
        this.mContext = context;
        this.mblList = blList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_building_completedreq, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BLCompletedRequestBean blist= mblList.get(position);

        holder.tv_appNo.setText("App No :"+blist.getApplicationNo());
        holder.tv_appdate.setText(blist.getApplicationDate());
        holder.tv_name.setText(blist.getApplicantName());
        holder.tv_obpaDate.setText(blist.getObpaDate());
        holder.tv_licenceFee.setText(blist.getLicenceFee());
        holder.tv_developmentcharge.setText(blist.getDelopmentCharge());
        holder.tv_vacantlandTax.setText(blist.getVacantLandTax());
        holder.tv_totalBl.setText(blist.getTotalbl());

    }

    @Override
    public int getItemCount() {
        return mblList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_appNo,tv_appdate,tv_name,tv_obpaDate,tv_licenceFee,tv_developmentcharge,tv_vacantlandTax,tv_totalBl;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_appNo = (TextView)itemView.findViewById(R.id.tv_appno);
            tv_appdate = (TextView)itemView.findViewById(R.id.tv_appdate);
            tv_name = (TextView)itemView.findViewById(R.id.tv_appname);
            tv_obpaDate = (TextView)itemView.findViewById(R.id.tv_obpadate);
            tv_licenceFee = (TextView)itemView.findViewById(R.id.tv_licencefee);
            tv_developmentcharge = (TextView)itemView.findViewById(R.id.tv_developmentcharge);
            tv_vacantlandTax = (TextView)itemView.findViewById(R.id.tv_vancantLandTax);
            tv_totalBl = (TextView)itemView.findViewById(R.id.tv_totalbl);

        }
    }


}
