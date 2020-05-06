package com.foodeze.rider.ActivitiesAndFragments.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.foodeze.rider.Constants.ApiRequest;
import com.foodeze.rider.Constants.Callback;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.R;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class ROnlineStatusActivity extends AppCompatActivity {

    ImageView back_icon;
    SwitchCompat on_line_switch;
    Context context;
    SharedPreferences sPref;
    String user_id, online_status;

    ProgressBar pb_online_status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_online_status);
        sPref = getSharedPreferences(PreferenceClass.user, MODE_PRIVATE);
        online_status = sPref.getString(PreferenceClass.RIDER_ONLINE_STATUS, "");

        context = ROnlineStatusActivity.this;
        initView();
    }

    public void initView() {

        pb_online_status = findViewById(R.id.pb_online_status);
        back_icon = findViewById(R.id.back_icon);
        on_line_switch = findViewById(R.id.on_line_switch);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,RiderMainActivity.class));
                finish();
            }
        });

        if (online_status.equalsIgnoreCase("1")) {
            on_line_switch.setChecked(true);

        } else {
            on_line_switch.setChecked(false);
        }

        on_line_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    getOnlineStatus("0");
                    pb_online_status.setVisibility(View.VISIBLE);
                }

                else {
                    getOnlineStatus("1");
                    pb_online_status.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,RiderMainActivity.class));
        super.onBackPressed();
    }

    public void getOnlineStatus(String status) {

        user_id = sPref.getString(PreferenceClass.pre_user_id, "");

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("online", status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Config.UPDATE_RIDER_STATUS, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    JSONObject jsonObject1 = new JSONObject(resp);

                    int code = Integer.parseInt(jsonObject1.optString("code"));

                    if (code==200){

                        Toast.makeText(context,"Successfully Updated",Toast.LENGTH_SHORT).show();
                        pb_online_status.setVisibility(View.GONE);

                        JSONObject msg=jsonObject1.optJSONObject("msg");
                        JSONObject UserInfo=msg.optJSONObject("UserInfo");

                        String online=UserInfo.optString("online");

                        SharedPreferences.Editor editor = sPref.edit();

                        if(online.equals("1")){
                            editor.putString(PreferenceClass.RIDER_ONLINE_STATUS,"1");
                            editor.commit();
                        }else {
                            editor.putString(PreferenceClass.RIDER_ONLINE_STATUS,"0");
                            editor.commit();
                        }

                         startActivity(new Intent(context,RiderMainActivity.class));
                         finish();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }




}
