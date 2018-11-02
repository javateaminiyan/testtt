package com.examp.three.adapter.TradeLicence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.model.TradeLicence.TradeNames;

import java.util.List;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.MyViewHolder>{

    List<TradeNames> orgList;
    Context context;

    public TradeAdapter(Context context, List<TradeNames> orgList){
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
        final TradeNames tradeNames = orgList.get(position);

        holder.tvTradeName.setText(Html.fromHtml(tradeNames.getDescription()));

    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTradeName;

        LinearLayout llTrade;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTradeName = (TextView)itemView.findViewById(R.id.tv_orgname);

            llTrade = (LinearLayout)itemView.findViewById(R.id.ll_org);

        }
    }
}
