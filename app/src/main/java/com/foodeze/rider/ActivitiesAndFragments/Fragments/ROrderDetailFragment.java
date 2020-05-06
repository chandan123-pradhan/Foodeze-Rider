package com.foodeze.rider.ActivitiesAndFragments.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.foodeze.rider.Constants.AllConstants;
import com.foodeze.rider.Constants.ApiRequest;
import com.foodeze.rider.Constants.Callback;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Utils.FontHelper;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.RootFragment;
import com.foodeze.rider.R;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class ROrderDetailFragment extends RootFragment {

    ImageView back_icon,info_icon;
    Button confirm_btn;
    String order_number;
    RelativeLayout user_location_div,restaurant_address_div,user_call_div,hotel_call_div,pay_to_rest_div;
    SharedPreferences rDetail_Pref;
    TextView rider_jobs_title,r_hotel_name,order_number_tv,hotel_address_tv,r_total_bil_tv,card_detail_tv,time_tv,hotel_name2,rest_address_tv,
            rest_phone_number,user_name,user_address,user_phone_number,total_tax_tv,delivery_fee_tv,total_payment_tv,card_tv,tip_tv,
            pay_to_rest_tv,sub_total_payment_tv,totalText,inst_txt,inst_txt_user;

    String rest_lat,rest_long,user_lat,user_long,hotel_phone_number,user_phone_number_pref;

    CamomileSpinner customProgress;
    public RelativeLayout progressDialog;
    RelativeLayout transparent_layer;
    String serVerKey,user_id;


    View v;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          v = inflater.inflate(R.layout.rider_order_detail, container, false);
        context=getContext();


        rDetail_Pref = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
        user_id = rDetail_Pref.getString(PreferenceClass.pre_user_id,"");

        FrameLayout frameLayout =v.findViewById(R.id.main_container_order_detail);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        FontHelper.applyFont(getContext(),frameLayout, AllConstants.verdana);

        init(v);
        showRiderTracking();


        return v;
    }

    public void init(View v){
        customProgress = v.findViewById(R.id.customProgress);
        progressDialog = v.findViewById(R.id.progressDialog);
        customProgress.start();
        transparent_layer = v.findViewById(R.id.transparent_layer);
        info_icon = v.findViewById(R.id.info_icon);

        String hotel_name = rDetail_Pref.getString(PreferenceClass.RIDER_HOTEL_NAME,"");
        order_number = rDetail_Pref.getString(PreferenceClass.RIDER_ORDER_NUMBER,"");
        String sub_total = rDetail_Pref.getString(PreferenceClass.RIDER_TOTAL_PAYMENT,"");
        if(sub_total.isEmpty()){
            sub_total = "0";
        }
        String symbol = rDetail_Pref.getString(PreferenceClass.RIDER_ORDER_SYMBOL,"");
        inst_txt_user = v.findViewById(R.id.inst_txt_user);
        inst_txt = v.findViewById(R.id.inst_txt);
        rider_jobs_title = v.findViewById(R.id.rider_jobs_title);
        r_hotel_name = v.findViewById(R.id.r_hotel_name);
        order_number_tv = v.findViewById(R.id.order_number_tv);
        hotel_address_tv = v.findViewById(R.id.hotel_address_tv);
        r_total_bil_tv = v.findViewById(R.id.r_total_bil_tv);
        card_detail_tv = v.findViewById(R.id.card_detail_tv);
        time_tv = v.findViewById(R.id.time_tv);
        hotel_name2 = v.findViewById(R.id.hotel_name2);
        rest_address_tv = v.findViewById(R.id.rest_address_tv);
        rest_phone_number = v.findViewById(R.id.rest_phone_number);
        user_name = v.findViewById(R.id.user_name);
        user_address = v.findViewById(R.id.user_address);
        user_phone_number = v.findViewById(R.id.user_phone_number);
        total_tax_tv = v.findViewById(R.id.total_tax_tv);
        delivery_fee_tv = v.findViewById(R.id.delivery_fee_tv);
        total_payment_tv = v.findViewById(R.id.total_payment_tv);
        card_tv = v.findViewById(R.id.card_tv);
        confirm_btn = v.findViewById(R.id.confirm_btn);
        tip_tv = v.findViewById(R.id.tip_tv);

        pay_to_rest_tv = v.findViewById(R.id.pat_to_rest_tv);
        pay_to_rest_div = v.findViewById(R.id.pay_to_rest_div);

        sub_total_payment_tv = v.findViewById(R.id.sub_total_payment_tv);
        totalText = v.findViewById(R.id.totalText);

        rider_jobs_title.setText("Order #"+order_number);
        order_number_tv.setText("Order #"+order_number);
        r_hotel_name.setText(hotel_name);
        r_total_bil_tv.setText(symbol+sub_total);


        getserverkeyCurrent(order_number);

        hotel_name2.setText(hotel_name);
        user_location_div = v.findViewById(R.id.user_location_div);
        restaurant_address_div = v.findViewById(R.id.restaurant_address_div);

        restaurant_address_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialStyledDialog.Builder(getContext())
                        .setTitle("GOOGLE MAP!")
                        .setDescription("This will open google map to let you track path.")
                        .setPositiveText("GOOGLE MAP")
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                String uri = "http://maps.google.com/maps?daddr=" + rest_lat + "," + rest_long + " (" + "Where the Restaurant is" + ")";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);

                            }
                        })
                        .setNegativeText("LATER")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.cancel();

                            }
                        })
                        .show();

            }
        });

        user_location_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialStyledDialog.Builder(getContext())
                        .setTitle("GOOGLE MAP!")
                        .setDescription("This will open google map to let you track path.")
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setPositiveText("GOOGLE MAP")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                String uri = "http://maps.google.com/maps?daddr=" + user_lat + "," + user_long + " (" + "Where the User Location is" + ")";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);

                            }
                        })
                        .setNegativeText("LATER")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.cancel();

                            }
                        })
                        .show();
            }
        });

        hotel_call_div = v.findViewById(R.id.hotel_call_div);
        user_call_div = v.findViewById(R.id.user_call_div);

        hotel_call_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialStyledDialog.Builder(getContext())
                        .setTitle("MAKE A CALL!")
                        .setDescription("This will call on hotel number.")
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setPositiveText("CALL")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                                phoneIntent.setData(Uri.parse("tel:"+hotel_phone_number));
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(phoneIntent);

                            }
                        })
                        .setNegativeText("LATER")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.cancel();

                            }
                        })
                        .show();
            }
        });

        user_call_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new MaterialStyledDialog.Builder(getContext())
                        .setTitle("MAKE A CALL!")
                        .setDescription("This will call on user number.")
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setPositiveText("CALL")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                                phoneIntent.setData(Uri.parse("tel:"+user_phone_number_pref));
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(phoneIntent);

                            }
                        })
                        .setNegativeText("LATER")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.cancel();

                            }
                        })
                        .show();

            }
        });

        back_icon = v.findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackRiderStatus();
            }
        });


        info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ROrderDetailWithItems restaurantMenuItemsFragment = new ROrderDetailWithItems();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.add(R.id.main_container_order_detail, restaurantMenuItemsFragment,"parent").commit();
            }
        });



        getOrderDetailItems();

    }


    public void showRiderTracking(){


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("order_id",order_number);
        } catch (JSONException e) {

        }

        ApiRequest.Call_Api(context, Config.SHOW_RIDER_TRACKING, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {


                 try {
                    JSONObject jsonResponse = new JSONObject(resp);

                    int code_id  = Integer.parseInt(jsonResponse.optString("code"));

                    if(code_id == 200) {

                        Log.e("MSG",jsonResponse.toString());

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        String jsonarray = json.optString("msg");

                        confirm_btn.setText(jsonarray);

                    }


                }catch (Exception e){

                    e.getMessage();
                }


            }
        });


    }


    public String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }


    public void getserverkeyCurrent(String order_id){
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference();

        final Query query2 =mref.child("RiderOrdersList").child(user_id).child("PendingOrders").orderByChild("order_id").equalTo(order_id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    serVerKey = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void trackRiderStatus(){
        progressDialog.setVisibility(View.VISIBLE);
        transparent_layer.setVisibility(View.VISIBLE);


        String currentTime = getCurrentTimeStamp();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("order_id",order_number);
            jsonObject.put("time",currentTime);
        } catch (JSONException e) {

        }

        ApiRequest.Call_Api(context, Config.TRACK_RIDER_STATUS, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);

                try {
                    JSONObject  jsonResponse = new JSONObject(resp);


                    int code_id  = Integer.parseInt(jsonResponse.optString("code"));

                    if(code_id == 200) {


                        JSONObject json = new JSONObject(jsonResponse.toString());
                        String jsonarray = json.optString("msg");

                        confirm_btn.setText(jsonarray);

                        if(confirm_btn.getText().toString().equalsIgnoreCase("order completed")||
                                confirm_btn.getText().toString().equalsIgnoreCase("order already completed")  ) {

                            final DatabaseReference add_to_onother= FirebaseDatabase.getInstance().getReference()
                                    .child("RiderOrdersList").child(user_id);
                            add_to_onother.child("PendingOrders").child(serVerKey).setValue(null);
                            RJobsFragment rJobsFragment = new RJobsFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_container_order_detail, rJobsFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }


                    }


                }catch (Exception e){
                    e.getMessage();
                }

            }
        });


    }


    String order_user_id,first_name,last_name;
    public void getOrderDetailItems(){
        transparent_layer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);


        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("order_id",order_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Config.SHOW_ORDER_DETAIL, orderJsonObject, new Callback() {
            @Override
            public void Responce(String resp) {

                try {
                    JSONObject jsonResponse = new JSONObject(resp);


                    int code_id  = Integer.parseInt(jsonResponse.optString("code"));

                    if(code_id == 200) {

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonArray = json.getJSONArray("msg");

                        for (int i=0;i<jsonArray.length();i++) {

                            JSONObject allJsonObject = jsonArray.getJSONObject(i);
                            JSONObject orderJsonObject = allJsonObject.getJSONObject("Order");
                            JSONObject userInfoObj = allJsonObject.getJSONObject("UserInfo");
                            JSONObject userAddressObj = allJsonObject.getJSONObject("Address");
                            JSONObject restaurantJsonObject = allJsonObject.getJSONObject("Restaurant");
                             JSONObject restaurantCurrencuObj = restaurantJsonObject.getJSONObject("Currency");
                            String currency_symbol = restaurantCurrencuObj.optString("symbol");
                            String time = orderJsonObject.optString("created");


                            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = dateFormatter.parse(time);

                            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
                            String displayValue = timeFormatter.format(date);

                            time_tv.setText(displayValue);

                            user_lat = userAddressObj.optString("lat");
                            user_long = userAddressObj.optString("long");
                            String tip = orderJsonObject.optString("rider_tip");
                            tip_tv.setText(currency_symbol+" "+tip);
                            String delivery_fee = orderJsonObject.optString("delivery_fee");
                            delivery_fee_tv.setText(currency_symbol+" "+delivery_fee);


                            order_user_id = userInfoObj.optString("user_id");
                            first_name = userInfoObj.optString("first_name");
                            last_name = userInfoObj.optString("last_name");

                            user_name.setText(first_name + " " + last_name);
                            user_phone_number.setText(userInfoObj.optString("phone"));
                            String street_user = userAddressObj.optString("street");
                            String city_user = userAddressObj.optString("city");

                            user_address.setText(street_user + ", " + city_user);
                            inst_txt_user.setText(userAddressObj.optString("instructions"));
                            inst_txt.setText(orderJsonObject.optString("accepted_reason"));

                            String subTotal= orderJsonObject.optString("sub_total");
                            String price = orderJsonObject.optString("price");
                            sub_total_payment_tv.setText(currency_symbol+" "+subTotal);
                            String getPaymentMethodTV = orderJsonObject.optString("cod");
                            if (getPaymentMethodTV.equalsIgnoreCase("0")) {
                                card_tv.setText("Credit Card");
                                card_detail_tv.setText("Credit Card");
                                pay_to_rest_div.setVisibility(View.GONE);
                                total_payment_tv.setText(currency_symbol+" "+price);
                            } else {
                                card_tv.setText("Cash On Delivery");
                                card_detail_tv.setText("Cash On Delivery");
                                String payToRest = String.valueOf(Double.parseDouble(delivery_fee)+Double.parseDouble(tip));
                                pay_to_rest_tv.setText(currency_symbol+" "+String.valueOf (Double.parseDouble(price)-Double.parseDouble(payToRest)));
                                totalText.setText("Collect From Customer");
                                total_payment_tv.setText(currency_symbol+" "+price);

                            }

                            JSONObject restaurantAddress = restaurantJsonObject.getJSONObject("RestaurantLocation");

                            rest_lat =restaurantAddress.optString("lat");
                            rest_long = restaurantAddress.optString("long");
                            String street = restaurantAddress.optString("street");
                            String city = restaurantAddress.optString("city");

                            rest_address_tv.setText(street + ", " + city);

                            hotel_address_tv.setText(street + ", " + city);
                            rest_phone_number.setText(restaurantJsonObject.optString("phone"));

                            String tax = orderJsonObject.optString("tax");

                            String tax_free = restaurantJsonObject.optString("tax_free");
                            if (tax_free.equalsIgnoreCase("1")) {
                                total_tax_tv.setText("(" + "0" + "%)");

                            }
                            else {
                                total_tax_tv.setText(currency_symbol+" "+tax);
                            }

                        }


                        transparent_layer.setVisibility(View.GONE);
                        progressDialog.setVisibility(View.GONE);
                    }


                }catch (Exception e){
                    e.getMessage();

                    transparent_layer.setVisibility(View.GONE);
                    progressDialog.setVisibility(View.GONE);
                }

            }
        });


    }



}
