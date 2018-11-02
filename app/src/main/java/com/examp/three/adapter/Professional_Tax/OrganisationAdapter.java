package com.examp.three.adapter.Professional_Tax;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.examp.three.R;
import com.examp.three.model.Organisations;

public class OrganisationAdapter extends RecyclerView.Adapter<OrganisationAdapter.MyViewHolder>{

    List<Organisations> orgList;
    Context context;

    public OrganisationAdapter(Context context, List<Organisations> orgList){
        this.context = context;
        this.orgList=orgList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.org_item_view,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Organisations organisationsList = orgList.get(position);

        holder.tvOrgName.setText(Html.fromHtml(organisationsList.getorgName()));

    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrgName;

        LinearLayout llOrg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrgName = (TextView)itemView.findViewById(R.id.tv_orgname);

            llOrg = (LinearLayout)itemView.findViewById(R.id.ll_org);

        }
    }
}

