package com.examp.three.adapter.Grievances_admin;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.examp.three.R;
import com.examp.three.model.Grievances_admin.GrievanceData;

import java.util.List;

public class GrievListReceived extends RecyclerView.Adapter<GrievListReceived.ViewHolder> {
    Context context;
    List<GrievanceData> grievDatalist;
    clickCallBack clickCallBack1;
    Typeface avvaiyarfont;
    public GrievListReceived(Context context, List<GrievanceData> grievDatalist, clickCallBack clickCallBack1) {
        this.context = context;
        this.grievDatalist = grievDatalist;
        this.clickCallBack1 = clickCallBack1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_grievance_complain,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_com_type.setText((position+1)+" . "+grievDatalist.get(position).getComplaintType());
        holder.tv_date.setText(grievDatalist.get(position).getDate());
        holder.tv_name.setText(grievDatalist.get(position).getComplainerName());
        holder.tv_mobno.setText(grievDatalist.get(position).getMobileNo());
        holder.tv_street.setText(grievDatalist.get(position).getStreetName());
        holder.tv_street.setTypeface(avvaiyarfont);
        holder.tv_wardno.setText(grievDatalist.get(position).getWardNo());
        holder.tv_grv.setText(Integer.toString(grievDatalist.get(position).getComplaintNo()));
        if(grievDatalist.get(position).getStatus().equalsIgnoreCase("Completed")){
            holder.tv_status_title.setText("Status");
            holder.tv_atnd.setText("Completed");
        }else{
            holder.tv_status_title.setText("Attend");
            holder.tv_atnd.setText("Yes");
        }
        holder.tv_atnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack1.receivedClick(grievDatalist.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return grievDatalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_com_type,tv_date,tv_mobno,tv_name,tv_street,tv_wardno,tv_grv,tv_atnd,tv_status_title;
        public ViewHolder(View itemView) {
            super(itemView);
            avvaiyarfont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/avvaiyar.ttf");
            tv_com_type = (TextView)itemView.findViewById(R.id.tv_com_type);
            tv_date = (TextView)itemView.findViewById(R.id.tv_date);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_mobno = (TextView)itemView.findViewById(R.id.tv_mobno);
            tv_street = (TextView)itemView.findViewById(R.id.tv_street);
            tv_status_title = (TextView)itemView.findViewById(R.id.tv_status_title);
            this.tv_street.setTypeface(avvaiyarfont);
            tv_wardno = (TextView)itemView.findViewById(R.id.tv_wardno);
            tv_grv = (TextView)itemView.findViewById(R.id.tv_grv);
            tv_atnd = (TextView)itemView.findViewById(R.id.tv_atnd);
        }
    }
   public interface clickCallBack{
        void receivedClick(GrievanceData grievanceData);
    }
}
