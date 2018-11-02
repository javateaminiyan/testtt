package com.examp.three.adapter.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examp.three.R;
import com.examp.three.model.Home.HomeBean;

import java.util.List;

public class CommanListAdapter extends RecyclerView.Adapter<CommanListAdapter.ViewHolder> {
    Context context;
    List<HomeBean> skillslist;
    LinearLayout skill_background;

    public CommanListAdapter(Context context, List<HomeBean> skillslist) {
        this.context = context;
        this.skillslist = skillslist;
    }

    @NonNull
    @Override
    public CommanListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counselling_home_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommanListAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(skillslist.get(position).getName());
        holder.imageView.setImageDrawable(context.getResources().getDrawable(skillslist.get(position).getImageId()));
    }

    @Override
    public int getItemCount() {
        return skillslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.tv_name);
            imageView = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }
}
