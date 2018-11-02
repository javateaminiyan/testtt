package com.examp.three.adapter.TradeLicence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.examp.three.R;
import com.examp.three.VolleySingleton.AppSingleton;
import com.examp.three.common.Common;
import com.examp.three.model.TradeLicence.TL_querydetails_pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.examp.three.common.Common.ACCESS_TOKEN;

public class ViewQuery_Adapter extends RecyclerView.Adapter<ViewQuery_Adapter.ViewHolder> {
    String responseViewQuery=null;
    private ArrayList<TL_querydetails_pojo> beanlist;
    Context mcontext ;

    public ViewQuery_Adapter(ArrayList<TL_querydetails_pojo> beanlist, Context context) {
        this.beanlist = beanlist;
        this.mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_view_query, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_onlineapplication_no.setText(beanlist.get(position).getmOnlineApplicationNo());
        holder.vq_tv_query.setText(beanlist.get(position).getmQuery());
        holder.vq_tv_querydate.setText(beanlist.get(position).getmQueryDate());
        holder.vq_tv_answer.setText(beanlist.get(position).getmAnswer());
        holder.vq_tv_answerdate.setText(beanlist.get(position).getmAnswerDate());
        holder.vq_tv_district.setText(beanlist.get(position).getmDistrict());
        holder.vq_tv_panchayat.setText(beanlist.get(position).getmPanchayat());
        holder.vq_tv_qastatus.setText(beanlist.get(position).getmQAStatus());

        holder.btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(beanlist.get(position).getmQAStatus().equalsIgnoreCase("ANSWERED")){

                    Toast.makeText(mcontext, "This Query Already Answered ", Toast.LENGTH_SHORT).show();

                }else {
                    holder.linear_button.setVisibility(View.GONE);
                    holder.linear_reply.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.btn_cancel_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linear_button.setVisibility(View.VISIBLE);
                holder.linear_reply.setVisibility(View.GONE);
            }
        });

        holder.btn_send_reply.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                String reply = holder.et_reply.getText().toString();

                String response = update_query(beanlist.get(position).getmSno(), beanlist.get(position).getmOnlineApplicationNo(), reply, beanlist.get(position).getmDistrict(), beanlist.get(position).getmPanchayat());

                if (response.equalsIgnoreCase("Success")) {
                    holder.vq_tv_answer.setText(holder.et_reply.getText().toString());

                }
                holder.linear_button.setVisibility(View.VISIBLE);
                holder.linear_reply.setVisibility(View.GONE);

            }



        });
    }

    @Override
    public int getItemCount() {
        return beanlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_onlineapplication_no, vq_tv_query,
                vq_tv_querydate, vq_tv_answer, vq_tv_answerdate, vq_tv_district,vq_tv_panchayat, vq_tv_qastatus;
        LinearLayout linear_button,linear_reply;
        Button btn_reply ,btn_cancel_reply,btn_send_reply;
        EditText et_reply;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_onlineapplication_no = (TextView) itemView.findViewById(R.id.tv_onlineapplication_no);
            vq_tv_query = (TextView) itemView.findViewById(R.id.vq_tv_query);
            vq_tv_querydate = (TextView) itemView.findViewById(R.id.vq_tv_querydate);
            vq_tv_answer = (TextView) itemView.findViewById(R.id.vq_tv_answer);
            vq_tv_answerdate = (TextView) itemView.findViewById(R.id.vq_tv_answerdate);
            vq_tv_district = (TextView) itemView.findViewById(R.id.vq_tv_district);
            vq_tv_panchayat = (TextView) itemView.findViewById(R.id.vq_tv_panchayat);
            vq_tv_qastatus = (TextView) itemView.findViewById(R.id.vq_tv_qastatus);

            linear_button = (LinearLayout)itemView.findViewById(R.id.linear_btn);
            linear_reply = (LinearLayout)itemView.findViewById(R.id.linear_reply);
            btn_reply = (Button)itemView.findViewById(R.id.btn_reply);
            btn_cancel_reply = (Button)itemView.findViewById(R.id.btn_cancel_reply);
            btn_send_reply  = (Button)itemView.findViewById(R.id.btn_send_reply);

            et_reply = (EditText)itemView.findViewById(R.id.et_reply);
        }
    }

    public String  update_query(String sno,String referenceno,String answer,String district,String panchayat){

        String url = Common.baseUrl+"Trade_UpdateTradeQueryDetails?Sno="+sno+"&ApplicationRefNo="+referenceno+"&Answer="+answer+"&District="+district+"&Panchayat="+panchayat+"";
        String REQUEST_TAG = "apiupdatequery_Request";

        System.out.println("rlllll"+url);
        JsonObjectRequest api_update_query = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println("ddddd"+response);
                try {
                    responseViewQuery=response.getString("Message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", ACCESS_TOKEN);


                return params;
            }
        };

        AppSingleton.getInstance(mcontext).addToRequestQueue(api_update_query, REQUEST_TAG);
        return  responseViewQuery;
    }
}
