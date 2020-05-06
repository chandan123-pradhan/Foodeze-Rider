package com.foodeze.rider.ActivitiesAndFragments.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.foodeze.rider.Constants.AllConstants;
import com.foodeze.rider.Constants.ApiRequest;
import com.foodeze.rider.Constants.Callback;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Services.UpdateLocation;
import com.foodeze.rider.Utils.FontHelper;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.RootFragment;
import com.foodeze.rider.ActivitiesAndFragments.Activities.LoginAcitvity;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.foodeze.rider.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class RProfileFragment extends RootFragment {

    RelativeLayout profile_div,log_out_div,today_job_div,change_password_div,review_div;
    SharedPreferences profile_pref;
    public static boolean FLAG_RIDER;
    String user_id;
    CamomileSpinner orderProgressBar;
    RelativeLayout transparent_layer,progressDialog;
    public static boolean RIDER_REVIEW;

    View v;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.rider_profile_fragment, container, false);
        context=getContext();

        profile_pref = getContext().getSharedPreferences(PreferenceClass.user,Context.MODE_PRIVATE);
        user_id = profile_pref.getString(PreferenceClass.pre_user_id, "");
        FrameLayout frameLayout = v.findViewById(R.id.profile_main_container);
        FontHelper.applyFont(getContext(),frameLayout, AllConstants.verdana);
        init(v);

        return v;
    }

    public void init(View v){

        progressDialog = v.findViewById(R.id.progressDialog);
        transparent_layer = v.findViewById(R.id.transparent_layer);
        orderProgressBar = v.findViewById(R.id.orderProgress);
        orderProgressBar.start();
        profile_div = v.findViewById(R.id.profile_div);
        review_div = v.findViewById(R.id.review_div);
        log_out_div = v.findViewById(R.id.log_out_div);
        today_job_div = v.findViewById(R.id.today_job_div);
        change_password_div = v.findViewById(R.id.change_password_div);
        today_job_div = v.findViewById(R.id.today_job_div);

        log_out_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserOnlineStatus();

            }
        });

        profile_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment restaurantMenuItemsFragment = new RiderAccountInfoFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.add(R.id.profile_main_container, restaurantMenuItemsFragment,"parent").commit();

            }
        });




        change_password_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment restaurantMenuItemsFragment = new ChangePasswordFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.add(R.id.profile_main_container, restaurantMenuItemsFragment,"ParentFragment").commit();
                FLAG_RIDER = true;
            }
        });



        today_job_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment restaurantMenuItemsFragment = new RTodayJobFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.add(R.id.profile_main_container, restaurantMenuItemsFragment,"ParentFragment").commit();
            }
        });

        review_div .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RIDER_REVIEW = true;
                Fragment reviewListFragment = new ReviewListFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.profile_main_container, reviewListFragment,"ParentFragment_MenuItems").commit();
            }
        });

    }

    private void logOutUser(){
        SharedPreferences.Editor editor = profile_pref.edit();
        editor.putString(PreferenceClass.USER_TYPE,"");
        editor.putString(PreferenceClass.pre_email, "");
        editor.putString(PreferenceClass.pre_pass, "");
        editor.putString(PreferenceClass.pre_first, "");
        editor.putString(PreferenceClass.pre_last, "");
        editor.putString(PreferenceClass.pre_contact, "");
        editor.putString(PreferenceClass.pre_user_id, "");

        editor.putBoolean(PreferenceClass.IS_LOGIN, false);
        editor.commit();

        getActivity().startActivity(new Intent(getContext(), LoginAcitvity.class));
       getActivity().finish();

        Intent intent = new Intent(getContext(), UpdateLocation.class);
        getContext().stopService(intent);

    }

    public void showUserOnlineStatus(){

        transparent_layer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);


        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("online", "0");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Config.UPDATE_RIDER_STATUS, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {

                transparent_layer.setVisibility(View.GONE);
                progressDialog.setVisibility(View.GONE);


                try {
                    JSONObject jsonObject1 = new JSONObject(resp);

                    int code = Integer.parseInt(jsonObject1.optString("code"));

                    if (code==200){

                        JSONObject msg=jsonObject1.optJSONObject("msg");
                        JSONObject UserInfo=msg.optJSONObject("UserInfo");

                        String online=UserInfo.optString("online");

                        SharedPreferences.Editor editor = profile_pref.edit();

                        if(online.equals("1")){
                            editor.putString(PreferenceClass.RIDER_ONLINE_STATUS,"1");
                            editor.commit();
                        }else {
                            editor.putString(PreferenceClass.RIDER_ONLINE_STATUS,"0");
                            editor.commit();
                            logOutUser();
                        }



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }


}
