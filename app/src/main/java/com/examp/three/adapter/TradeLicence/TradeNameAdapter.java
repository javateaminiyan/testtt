package com.examp.three.adapter.TradeLicence;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.model.TradeLicence.TradeBean;

import java.util.ArrayList;
/**
 * Created by admin on 15-09-2017.
 */
public class TradeNameAdapter extends RecyclerView.Adapter<TradeNameAdapter.ViewHolder> {

    private ArrayList<TradeBean> beanList;

    public TradeNameAdapter(ArrayList<TradeBean> beanList) {
        this.beanList = beanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_spinner_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvPlace.setText(beanList.get(position).getmDescriptionT());

    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public  void filterList(ArrayList<TradeBean> filterdNames) {
        this.beanList = filterdNames;
        notifyDataSetChanged();
    }

    public ArrayList<TradeBean> getList() {
        return beanList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPlace = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
