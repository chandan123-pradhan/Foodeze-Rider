package com.foodeze.rider.ActivitiesAndFragments.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.foodeze.rider.Constants.AllConstants;
import com.foodeze.rider.Constants.ApiRequest;
import com.foodeze.rider.Constants.Callback;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Models.ROrderModel;
import com.foodeze.rider.Utils.FontHelper;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.RootFragment;
import com.foodeze.rider.ActivitiesAndFragments.Activities.ROnlineStatusActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.foodeze.rider.R;

import com.foodeze.rider.Services.UpdateLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class RJobsFragment extends RootFragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    SharedPreferences s_pref;
    RecyclerView pending_job_rv;
    RecyclerView current_job_rv;

    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.LayoutManager recyclerViewlayoutManager2;

    String user_id;

    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;

    Double latitude, longitude;
    public static boolean FLAG_CURRENT_JOB;
    TextView time_check_tv;
    Button r_check_out_btn;
    DatabaseReference mDatabase;
    FirebaseDatabase firebaseDatabase;

    List<Address> user = null;


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    CamomileSpinner customProgress;
    public RelativeLayout progressDialog;
    RelativeLayout transparent_layer;

    private static String serverkey;

    View v;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.rider_jobs_layout, container, false);
        context=getContext();

         s_pref = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
        user_id = s_pref.getString(PreferenceClass.pre_user_id, "");
        customProgress = v.findViewById(R.id.customProgress);
        progressDialog = v.findViewById(R.id.progressDialog);
        customProgress.start();
        transparent_layer = v.findViewById(R.id.transparent_layer);

        FrameLayout frameLayout = v.findViewById(R.id.main_container);
        FontHelper.applyFont(getContext(), frameLayout, AllConstants.verdana);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        pending_job_rv = v.findViewById(R.id.pending_job_list);
        current_job_rv = v.findViewById(R.id.current_job_list);
        time_check_tv = v.findViewById(R.id.time_check_tv);

        pending_job_rv.setHasFixedSize(false);
        current_job_rv.setHasFixedSize(false);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerViewlayoutManager2 = new LinearLayoutManager(getContext());
        pending_job_rv.setLayoutManager(recyclerViewlayoutManager);
        current_job_rv.setLayoutManager(recyclerViewlayoutManager2);

        r_check_out_btn = v.findViewById(R.id.r_check_out_btn);


        fn_permission();
        if (boolean_permission) {

            Intent intent = new Intent(getContext(), UpdateLocation.class);
            getContext().startService(intent);

        } else {
            Toast.makeText(getContext(), "Enable GPS", Toast.LENGTH_SHORT).show();
        }

        shouwOnlineStatus();

        showComingRiderShift();

        getPendingOrderList();
        getCurrentOrderList();


        r_check_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), ROnlineStatusActivity.class));
                getActivity().finish();
            }
        });


        return v;
    }


    public void getCurrentOrderList(){


        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference().child("RiderOrdersList").child(user_id).child("CurrentOrders");
        Query query=mDatabase.orderByKey();
        FirebaseRecyclerOptions<ROrderModel> options =
                new FirebaseRecyclerOptions.Builder<ROrderModel>()
                        .setQuery(query, ROrderModel.class)
                        .build();


        final FirebaseRecyclerAdapter<ROrderModel,RJobsFragment.Orderviewholder> fRadapter =
                new FirebaseRecyclerAdapter<ROrderModel, RJobsFragment.Orderviewholder>(options)
                {
                    @Override
                    protected void onBindViewHolder(RJobsFragment.Orderviewholder holder, int position, final ROrderModel model) {


                        holder.r_hotel_name.setText(model.getRestaurants());
                        holder.r_order_number.setText(model.getOrder_id());
                        holder.r_total_bil_tv.setText(model.getSymbol()+" "+model.getPrice());

                        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getserverkey(model.getOrder_id());
                                acceptRiderOrder(model.getOrder_id());
                            }
                        });



                    }

                    @Override
                    public RJobsFragment.Orderviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.r_row_item_job,parent,false);

                        return new RJobsFragment.Orderviewholder(view);
                    }


                    @Override
                    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
                        super.onAttachedToRecyclerView(recyclerView);
                        progressDialog.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);

                    }
                };

        fRadapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                fRadapter.notifyDataSetChanged();

            }
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                fRadapter.notifyDataSetChanged();
            }
        });

        fRadapter.startListening();

        current_job_rv.setAdapter(fRadapter);
        fRadapter.notifyDataSetChanged();



    }

    class Orderviewholder extends RecyclerView.ViewHolder{
        public TextView r_hotel_name,r_order_number,r_total_bil_tv;
        RelativeLayout r_job_main;
        Button accept_btn;


        public Orderviewholder(View itemView) {

            super(itemView);

            r_hotel_name = itemView.findViewById(R.id.r_hotel_name);
            r_order_number = itemView.findViewById(R.id.r_order_number);

            r_total_bil_tv = itemView.findViewById(R.id.r_total_bil_tv);

            r_job_main = itemView.findViewById(R.id.r_job_main);
            accept_btn = itemView.findViewById(R.id.accept_btn);


        }
    }


    private void acceptRiderOrder(final String order_id) {

        customProgress.start();
        progressDialog.setVisibility(View.VISIBLE);
        transparent_layer.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
            jsonObject.put("status", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(context, Config.Accept_RIDER_ORDER, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {

                 try {
                    JSONObject jsonObject1 = new JSONObject(resp);

                    int code = Integer.parseInt(jsonObject1.optString("code"));
                    if (code == 200) {

                        addToPendingOrders();


                    }

                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);
                }


            }
        });


    }


    public void addToPendingOrders(){

        final DatabaseReference add_to_onother=FirebaseDatabase.getInstance().getReference()
                .child("RiderOrdersList").child(user_id);
        add_to_onother.child("CurrentOrders").child(serverkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ROrderModel modelClass=dataSnapshot.getValue(ROrderModel.class);
                HashMap<String,String> map=new HashMap<>();
                map.put("order_id",modelClass.getOrder_id());
                map.put("price",modelClass.getPrice());
                map.put("restaurants",modelClass.getRestaurants());
                map.put("status",modelClass.getStatus());
                map.put("symbol",modelClass.getSymbol());

                add_to_onother.child("PendingOrders").child(serverkey)
                        .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        add_to_onother.child("CurrentOrders").child(serverkey).removeValue();

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getPendingOrderList(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference().child("RiderOrdersList").child(user_id).child("PendingOrders");
        Query query=mDatabase.orderByKey();
        FirebaseRecyclerOptions<ROrderModel> options =
                new FirebaseRecyclerOptions.Builder<ROrderModel>()
                        .setQuery(query, ROrderModel.class)
                        .build();


        final FirebaseRecyclerAdapter<ROrderModel,RJobsFragment.Orderviewholder> fRadapter =
                new FirebaseRecyclerAdapter<ROrderModel, RJobsFragment.Orderviewholder>(options)
                {
                    @Override
                    protected void onBindViewHolder(RJobsFragment.Orderviewholder holder, int position, final ROrderModel model) {


                            holder.r_hotel_name.setText(model.getRestaurants());
                            holder.r_order_number.setText("Order #"+model.getOrder_id());
                            holder.r_total_bil_tv.setText(model.getSymbol()+" "+model.getPrice());
                            holder.accept_btn.setVisibility(View.GONE);

                            holder.r_job_main.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FLAG_CURRENT_JOB = true;


                                    SharedPreferences.Editor editor = s_pref.edit();
                                    editor.putString(PreferenceClass.RIDER_HOTEL_NAME,model.getRestaurants());
                                    editor.putString(PreferenceClass.RIDER_ORDER_NUMBER,model.getOrder_id());
                                    editor.putString(PreferenceClass.RIDER_ORDER_SYMBOL,model.getSymbol());
                                    editor.putString(PreferenceClass.RIDER_TOTAL_PAYMENT,model.getPrice());
                                    editor.commit();

                                    Fragment restaurantMenuItemsFragment = new ROrderDetailFragment();
                                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                    transaction.addToBackStack(null);
                                    transaction.add(R.id.main_container, restaurantMenuItemsFragment,"parent").commit();
                                }
                            });



                    }

                    @Override
                    public RJobsFragment.Orderviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.r_row_item_job,parent,false);

                        return new RJobsFragment.Orderviewholder(view);
                    }


                    @Override
                    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
                        super.onAttachedToRecyclerView(recyclerView);
                        progressDialog.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);
                        //  view.findViewById(R.id.orderProgress).setVisibility(View.GONE);

                    }
                };

        fRadapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                fRadapter.notifyDataSetChanged();

            }
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                fRadapter.notifyDataSetChanged();
            }
        });

        fRadapter.startListening();

        pending_job_rv.setAdapter(fRadapter);
        fRadapter.notifyDataSetChanged();


    }





    public void getserverkey(String order_id){
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference();

        final Query query2 =mref.child("RiderOrdersList").child(user_id).child("CurrentOrders").orderByChild("order_id").equalTo(order_id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    serverkey = nodeDataSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void shouwOnlineStatus(){

        String online_status=s_pref.getString(PreferenceClass.RIDER_ONLINE_STATUS,"0");
        if(online_status.equals("1")){
            r_check_out_btn.setText("Check Out");
        }
        else {
            r_check_out_btn.setText("Check In");
        }

    }


    public void showComingRiderShift(){
        progressDialog.setVisibility(View.VISIBLE);
        transparent_layer.setVisibility(View.VISIBLE);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
        String formattedDate = df.format(c.getTime());

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",user_id);
            jsonObject.put("datetime",formattedDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Config.SHOW_UP_COMMING_RIDER_SHIFTS, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {

                try {
                    JSONObject jsonObject1 = new JSONObject(resp);
                    int code_id  = Integer.parseInt(jsonObject1.optString("code"));
                    if(code_id == 200){
                        JSONArray jsonArray = jsonObject1.getJSONArray("msg");

                        for(int i =0; i<jsonArray.length();i++){

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("RiderTiming");

                            String starting_time = jsonObject3.optString("starting_time");
                            String ending_time = jsonObject3.optString("ending_time");



                            SharedPreferences.Editor editor = s_pref.edit();
                            editor.putString(PreferenceClass.TIMING_ID,jsonObject3.optString("id"));
                            editor.commit();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                            Date dt,dt2;
                            try {
                                dt = sdf.parse(starting_time);
                                dt2 = sdf.parse(ending_time);
                                System.out.println("Time Display: " + sdfs.format(dt));
                                String finalTime = sdfs.format(dt);
                                String finalTime2 = sdfs.format(dt2);

                                time_check_tv.setText(finalTime+"-"+finalTime2);
                                // <-- I got result here
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);
                }


            }
        });


    }


    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();

            }
            return false;
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
