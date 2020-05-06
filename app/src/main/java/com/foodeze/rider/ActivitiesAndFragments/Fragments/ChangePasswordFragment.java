package com.foodeze.rider.ActivitiesAndFragments.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.RootFragment;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.foodeze.rider.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class ChangePasswordFragment extends RootFragment {

    ImageView back_icon;
    EditText old_password,new_password,confirm_password;
    Button btn_change_pass;
    SharedPreferences sharedPreferences;

    CamomileSpinner changePassProgress;
    RelativeLayout transparent_layer,progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.change_password_fragment, container, false);
        sharedPreferences = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);

        init(v);

        return v;
    }

    public void init(View v){
        changePassProgress = v.findViewById(R.id.changePassProgress);
        changePassProgress.start();
        progressDialog = v.findViewById(R.id.progressDialog);
        transparent_layer = v.findViewById(R.id.transparent_layer);
        btn_change_pass = v.findViewById(R.id.btn_change_pass);

        old_password = v.findViewById(R.id.ed_old_pass);
        new_password = v.findViewById(R.id.ed_new_pass);
        confirm_password = v.findViewById(R.id.ed_confirm_pass);

        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (old_password.getText().toString().trim().equals("") || old_password.getText().toString().length()<6) {

                    Toast.makeText(getContext(), "Check password length can not be shorter than 6!", Toast.LENGTH_SHORT).show();
                    old_password.setError("Check password length can not be shorter than 6!");

                } else if (new_password.getText().toString().trim().equals("") || new_password.getText().toString().length()<6) {

                    Toast.makeText(getContext(), "Check password length can not be shorter than 6!", Toast.LENGTH_SHORT).show();
                    new_password.setError("Check password length can not be shorter than 6!");
                }else if (confirm_password.getText().toString().trim().equals("") || confirm_password.getText().toString().length()<6) {

                    Toast.makeText(getContext(), "Check password length can not be shorter than 6!", Toast.LENGTH_SHORT).show();
                    confirm_password.setError("Check password length can not be shorter than 6!");
                }
        else {
                    if (new_password.getText().toString().equals(confirm_password.getText().toString())) {

                        changePasswordVollyRequest();
                    } else {
                        Toast.makeText(getContext(), "Password does not match", Toast.LENGTH_LONG).show();
                        confirm_password.setError("Password does not match");
                        new_password.setError("Password does not match");
                        //passwords not matching.please try again
                    }
                }
            }
        });
        back_icon = v.findViewById(R.id.back_icon);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try  {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

              if(RProfileFragment.FLAG_RIDER){
                    RProfileFragment rJobsFragment = new RProfileFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.change_pass_main_container, rJobsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    RProfileFragment.FLAG_RIDER = true;


                }



            }
        });


    }

  public void changePasswordVollyRequest(){

        String getUser_id = sharedPreferences.getString(PreferenceClass.pre_user_id,"");
        RequestQueue queue = Volley.newRequestQueue(getContext());
       JSONObject jsonObject = new JSONObject();
      try {
          jsonObject.put("user_id",getUser_id);
          jsonObject.put("old_password",old_password.getText().toString());
          jsonObject.put("new_password",new_password.getText().toString());
      } catch (JSONException e) {
          e.printStackTrace();
      }


      JsonObjectRequest requestForChangePass = new JsonObjectRequest(Request.Method.POST, Config.CHANGE_PASSWORD, jsonObject, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {


              Log.d("JSONPost", response.toString());
              String strJson =  response.toString();
              JSONObject jsonResponse = null;

              try {
                  jsonResponse = new JSONObject(strJson);

                  int code_id  = Integer.parseInt(jsonResponse.optString("code"));
                  if (code_id==200){

                      Toast.makeText(getContext(),"Password Changed Successfully",Toast.LENGTH_LONG).show();
                      if(RProfileFragment.FLAG_RIDER){
                          RProfileFragment rJobsFragment = new RProfileFragment();
                          FragmentTransaction transaction = getFragmentManager().beginTransaction();
                          transaction.replace(R.id.change_pass_main_container, rJobsFragment);
                          transaction.addToBackStack(null);
                          transaction.commit();
                          RProfileFragment.FLAG_RIDER = true;

                      }

                  }
                  else {

                      Toast.makeText(getContext(),"Password Not Changed",Toast.LENGTH_LONG).show();
                  }

              } catch (JSONException e) {
                  e.printStackTrace();
              }

          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              Log.d("Volly Error",error.toString());
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

      queue.add(requestForChangePass);

  }


}
