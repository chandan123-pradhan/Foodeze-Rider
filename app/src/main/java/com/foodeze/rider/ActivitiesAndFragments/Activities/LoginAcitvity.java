package com.foodeze.rider.ActivitiesAndFragments.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodeze.rider.Constants.AllConstants;
import com.foodeze.rider.Constants.ApiRequest;
import com.foodeze.rider.Constants.Callback;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.Functions;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Utils.FontHelper;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.foodeze.rider.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class LoginAcitvity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sPref;



    FrameLayout login_main_div;

    CamomileSpinner logInProgress;
    RelativeLayout transparent_layer,progressDialog;

    Button log_in_now,signUp;


    TextView loginText,tv_email,tv_pass,sign_up_txt,tv_forget_password;

    EditText ed_email,ed_password;



    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );



    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                |WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.login_activity);

        sPref = getSharedPreferences(PreferenceClass.user,Context.MODE_PRIVATE);


        ed_email = (EditText)findViewById(R.id.ed_email);
        ed_password =(EditText)findViewById(R.id.ed_password);
        log_in_now = (Button)findViewById(R.id.btn_login);
        signUp=(Button)findViewById(R.id.sign_up);


        login_main_div = findViewById(R.id.login_main_div);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent signUpintent=new Intent(LoginAcitvity.this,SignUpActivity.class);
                    startActivity(signUpintent);
                    finish();

            }
        });
        login_main_div.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                return false;
            }
        });



        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_pass = (TextView)findViewById(R.id.tv_password);

        logInProgress = findViewById(R.id.logInProgress);
        logInProgress.start();
        progressDialog = findViewById(R.id.progressDialog);
        transparent_layer = findViewById(R.id.transparent_layer);

        loginText = (TextView)findViewById(R.id.login_title);
        tv_forget_password = findViewById(R.id.tv_forget_password);
        tv_forget_password.setOnClickListener(this);
        FontHelper.applyFont(LoginAcitvity.this,tv_forget_password, AllConstants.arial);

        //

        log_in_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valid = checkEmail(ed_email.getText().toString());

                if (ed_email.getText().toString().trim().equals("")) {

                    Toast.makeText(LoginAcitvity.this, "Enter Email!", Toast.LENGTH_SHORT).show();

                } else if (ed_password.getText().toString().trim().equals("")) {

                    Toast.makeText(LoginAcitvity.this, "Enter Password!", Toast.LENGTH_SHORT).show();
                }else if (ed_password.getText().toString().length()<6) {

                    Toast.makeText(LoginAcitvity.this, "Enter Password Atleat 6 Charaters!", Toast.LENGTH_SHORT).show();
                }
                else if (!valid) {

                    Toast.makeText(LoginAcitvity.this, "Enter Valid Email!", Toast.LENGTH_SHORT).show();
                }else {

                    String this_email = ed_email.getText().toString();
                    String this_password = ed_password.getText().toString();

                    login(this_email,this_password);

                }
            }
        });




    }

    private void login(String email,String pass){

        Functions.Hide_keyboard(LoginAcitvity.this);

        String _lat = sPref.getString(PreferenceClass.LATITUDE,"");
        String _long = sPref.getString(PreferenceClass.LONGITUDE,"");
        String device_tocken = sPref.getString(PreferenceClass.device_token,"");


        String url = Config.LOGIN_URL;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", pass);
            jsonObject.put("device_token", device_tocken);
            jsonObject.put("role","rider");

            if(_lat.isEmpty()){
                jsonObject.put("lat", "31.5042483");
            }else {
                jsonObject.put("lat", _lat);
            }
            if(_long.isEmpty()){
                jsonObject.put("long", "74.3307944");
            }else {
                jsonObject.put("long", _long);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        transparent_layer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);

        ApiRequest.Call_Api(this, url, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {

                transparent_layer.setVisibility(View.GONE);
                progressDialog.setVisibility(View.GONE);

                try {
                    JSONObject jsonResponse = new JSONObject(resp);


                    int code_id  = Integer.parseInt(jsonResponse.optString("code"));

                    if(code_id == 200) {
                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONObject resultObj = json.getJSONObject("msg");
                        JSONObject json1 = new JSONObject(resultObj.toString());
                        JSONObject resultObj1 = json1.getJSONObject("UserInfo");
                        JSONObject resultObj2 = json1.getJSONObject("User");

                        SharedPreferences.Editor editor = sPref.edit();
                        editor.putString(PreferenceClass.pre_email, ed_email.getText().toString());
                        editor.putString(PreferenceClass.pre_pass, ed_password.getText().toString());
                        editor.putString(PreferenceClass.pre_first, resultObj1.optString("first_name"));
                        editor.putString(PreferenceClass.pre_last, resultObj1.optString("last_name"));
                        editor.putString(PreferenceClass.pre_contact, resultObj1.optString("phone"));
                        editor.putString(PreferenceClass.pre_user_id, resultObj1.optString("user_id"));

                        editor.putBoolean(PreferenceClass.IS_LOGIN, true);
                        editor.commit();


                        if(resultObj2.optString("role").equalsIgnoreCase("rider")){

                            editor.putString(PreferenceClass.USER_TYPE,resultObj2.optString("role"));
                            editor.commit();
                            startActivity(new Intent(LoginAcitvity.this, RiderMainActivity.class));
                            finishAffinity();

                        }


                    }else{


                        JSONObject json = new JSONObject(jsonResponse.toString());
                        Toast.makeText(LoginAcitvity.this,json.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }



    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    @Override
    public void onClick(View view) {


        if(view==tv_forget_password){

            startActivity(new Intent(LoginAcitvity.this, RecoverPasswordActivity.class));

        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }



}
