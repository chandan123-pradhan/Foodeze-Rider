package com.foodeze.rider.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.foodeze.rider.Constants.Config;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import com.foodeze.rider.R;
import com.foodeze.rider.ActivitiesAndFragments.Fragments.RJobsFragment;
import com.foodeze.rider.Models.RiderJobModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class RPendingJobAdapter extends RecyclerView.Adapter<RPendingJobAdapter.ViewHolder> {

    ArrayList<RiderJobModel> getDataAdapter;
    Context context;
    OnItemClickListner onItemClickListner;
    RJobsFragment rJobsFragment;
    CamomileSpinner progressBar;
    RelativeLayout progressDialog,transparent_layer;


    public RPendingJobAdapter(ArrayList<RiderJobModel> getDataAdapter, Context context,RJobsFragment fragment,CamomileSpinner progressBar,
                              RelativeLayout progressDialog,RelativeLayout transparent_layer){
        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
        this.rJobsFragment = fragment;
        this.progressBar = progressBar;
        this.progressDialog = progressDialog;
        this.transparent_layer = transparent_layer;

    }

    @Override
    public RPendingJobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_rider_current_job, parent, false);

        RPendingJobAdapter.ViewHolder viewHolder = new RPendingJobAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        RiderJobModel jobListModel = getDataAdapter.get(position);
        String symbol = jobListModel.getRider_symbol();

        holder.r_hotel_name.setText(jobListModel.getHotel_name());
        holder.r_order_number.setText("Order #"+jobListModel.getOrder_number());
        holder.r_order_address.setText(jobListModel.getHotel_address());
        holder.r_total_bil_tv.setText(symbol+jobListModel.getOrder_price());
        holder.card_detail_tv.setText(jobListModel.getOrder_cash_status());
        holder.time_tv.setText(jobListModel.getOrder_time());


        holder.r_job_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemClickListner.OnItemClicked(v, position);
            }
        });

        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                acceptRiderOrder(getDataAdapter.get(position).getOrder_number());
            }
        });
    }


    @Override
    public int getItemCount() {
        return getDataAdapter.size() ;
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView r_hotel_name,r_order_number,r_order_address,r_total_bil_tv,card_detail_tv,time_tv;
        RelativeLayout r_job_main;
        Button accept_btn;


        public ViewHolder(View itemView) {

            super(itemView);

            r_hotel_name = itemView.findViewById(R.id.r_hotel_name);
            r_order_number = itemView.findViewById(R.id.r_order_number);
            r_order_address = itemView.findViewById(R.id.r_order_address);
            r_total_bil_tv = itemView.findViewById(R.id.r_total_bil_tv);
            card_detail_tv = itemView.findViewById(R.id.card_detail_tv);
            time_tv = itemView.findViewById(R.id.time_tv);

            r_job_main = itemView.findViewById(R.id.r_job_main);
            accept_btn = itemView.findViewById(R.id.accept_btn);


        }
    }

    public interface OnItemClickListner {
        void OnItemClicked(View view, int position);
    }

    public void setOnItemClickListner(RPendingJobAdapter.OnItemClickListner onCardClickListner) {
        this.onItemClickListner = onCardClickListner;
    }


    private void acceptRiderOrder(String order_id) {

        progressBar.start();
        progressDialog.setVisibility(View.VISIBLE);
        transparent_layer.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
            jsonObject.put("status", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.Accept_RIDER_ORDER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String str = response.toString();
                try {
                    JSONObject jsonObject1 = new JSONObject(str);

                    int code = Integer.parseInt(jsonObject1.optString("code"));
                    if (code == 200) {

                        rJobsFragment.getCurrentOrderList();
                        rJobsFragment.getPendingOrderList();
                        notifyDataSetChanged();
                        progressDialog.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("api-key", "2a5588cf-4cf3-4f1c-9548-cc1db4b54ae3");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }


}